package com.hgzp.advertisingsys.pagemodel.system;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;


/**
 * ActiveModel
 * 创建人：wangwk
 * 类描述：菜单对应的按钮权限
 * 创建日期：2023/8/25 9:51
 */
@Data
@NoArgsConstructor
public class ActiveModel {

	/**
	 * 操作代码
	 */
	private String activekeycode;

	/**
	 * 按钮名称
	 */
	private String activename;

	/**
	 * 是否选中
	 */
	private boolean checked;

	/**
	 * 按钮url
	 */
	private String sfunctionurl;

	/**
	 *
	 */
//	private boolean isshow;

	/**
	 * 是否树显示
	 */
	private boolean treeScope;

	/**
	 * 范围
	 */
	private List<ScopeModel> scopes;

	/**
	 * 能否修改
	 */
	private boolean bmodify = true;

	/**
	 * 部门深度
	 */
	private int idepth;

	/**
	 * 范围表id
	 */
    private Long scopeid;

	/**
	 * 按钮组，用于同组赋权
	 */
	private String sgroupkey;

	/**
	 * 按钮范围分类
	 */
	private String activeScopeClass;

	/**
	 * 是否全部
	 */
	private boolean ball;

	
	
	public ActiveModel(String activekeycode, String activename, boolean checked,
					   List<ScopeModel> scopes) {
		this.activekeycode = activekeycode;
		this.activename = activename;
		this.checked = checked;
		this.scopes = scopes;
	}


	public ActiveModel(String activekeycode, String activename, String sfunctionurl, boolean bmodify) {
		this.activekeycode = activekeycode;
		this.activename = activename;
		this.sfunctionurl = sfunctionurl;
		this.bmodify = bmodify;
	}


	public ActiveModel(String activekeycode, String activename, String sfunctionurl, boolean bmodify, String sgroupkey) {
		this.activekeycode = activekeycode;
		this.activename = activename;
		this.sfunctionurl = sfunctionurl;
		this.bmodify = bmodify;
		this.sgroupkey = sgroupkey;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ActiveModel that = (ActiveModel) o;
		boolean fieldEqual = Objects.equals(scopeid, that.scopeid) && Objects.equals(activekeycode, that.activekeycode) && Objects.equals(activename, that.activename) && Objects.equals(checked, that.checked);
		boolean listEqual = true;
		if (scopes == that.scopes) {
			return fieldEqual;
		}

		if (scopes != null && that.scopes != null) {
			if (scopes.size() != that.scopes.size()) {
				return false;
			}
			for (ScopeModel scope : scopes) {
				listEqual = that.scopes.contains(scope);
				if (!listEqual) {
					return false;
				}
			}
		} else {
			return false;
		}

		return fieldEqual && listEqual;
	}

	@Override
	public int hashCode() {
		return Objects.hash(activename, checked, scopes, scopeid);
	}
}


