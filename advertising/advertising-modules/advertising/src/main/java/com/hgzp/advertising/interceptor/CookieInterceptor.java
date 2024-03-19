package com.hgzp.advertising.interceptor;

import cn.hutool.jwt.JWT;
import com.hgzp.core.constant.CacheConstants;
import com.hgzp.core.exception.AuthorizationException;
import com.hgzp.service.RedisService;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 *    
 * 项目名称：cpsn  
 * 类全名:cpsn.interceptor.CookieInterceptor    
 * 类描述：    cookie拦截器   登陆验证
 * 创建人：wangwk    
 * 创建时间：2017年12月28日 上午9:12:51
 * 方法列表： 
 * 修改历史：   
 *   1、修改人：wangwk    
 * 		修改时间：2017年12月28日 上午9:12:51    
 * 		修改备注：  
 *	 2、
 *
 * @version  jdk1.7
 *
 */
public class CookieInterceptor implements HandlerInterceptor {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(CookieInterceptor.class);
	@Autowired
	private RedisService redisService;

	@Value("${des.key}")
	private String desKey;

	/**
	 * 
	 * 方法名称:postHandle.<br/>
	 * 方法功能: 拦截方法.<br/>
	 * 
	 * @author wangwk
	 * @param request
	 * @param response
	 * @param handler
	 * @param modelAndView
	 * @throws Exception
	 * @since JDK 1.7
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String requestType = request.getHeader("X-Requested-With");
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String url = requestUri.substring(contextPath.length());
       

		if (url.contains("/captcha-image")
				|| url.contains("/login")
				|| url.contains("/logOut")
				|| url.contains("/error")
				|| url.contains("/api")
				|| url.contains("/config")
				|| url.contains("/main/index")
				|| url.contains("/apk")
				|| "true".equals(request.getHeader("feignapi"))
				) {// 如果要访问的资源是不需要验证的
			return true;
		}


		boolean bloginflag = true;
		String accessToken = WebUtil.getToken(request);
		if (StringUtils.isEmpty(accessToken)){
			bloginflag = false;
		}

		Object loginname = null;
		if(bloginflag){
			JWT jwt = JWT.of(accessToken);
			loginname = jwt.getPayload("loginname");
			if(loginname == null || StringUtils.isEmpty(loginname.toString())){
				bloginflag = false;
			}
		}
		String loginKey = CacheConstants.PC_LOGIN_TOKEN_KEY + loginname;
		if(bloginflag){
			boolean islogin = redisService.hasKey(loginKey);
			if(!islogin){
				bloginflag = false;
			}
		}

//		if (bloginflag) {
//			LoginUser loginuser = redisService.getCacheObject(loginKey);
//			if (!accessToken.equals(loginuser.getAccessToken())) {
//				bloginflag = false;
//			}
//		}
		

		if (!bloginflag  ) {// 判断cookie与session只要有一个成立就跳转到login   && url.indexOf("/weixin/"
			logger.info("=============cookie=======" + url);
			request.setAttribute("msg", "您还没有登录或登录已超时，请重新登录，然后再刷新本功能！");
			response.setStatus(403);
			return false;
		} else {
			LoginUser loginUser = redisService.getCacheObject(CacheConstants.PC_LOGIN_TOKEN_KEY + loginname);
			request.setAttribute("LoginUser", loginUser);
		}
		
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
