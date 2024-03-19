package com.hgzp.advertisingsys.pagemodel.system;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * ScopeModel
 * 创建人：wangwk
 * 类描述：按钮权限对应的范围
 * 创建日期：2023/8/25 9:51
 */
@Data
@NoArgsConstructor
public class ScopeModel {

	/**
	 * 范围id
	 */
	private Long scopeid;

	/**
	 * 业务id
	 */
	private Long businessid;

	/**
	 * 业务名称
	 */
	private String businessname;

	/**
	 * 是否选中
	 */
	private boolean checked;

	/**
	 * 图标
	 */
	private String iconSkin;

	/**
	 * 构造树的父id
	 */
	private Long businesspid;
	

	public ScopeModel(Long scopeid, Long businessid, String businessname,
					  boolean checked) {
		this.scopeid = scopeid;
		this.businessid = businessid;
		this.businessname = businessname;
		this.checked = checked;
	}
	public ScopeModel(Long scopeid, Long businessid, String businessname, Long businesspid,
					  boolean checked, String iconSkin) {
		this.scopeid = scopeid;
		this.businessid = businessid;
		this.businesspid = businesspid;
		this.businessname = businessname;
		this.checked = checked;
		this.iconSkin = iconSkin;
		
	}

	public ScopeModel(Long scopeid, Long businessid, String businessname, Long businesspid,
					  boolean checked) {
		this.scopeid = scopeid;
		this.businessid = businessid;
		this.businesspid = businesspid;
		this.businessname = businessname;
		this.checked = checked;

	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ScopeModel that = (ScopeModel) o;
		boolean fieldEqual = Objects.equals(businessid, that.businessid) && Objects.equals(businessname, that.businessname) && Objects.equals(checked, that.checked);
		return fieldEqual;
	}

	@Override
	public int hashCode() {
		return Objects.hash(scopeid, checked);
	}
}


