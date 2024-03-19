package com.hgzp.utils;

import cn.hutool.extra.servlet.ServletUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 
 * web工具类
 *
 * @version  jdk1.7
 *
 */
public class WebUtil {

	public static final String AUTHENTICATION = "Authorization";
	public static final String PREFIX = "Bearer ";

	public static LoginUser getLoginUser() throws Exception {
		return getLoginUser(getHttpServletRequest());
	}

	/**
	 * 获取安全的当前用户,异常交给全局异常处理
	 *
	 * @author wangxk
	 * @return {@link LoginUser}
	 */
	public static LoginUser getSafeLoginUser() {
		try {
			return getLoginUser(getHttpServletRequest());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取登陆用户信息 （未包含mac地址）
	 * 
	 * @author wangwk 
	 * @param request
	 * @return LoginUser 用户对象
	 * @throws UnsupportedEncodingException 
	 * @since JDK 1.7
	 */
	public static LoginUser getLoginUser(HttpServletRequest request) throws UnsupportedEncodingException {
		Cookie[] cookies = request.getCookies();
		LoginUser user = new LoginUser();
		if(request.getAttribute("LoginUser") != null){
			user = (LoginUser)request.getAttribute("LoginUser");
		}
		
		user.setIp(ServletUtil.getClientIP(request)== null ? "" : ServletUtil.getClientIP(request));


		return user;
	}



	/**
	 * 
	 * 将一个 JavaBean 对象转化为一个 Map
	 * 
	 * @author wangwk 
	 * @param  bean
	 *         要转化的JavaBean 对象
	 * @return 转化出来的 Map 对象
	 * @throws IntrospectionException
	 *        		 如果分析类属性失败
	 * @throws IllegalAccessException
	 * 				如果实例化 JavaBean 失败
	 * @throws InvocationTargetException 
	 * 				如果调用属性的 setter 方法失败
	 * @since JDK 1.7
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map convertBean(Object bean) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		Class type = bean.getClass();
		Map returnMap = new HashMap();
		BeanInfo beanInfo = Introspector.getBeanInfo(type);

		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!"class".equals(propertyName)) {
				Method readMethod = descriptor.getReadMethod();
				Object result = readMethod.invoke(bean, new Object[0]);
				if (result != null) {
					returnMap.put(propertyName, result);
				} else {
					returnMap.put(propertyName, "");
				}
			}
		}
		return returnMap;
	}

	/**
	 * 生成错误代码
	 * 
	 * @author wangwk 
	 * @return 
	 * @since JDK 1.7
	 */
	public static String getErrorCode() {
		return System.currentTimeMillis() + "";
	}

	/**
	 * 过滤html
	 * 
	 * @author wangwk 
	 * @param inputString
	 * @return 
	 * @since JDK 1.7
	 */
	public static String htmlRemoveTag(String inputString) {
		if (inputString == null) {
			return null;
		}
		// 含html标签的字符串
		String htmlStr = inputString;
		String textStr = "";
		Pattern p_html;
		java.util.regex.Matcher m_html;
		try {
			// 定义HTML标签的正则表达式
			String regEx_html = "<[^>]+>";
			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			// 过滤html标签
			htmlStr = m_html.replaceAll("");
			//peij 20230609 保留回车
			textStr = htmlStr;//.replaceAll("\\n","");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 返回文本字符串
		return textStr;
	}
	/**
	 *
	 * 方法名称:getCookieValue.<br/>
	 * 方法功能: 根据cookie名称获取cookie值.<br/>
	 * @author wangwk
	 * @param request
	 * @param name
	 * @return
	 * @since JDK 1.7
	 */
	public static String getCookieValue(HttpServletRequest request, String name){
		Cookie[] cookies = request.getCookies();
		String value = null;
		for (int i = 0; cookies != null && i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if(name.equals(cookie.getName())){
				value = cookie.getValue();
				break;
			}
		}
		return value;
	}
	/**
	 * 
	 * 方法名称:htmlFilterTag.<br/> 
	 * 方法功能:过滤所有标签保留img video a<br/> 
	 * @author Peij 
	 * @param inputString
	 * @return 
	 * @since JDK 1.7
	 */
	public static String htmlFilterTag(String inputString) {
		if (inputString == null) {
			return null;
		}
		// 含html标签的字符串
		String htmlStr = inputString;
		String textStr = "";
		Pattern p_html;
		java.util.regex.Matcher m_html;
		try {
			// 定义HTML标签的正则表达式
			String regEx_html = "(?!<(img|video|a).*?>)<.*?>";
			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			// 过滤html标签
			htmlStr = m_html.replaceAll("");
			textStr = htmlStr.replaceAll("\\n","");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 返回文本字符串
		return textStr;
	}
	public static HttpServletRequest getHttpServletRequest(){
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
	/**
	 * 获取请求token
	 */
	public static String getToken(HttpServletRequest request) {
		String token = request.getHeader(AUTHENTICATION);
		//peij 20221026 如果header中没有token 再从参数中取
		if(!StringUtils.hasText(token)){
			token = request.getParameter("token");
		}
		// 如果前端设置了令牌前缀，则裁剪掉前缀
		if (StringUtils.hasText(token) && token.startsWith(PREFIX)) {
			token = token.replaceFirst(PREFIX, "");
		}
		return token;
	}




}
