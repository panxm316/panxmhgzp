package com.hgzp.advertisingsys.service.system.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertisingsys.service.system.TwsysoperatelogServiceI;
import com.hgzp.core.annotation.LogData;
import com.hgzp.core.model.Twsysoperatelog;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.mapper.system.TwsysoperatelogMapper;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import com.hgzp.utils.security.SM4Utils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统管理操作日志 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-09-08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TwsysoperatelogServiceImpl extends ServiceImpl<TwsysoperatelogMapper, Twsysoperatelog> implements TwsysoperatelogServiceI {


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
            if(!"null".equals(changeId) && StrUtil.isNotBlank(changeId)){
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
        vaule = String.join("->", valueChangeList);
        return vaule;
    }

    private Class<?> getTableEntityClass(String tableName) throws ClassNotFoundException {
        String classFullName = "com.hgzp.core.model." + StrUtil.upperFirst(tableName);
        return Class.forName(classFullName);
    }

    private static Object getColumnValue(Class<?> aClass, String vaule, String showColumn) {
        BaseMapper baseMapper = SpringUtil.getBean(StrUtil.lowerFirst(aClass.getSimpleName()) + "Mapper");
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(showColumn);
        //可能为多个
        if(vaule.contains(",")){
            queryWrapper.in("id", vaule.split(","));
        }else{
            queryWrapper.eq("id", vaule);
        }
        List<Map<String, Object>> mapList = baseMapper.selectMaps(queryWrapper);
        if(mapList.size() == 0){
            return null;
        }
        String collect = mapList.stream()
                .map(m -> m.get(showColumn).toString())
                .collect(Collectors.joining(","));
        return collect;
    }

    /**
     * 对比两个对象
     *
     * @param oldObj 旧对象
     * @param newObj 新对象
     */
    @Override
    public String compareTowObject(Object oldObj, Object newObj) throws Exception {
        List<Map<String, String >> list = new ArrayList<>();
        if(oldObj == null){
            //新增
            Class<?> newObjClass = newObj.getClass();
            Field[] declaredFields = newObjClass.getDeclaredFields();
            Map<String, String> r = new HashMap<>();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                if(field.isAnnotationPresent(LogData.class)){
                    LogData annotation = field.getAnnotation(LogData.class);
                    String alias = StrUtil.isBlank(annotation.alias()) ? field.getName() : annotation.alias();
                    if(ObjectUtil.isNotEmpty(field.get(newObj))){
                        String value = field.get(newObj).toString();
                        if(StrUtil.isNotBlank(annotation.mappedBy())){
                            //将需要展示的列由id变为名称
                            value = getMappedValue(value, annotation);
                        }
                        //如果需要解密
                        value = decryptValue(value, annotation);
                        r.put(alias, null + "->" + value);

                    }
                }
            }
            list.add(r);
            return JSON.toJSONString(list);
        }

        if(newObj == null){
            //删除
            Class<?> oldObjClass = oldObj.getClass();
            Field[] declaredFields = oldObjClass.getDeclaredFields();
            Map<String, String> r = new HashMap<>();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                if(field.isAnnotationPresent(LogData.class)){
                    LogData annotation = field.getAnnotation(LogData.class);
                    String alias = StrUtil.isBlank(annotation.alias()) ? field.getName() : annotation.alias();
                    if(ObjectUtil.isNotEmpty(field.get(oldObj))){
                        String value = field.get(oldObj).toString();
                        if(StrUtil.isNotBlank(annotation.mappedBy())){
                            //将需要展示的列由id变为名称
                            value = getMappedValue(value, annotation);
                        }
                        //如果需要解密
                        value = decryptValue(value, annotation);
                        r.put(alias, value);
                    }
                }
            }
            list.add(r);
            return JSON.toJSONString(list);
        }


        //获取对象的class
        Class<?> clazz1 = oldObj.getClass();
        Class<?> clazz2 = newObj.getClass();
        //获取对象的属性列表
        Field[] oldObjField = clazz1.getDeclaredFields();
        Map<String, String> nameMap = new LinkedHashMap<>();
        //遍历属性列表field1
        Map<String, String> r = new LinkedHashMap<>();
        for (Field oldField : oldObjField) {
            if(oldField.isAnnotationPresent(LogData.class)){
                LogData annotation = oldField.getAnnotation(LogData.class);
                //id列存在显示数据名称用的
                if(StrUtil.isNotBlank(annotation.showColumn())){
                    Object fieldValue = ReflectUtil.getFieldValue(oldObj, annotation.showColumn());
                    nameMap.put("名称", fieldValue.toString());
                    continue;
                }
                String newValue = null;
                //获取新对象的字段
                Field newField = ReflectUtil.getField(clazz2, oldField.getName());
                oldField.setAccessible(true);
                if(newField != null){
                    newField.setAccessible(true);
                    //"" 与 null 视为相同
                    Object oldObjValue = ObjectUtil.isEmpty(oldField.get(oldObj)) ? null : oldField.get(oldObj);
                    Object newObjValue = ObjectUtil.isEmpty(newField.get(newObj)) ? null : newField.get(newObj);
                    if (ObjectUtil.notEqual(oldObjValue, newObjValue)) {
                        newValue = newField.get(newObj) + "";
                    }else{
                        continue;
                    }
                }
                String changeValue = oldField.get(oldObj)+"->" + (StrUtil.isBlank(newValue) ? "null" : newValue);
                String alias = StrUtil.isBlank(annotation.alias()) ? oldField.getName() : annotation.alias();
                if(StrUtil.isNotBlank(annotation.mappedBy())){
                    //将需要展示的列由id变为名称
                    changeValue = getMappedValue(changeValue, annotation);
                }
                //如果需要解密
                changeValue = decryptValue(changeValue, annotation);
                //日期格式化
                changeValue = dateValueParse(oldField, changeValue);

                r.put(alias, changeValue);

            }

        }
        if(nameMap.size() > 0){
            list.add(nameMap);
        }
        list.add(r);
        return r.size() == 0 ? null : JSON.toJSONString(list);
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
            vaule = String.join("->", vauleList);
        }
        return vaule;
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

    @Override
    public void saveAsyncSysoperatelogWithoutUser(Twsysoperatelog twsysoperatelog) throws Exception {
        LoginUser loginUser = WebUtil.getLoginUser();
        String clientIp = ServletUtil.getClientIP(WebUtil.getHttpServletRequest());

        twsysoperatelog.setEmployid(loginUser.getUserid());
        twsysoperatelog.setEmployname(loginUser.getUsername());
        twsysoperatelog.setSoperateip(clientIp);
        twsysoperatelog.setSoperatetime(new Date());

        saveAsyncSysoperatelog(twsysoperatelog);
    }

    @Async
    @Override
    public void saveAsyncSysoperatelog(Twsysoperatelog twsysoperatelog){
        this.save(twsysoperatelog);
    }

    @Override
    public Page<Twsysoperatelog> getSysoperatelogPageList(Page<Twsysoperatelog> page, BaseQueryInfo query, String slogtype) {
        LambdaQueryChainWrapper<Twsysoperatelog> twsysoperatelogLqw = this.lambdaQuery();
        twsysoperatelogLqw.ge(query.getStartTime() != null, Twsysoperatelog::getSoperatetime, query.getStartTime());
        if (query.getEndTime() != null) {
            twsysoperatelogLqw.lt(Twsysoperatelog::getSoperatetime, DateUtil.offsetDay(query.getEndTime(), 1));
        }
        Page<Twsysoperatelog> twSysoperatelogPage = twsysoperatelogLqw
                .eq(StrUtil.isNotBlank(slogtype), Twsysoperatelog::getSlogtype, slogtype)
                .and(StrUtil.isNotBlank(query.getQueryKey()), i -> i.like(Twsysoperatelog::getEmployname, query.getQueryKey())
                        .or()
                        .like(Twsysoperatelog::getSremark, query.getQueryKey())
                        .or()
                        .like(Twsysoperatelog::getSoldvalue, query.getQueryKey())
                        .or()
                        .like(Twsysoperatelog::getSnewvalue, query.getQueryKey()))
                .page(page);

        return twSysoperatelogPage;
    }

}
