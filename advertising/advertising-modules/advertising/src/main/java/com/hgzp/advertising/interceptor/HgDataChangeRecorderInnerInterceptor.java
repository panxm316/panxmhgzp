package com.hgzp.advertising.interceptor;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.inner.DataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.service.system.TwoperatelogServiceI;
import com.hgzp.core.annotation.LogData;
import com.hgzp.core.emnus.LogTypes;
import com.hgzp.core.model.Twoperatelog;
import com.hgzp.core.model.Twsysoperatelog;
import com.hgzp.service.common.HgDataChangeRecorderInnerInterceptorI;
import com.hgzp.utils.security.SM4Utils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.*;

/**
 * HgDataChangeRecorderInnerInterceptor
 * 创建人：wangwk
 * 类描述：自定义 数据变化拦截器
 * 创建日期：2023/9/7 8:53
 */
public class HgDataChangeRecorderInnerInterceptor extends DataChangeRecorderInnerInterceptor implements HgDataChangeRecorderInnerInterceptorI {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

//    @Autowired
    TwoperatelogServiceI twoperatelogService;


    private ThreadLocal<Boolean> recoredFlag = new ThreadLocal<>();
    private ThreadLocal<Long> slaveIdTreadLocal = new ThreadLocal<>();

    /**
     * 记录日志
     */
    @Override
    public void recoredLog(){
        recoredFlag.set(true);
    }

    @Override
    public void recoredLog(Long slaveId) {
        recoredFlag.set(true);
        slaveIdTreadLocal.set(slaveId);
    }

    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        Boolean aBoolean = recoredFlag.get();
        if(aBoolean == null){
            return ;
        }
        try {
            if(!aBoolean){
                return;
            }
        } finally {
            recoredFlag.remove();
        }


