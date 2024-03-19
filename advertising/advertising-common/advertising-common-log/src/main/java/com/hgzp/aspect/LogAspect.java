package com.hgzp.aspect;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson2.JSON;
import com.hgzp.annotation.LogRecord;
import com.hgzp.core.model.Twlog;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.LoginInfoVo;
import com.hgzp.filter.PropertyPreExcludeFilter;
import com.hgzp.service.AsyncLogService;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 操作日志记录处理
 *
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 排除敏感属性字段
     */
    public static final String[] EXCLUDE_PROPERTIES = {"password", "oldPassword", "newPassword", "confirmPassword"};

    public static final String[] LOGIN_METHODNAME = {"login"};

    /**
     * 计算操作消耗时间
     */
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<Long>("Cost Time");

    @Autowired
    private AsyncLogService asyncLogService;

    /**
     * 处理请求前执行
     */
    @Before(value = "@annotation(controllerLog)")
    public void boBefore(JoinPoint joinPoint, LogRecord controllerLog) {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, LogRecord controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, LogRecord controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, LogRecord controllerLog, final Exception e, Object jsonResult) {
        try {
            StringBuilder resultMsg = new StringBuilder();

            HttpServletRequest httpServletRequest = WebUtil.getHttpServletRequest();
            LoginUser user = WebUtil.getLoginUser();
            String clientIP = ServletUtil.getClientIP(httpServletRequest);

            // 设置标题
            String title = controllerLog.title();
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            // 设置消耗时间
            long costTime = System.currentTimeMillis() - TIME_THREADLOCAL.get();
            //获取请求参数
            String requestValue = getRequestValue(joinPoint, EXCLUDE_PROPERTIES);

            resultMsg.append("消耗时间：")
                    .append(costTime)
                    .append("\n");
            resultMsg.append("请求参数：")
                    .append(requestValue)
                    .append("\n");
            if (e != null) {
                resultMsg.append("异常信息：")
                        .append(StrUtil.sub(e.getMessage(), 0, 1000))
                        .append("\n");
            }

            //如果是登录接口
            if(Arrays.asList(LOGIN_METHODNAME).contains(methodName)){
                Json result = (Json) jsonResult;
                if(result != null && result.isSuccess()){
                    Object obj = result.getObj();
                    if(obj instanceof LoginInfoVo){
                        user = ((LoginInfoVo)obj).getLoginuser();
                    }

                }
            }


            // *========数据库日志=========*//
            Twlog operLog = new Twlog();
            operLog.setDoperatordate(new Date());
            operLog.setEmployid(user.getUserid());
            operLog.setEmployname(user.getUsername());
            operLog.setSlogtype(title);
            operLog.setSoperatorip(clientIP);
            operLog.setSremark(resultMsg.toString());
            // 保存数据库
            asyncLogService.saveSysLog(operLog);
            System.out.println("生成的id：" + operLog.getId());
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        } finally {
            TIME_THREADLOCAL.remove();
        }
    }


    /**
     * 获取请求的参数
     * @throws Exception 异常
     */
    private String getRequestValue(JoinPoint joinPoint, String[] excludeParamNames) throws Exception {
        HttpServletRequest request = WebUtil.getHttpServletRequest();
        String requestMethod = request.getMethod();
        Map<String, String> paramsMap = ServletUtil.getParamMap(request);
        if (CollectionUtil.isEmpty(paramsMap)
                && (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod))) {
            String params = argsArrayToString(joinPoint.getArgs(), excludeParamNames);
            return StrUtil.sub(params, 0, 2000);
        } else {
            return StrUtil.sub(JSON.toJSONString(paramsMap, excludePropertyPreFilter(excludeParamNames)), 0, 2000);
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (ObjUtil.isNotNull(o) && !isFilterObject(o)) {
                    try {
                        String jsonObj = JSON.toJSONString(o, excludePropertyPreFilter(excludeParamNames));
                        params += jsonObj.toString() + " ";
                    } catch (Exception e) {
                    }
                }
            }
        }
        return params.trim();
    }

    /**
     * 忽略敏感属性
     */
    public PropertyPreExcludeFilter excludePropertyPreFilter(String[] excludeParamNames) {
        return new PropertyPreExcludeFilter().addExcludes(ArrayUtil.addAll(EXCLUDE_PROPERTIES, excludeParamNames));
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
