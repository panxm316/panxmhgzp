package com.hgzp.core.page;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * TreeQueryVo
 * 创建人：muyn
 * 类描述：行业、来源查询对象
 * 创建日期：2023/9/1 10:03
 */
@Data
@NoArgsConstructor
public class TreeQueryVo extends BaseQueryInfo{
	/***
	 * 是否显示根节点
	 */
	private boolean showRoot;
	/***
	 * 根节点名称
	 */
	private String rootName;
}