        super.beforePrepare(sh, connection, transactionTimeout);
    }

    @Override
    protected void dealOperationResult(OperationResult operationResult) {
        String tableName = operationResult.getTableName();
        if(tableName.contains("*")){
            return ;
        }
        String changedData = operationResult.getChangedData();
        changedData = ReUtil.replaceAll(changedData, "(?<!\\\\)\\\\(?!\"|\\\\)", "\\\\");
        String operation = operationResult.getOperation();
        try {
            //将变化的值分离出来转化为中文
            //[{"ID":"1696809347203284992","SDEPTNAME":"newdata->newdata12","ISORT":"4->2"},{"ID":"1699307910481543168","SDEPTNAME":"newdata->newdata12","ISORT":"7->2"}]
            JSONArray objects = JSONArray.parseArray(changedData);
            Class<?> aClass = getTableEntityClass(tableName);
            LogData classAnnotation = aClass.getAnnotation(LogData.class);
            if(classAnnotation == null){
                return ;
            }
            twoperatelogService = SpringUtil.getBean(TwoperatelogServiceI.class);

            buildChangeLog(tableName, operation, objects, aClass, classAnnotation);

            logger.info("=========数据变化日志：" + JSONObject.toJSONString(operationResult));


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            slaveIdTreadLocal.remove();
        }


    }

    private Class<?> getTableEntityClass(String tableName) throws ClassNotFoundException {
        String classFullName = "com.hgzp.core.model." + StrUtil.upperFirst(tableName);
        return Class.forName(classFullName);
    }


    private void buildChangeLog(String tableName, String operation, JSONArray changeDataArray, Class<?> aClass, LogData classAnnotation) throws Exception {
        JSONObject nameObject = new JSONObject();
        for (int i = 0; i < changeDataArray.size(); i++) {
            JSONObject result = new JSONObject();
            Twoperatelog log = new Twoperatelog();
            log.setSlogtype(getOperationType(operation));
            log.setStablename( tableName);
            log.setRecordslaveid(slaveIdTreadLocal.get());
            log.setSlogname( StrUtil.isBlank(classAnnotation.alias()) ? tableName : classAnnotation.alias());
            List<JSONObject> resultList = new ArrayList<>();
            JSONObject changeData = changeDataArray.getJSONObject(i);

            Set<Map.Entry<String, Object>> changeDataEntries = changeData.entrySet();
            for (Map.Entry<String, Object> entry : changeDataEntries) {
                String key = entry.getKey();
                String vaule = entry.getValue().toString();
                if("ID".equalsIgnoreCase(key)){
                    //获取id
                    Long id = getId(vaule, operation);
                    log.setRecordmasterid(id );
                    Field idField = ReflectUtil.getField(aClass, "id");
                    LogData annotation = idField.getAnnotation(LogData.class);
                    //只有更新记录名称值
                    if(annotation != null && LogTypes.LOG_TYPES_UPDATE.getTypeStr().equals(log.getSlogtype())){
                        String showColumn = annotation.showColumn();
                        if(StrUtil.isNotBlank(showColumn)){
                            //获取mapper查询
                            Object o = getColumnValue(aClass, vaule, showColumn);
                            if(o != null){
                                nameObject.put("名称", o);
                            }
                        }

                    }
                    continue;
                }
                Field field = ReflectUtil.getField(aClass, key.toLowerCase());
                LogData annotation = field.getAnnotation(LogData.class);
                if(annotation != null){
                    if(StrUtil.isNotBlank(annotation.mappedBy())){
                        //将需要展示的列由id变为名称
                        vaule = getMappedValue(vaule, annotation);
                    }
                    //如果字段加密需要解密
                    vaule = decryptValue(vaule, annotation);
                    //如果是日期格式化一下
                    vaule = dateValueParse(field, vaule);
                    String alias = StrUtil.isBlank(annotation.alias()) ? key : annotation.alias();
                    result.put(alias, vaule);
                }
            }
            if(nameObject.size() > 0){
                resultList.add(nameObject);
            }
            if(result.size() > 0){
                resultList.add(result);
            }
            log.setSnewvalue(JSONObject.toJSONString(resultList));

            twoperatelogService.saveAsyncOperatelogWithoutUser(log);
        }
    }

    private String dateValueParse(Field field, String vaule){
        if(field.getType() == Date.class){
            List<String> vauleList = new ArrayList<>();
            for (String value : vaule.split("->")) {
                if(!"null".equals(value) && StrUtil.isNotBlank(value)){
                    DateTime parse = null;
                    try {
                        parse = DateUtil.parse(value, DatePattern.JDK_DATETIME_FORMAT);
                        String formatDateTime = DateUtil.formatDateTime(parse);
                        vauleList.add(formatDateTime);
                    } catch (Exception e) {
                        vauleList.add(value);
                    }

                }else{
                    if(vaule.contains("->")){
                        vauleList.add("null");
                    }
                }
            }
            vaule = String.join("->", vauleList);
        }
        return vaule;
    }




    private String decryptValue(String vaule, LogData annotation) {
        if(annotation.encrypt() == LogData.EncryptType.SM4){
            List<String> vauleList = new ArrayList<>();
            for (String value : vaule.split("->")) {
                if(!"null".equals(value) && StrUtil.isNotBlank(value)){
                    vauleList.add(SM4Utils.decrypt(value));
                }else{
                    if(vaule.contains("->")){
                        vauleList.add("null");
                    }
                }
            }
            // "xxx->"  这种在split时拆分的数组只有一个值会导致后面的值无法对应
            if(vaule.contains("->") && vauleList.size() == 1){
                vauleList.add("null");
            }
            vaule = String.join("->", vauleList);
        }
        return vaule;
    }

    /**
     * getMappedValue
     * 方法功能: 将值转换为关联列的值
     * @author wangwk
     * @date 2023/9/14 9:06
     * @param vaule
     * @param annotation
     * @return java.lang.String
     */
    private String getMappedValue(String vaule, LogData annotation) throws ClassNotFoundException {
        Class<?> tableEntityClass = getTableEntityClass(annotation.mappedBy());
        String[] changeIds = vaule.split("->");
        List<String> valueChangeList = new ArrayList<>();
        for (String changeId : changeIds) {
            if(!"null".equals(changeId)){
                Object columnValue = getColumnValue(tableEntityClass, changeId, annotation.mappedByColumn());
                if(columnValue != null){
                    valueChangeList.add(columnValue.toString());
                }
            }else{
                if(vaule.contains("->")){
                    valueChangeList.add("null");
                }
            }
        }
        // "xxx->"  这种在split时拆分的数组只有一个值会导致后面的值无法对应
        if(vaule.contains("->") && valueChangeList.size() == 1){
            valueChangeList.add("null");
        }
        vaule = String.join("->", valueChangeList);
        return vaule;
    }

    public Long getId(String value, String operation){
        switch (operation){
            case "insert":
                return Long.parseLong(value.split("->")[1]);
            default:
                return Long.parseLong(value);
        }

    }


    private static Object getColumnValue(Class<?> aClass, String vaule, String showColumn) {
        BaseMapper baseMapper = SpringUtil.getBean(StrUtil.lowerFirst(aClass.getSimpleName()) + "Mapper");
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(showColumn)
                .eq("id", vaule);
        List<Map<String, Object>> map = baseMapper.selectMaps(queryWrapper);
        if(map.size() == 0){
            return null;
        }
        Map<String, Object> objectMap = map.get(0);
        return objectMap.get(showColumn);
    }


    public String getOperationType(String operation){
        switch (operation){
            case "insert":
                return LogTypes.LOG_TYPES_INSERT.getTypeStr();
            case "update":
                return LogTypes.LOG_TYPES_UPDATE.getTypeStr();
            case "delete":
                return LogTypes.LOG_TYPES_DELETE.getTypeStr();
            default:
                return operation;
        }
    }





}
