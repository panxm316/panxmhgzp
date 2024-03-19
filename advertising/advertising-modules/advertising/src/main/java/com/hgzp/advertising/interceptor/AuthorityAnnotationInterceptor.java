package com.hgzp.advertising.interceptor;


import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.hgzp.advertising.pagemodel.system.dto.EmpAuthorityDTO;
import com.hgzp.advertising.service.system.TbmenuServiceI;
import com.hgzp.advertising.service.system.TbmenuactionServiceI;
import com.hgzp.advertising.service.system.TbroleServiceI;
import com.hgzp.core.annotation.RlAuthorityUrl;
import com.hgzp.core.constant.CacheConstants;
import com.hgzp.core.constant.SecurityConstants;
import com.hgzp.core.constant.system.AdminFlagConstants;
import com.hgzp.core.constant.system.MenuClassConstants;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.model.Tbmenu;
import com.hgzp.core.model.Tbmenuaction;
import com.hgzp.service.RedisService;
import com.hgzp.service.system.BaseTbmenuServiceI;
import com.hgzp.service.system.BaseTbmenuactionServiceI;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.hgzp.core.constant.SecurityConstants.ROLE_PERMISSION;

/**
 * @description: 权限拦截器
 * @author: wangwk
 * @create：2023/9/18 09:13
 */
public class AuthorityAnnotationInterceptor extends HandlerInterceptorAdapter {

	private final Logger logger = LogManager.getLogger(this.getClass());



	@Autowired
	private TbmenuServiceI menuService;
	@Autowired
	private TbmenuactionServiceI menuactionService;
	@Autowired
	TbroleServiceI tbroleService;
	@Autowired
	RedisService redisService;

	/**
	 * 
	 * 方法名称:preHandle.<br/> 
	 * 方法功能: 拦截方法.<br/>
	 * @author wangwk 
	 * @param request  
	 * @param response
	 * @param handler  
	 * @return
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		String requestType = request.getHeader("X-Requested-With");
		String contextPath = request.getContextPath();
		String uri = request.getRequestURI();
		String functionUrl = uri.substring(uri.indexOf(contextPath) + contextPath.length() + 1);

		HandlerMethod method = (HandlerMethod) handler;

		
		//07包请求需要共享权限
		RlAuthorityUrl rlAuthorityUrl = method.getMethodAnnotation(RlAuthorityUrl.class);
		if (rlAuthorityUrl != null) {
			String url = rlAuthorityUrl.url();
			if (StrUtil.isNotBlank(url)) {
				functionUrl = url;					
			}
		}
		

		System.out.println("functionUrl------------" + functionUrl);

		// 先判断url是否为菜单或按钮的url不是则放行
		boolean isExist = isMenuOrActionUrl(functionUrl);

		LoginUser user = WebUtil.getLoginUser(request);
		if(user.getUserid() == null){
			throw new DataExistException(functionUrl + "人员信息不存在");
		}
		if (!isExist || user.getAdminflag() == AdminFlagConstants.PARTTIME_ADMIN ) {
			EmpAuthorityDTO empAuthorityDTO = redisService.getCacheObject(StrUtil.format(CacheConstants.EMP_AUTH_ID_LIST, user.getUserid()));
			request.setAttribute(ROLE_PERMISSION, empAuthorityDTO);
			return true;
		}

		EmpAuthorityDTO userRoleScope = tbroleService.getUserRoleScopeByMenu(user.getUserid(), functionUrl);

		boolean isHasAuthority = userRoleScope.isHasAuthority();

		request.setAttribute(ROLE_PERMISSION, userRoleScope);

		
		System.out.println("isRole---------------------" + isHasAuthority);

		if (!isHasAuthority) {
			response.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
		
			return false;
		} else {
			return true;
		}
	}

	private boolean isMenuOrActionUrl(String functionUrl) {
		List<Tbmenu> menus = menuService.getTbmenuListBymenuClass(MenuClassConstants.ADVERTISING);
		List<Long> menuIdList = menus.stream().map(Tbmenu::getId).collect(Collectors.toList());
		List<Tbmenuaction> menuActionListByMenuIdList = menuactionService.getMenuActionListByMenuIdList(menuIdList);

		boolean isExist = false;
		for (Tbmenu m : menus) {
			if (m.getSfunctionurl() != null && functionUrl.equals(m.getSfunctionurl().trim())) {
				isExist = true;
				break;
			}
			List<Tbmenuaction> tbmenuactionList = menuActionListByMenuIdList.stream()
					.filter(menuAction -> m.getId().equals(menuAction.getMenuid()))
					.collect(Collectors.toList());
			for (Tbmenuaction ma : tbmenuactionList) {
				if (ma.getSfunctionurl() != null && ma.getSfunctionurl().contains(functionUrl)
						&&  ma.getBuse() != null && ma.getBuse() ) {
					isExist = true;
					break;
				}
			}
			if (isExist) {
				break;
			}
		}
		return isExist;
	}
}