package com.hgzp.core.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.core.page.PageRequest;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * Copyright (C), 2014-2016, 国家复合出版系统工程         
 *    
 * 项目名称：MTXTGZ  
 * 类全名:reslib.controller.BaseController    
 * 类描述：    
 * 创建人：peij    
 * 创建时间：2015年9月17日 下午3:32:33
 * 方法列表： 	initBinder(ServletRequestDataBinder)
			redirectJsp(String, String)
 * 修改历史：   
 *   1、修改人：peij    
 * 		修改时间：2015年9月17日 下午3:32:33    
 * 		修改备注：  
 *	 2、
 *
 * @version  jdk1.7
 *
 */
public class BaseController {

	/**
	 * 
	 * 方法名称:initBinder.<br/> 
	 * 方法功能: 日期字符串数据类型转换.<br/>
	 * @author peij 
	 * @param binder 
	 * @since JDK 1.7
	 */
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		binder.registerCustomEditor(Date.class, new DateCovert());
	}

	/**
	 * 将分页对象转换为 mybatis-plus 的分页对象
	 * @param pageRequest
	 * @return
	 */
	public Page getPage(PageRequest pageRequest){
		List<OrderItem> orders = new ArrayList<>();
		Page page = new Page(pageRequest.getPage(), pageRequest.getPageSize());
		List<PageRequest.Sort> sorts = pageRequest.getSorts();
		for (PageRequest.Sort sort : sorts) {
			OrderItem item;
			if("asc".equals(sort.getDir()) ){
				item = new OrderItem(sort.getProperty(), true);
			}else{
				item = new OrderItem(sort.getProperty(), false);
			}
			orders.add(item);
		}

		page.setOrders(orders);
		return page;
	}




}
