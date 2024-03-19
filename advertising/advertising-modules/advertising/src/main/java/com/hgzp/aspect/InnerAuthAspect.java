package com.hgzp.aspect;

import com.hgzp.core.annotation.InnerAuth;
import com.hgzp.core.constant.SecurityConstants;
import com.hgzp.core.exception.InnerAuthException;
import com.hgzp.utils.WebUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 内部服务调用验证处理
 */
@Aspect
@Component
public class InnerAuthAspect implements Ordered {
    @Around("@annotation(innerAuth)")
    public Object innerAround(ProceedingJoinPoint point, InnerAuth innerAuth) throws Throwable {
        String source = WebUtil.getHttpServletRequest().getHeader(SecurityConstants.FROM_SOURCE);
        // 内部请求验证
        if (!SecurityConstants.INNER.equals(source)) {
            throw new InnerAuthException("没有内部访问权限，不允许访问");
        }

        String userid = WebUtil.getHttpServletRequest().getHeader(SecurityConstants.DETAILS_USER_ID);
        // 用户信息验证
        if (innerAuth.isUser() && (StringUtils.isEmpty(userid) )) {
            throw new InnerAuthException("没有设置用户信息，不允许访问 ");
        }
        return point.proceed();
    }

    /**
     * 确保在权限认证aop执行前执行
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
