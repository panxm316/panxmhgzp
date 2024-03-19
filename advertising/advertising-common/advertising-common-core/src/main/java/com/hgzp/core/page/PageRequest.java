package com.hgzp.core.page;


import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 分页类封装
 * 
 * @author wangwk
 */
public class PageRequest {

	private int page = 1 ;// 当前页
	private int pageSize = 10;//每页显示记录数	
	
	private String sort = null;  //排序字段
	private String order = null;  //   asc/desc
	
	public PageRequest() {
	}

	public PageRequest(int page, int pageSize) {
		this.page = page;
		this.pageSize = pageSize;
	}

	public PageRequest(int page, int pageSize, String sort, String order) {
		this.page = page;
		this.pageSize = pageSize;
		this.sort = sort;
		this.order = order;
	}

	/**
	 * 获得当前页的页号, 序号从1开始, 默认为1.
	 */
	public int getPage() {
		return page;
	}
	
	
	/**
	 * 设置当前页的页号, 序号从1开始, 低于1时自动调整为1.
	 */
	public void setPage(int page) {
		this.page = page;
		
		if(page < 1){
			this.page = 1;
		}
	}
	
	/**
	 * 获得每页的记录数量, 默认为10.
	 */
	public int getPageSize() {
		return pageSize;
	}
	
	
	/**
	 * 设置每页的记录数量, 低于1时自动调整为1.
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		
		if (pageSize < 1) {
			this.pageSize = 1;
		}
	}
	
	/**
	 * 获得排序字段, 无默认值.
	 */
	public String getSort() {
		return sort;
	}
	
	/**
	 * 设置排序字段
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	/**
	 * 获得排序方向, 无默认值.
	 */
	public String getOrder() {
		return order;
	}
	
	/**
	 * 设置排序方式向.
	 * 
	 * @param orderDir
	 *           
	 */
	public void setOrder(String order) {
		this.order = order;
	}
	
	
	/**
	 * 根据page和pageSize计算当前页第一条记录在总结果集中的位置, 序号从0开始.
	 */
	public int getOffset() {
		return ((page - 1) * pageSize);
	}
	
	/**
	 * 获得排序参数.
	 */
	public List<Sort> getSorts() {
		List<Sort> orders = new ArrayList<Sort>();
		if(StrUtil.isAllNotBlank(sort, order)){
			String[] orderBys = StrUtil.splitToArray(sort, ',');
			String[] orderDirs = StrUtil.splitToArray(order, ',');

			for (int i = 0; i < orderBys.length; i++) {
				orders.add(new Sort(orderBys[i], orderDirs[i]));
			}
		}


		return orders;
	}
	
	
	public static class Sort {
		public static final String ASC = "asc";
		public static final String DESC = "desc";

		private final String property;
		private final String dir;

		public Sort(String property, String dir) {
			this.property = property;
			this.dir = dir;
		}

		public String getProperty() {
			return property;
		}

		public String getDir() {
			return dir;
		}
	}
	
}
