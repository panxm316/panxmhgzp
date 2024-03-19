package com.hgzp.core.page;

import com.hgzp.core.model.Tbmenu;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;



/**
 * Menu
 * 创建人：wangwk
 * 类描述：菜单模型
 * 创建日期：2023/8/17 15:36
 */
@Data
public class Menu implements java.io.Serializable{


	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private Long id;

	/**
	 * 名称
	 */
	private String sname;

	/**
	 * url
	 */
	private String sfunctionurl;

	/**
	 * 图片
	 */
	private String simageurl;

	/**
	 * 前端路由地址
	 */
	private String srouterpath;

	/**
	 * 前端组件名称
	 */
	private String scomponent;

	/**
	 * 深度
	 */
	private Integer idepth;

	/**
	 * 父id
	 */
	private Long parentid;

	/**
	 * 序号
	 */
	private Integer isort;

	/**
	 * 菜单权限使用别名
	 */
	private String sothername;

	/**
	 * 子菜单
	 */
	private List<Menu> menus = new ArrayList<>(0);

	public static Menu convertMenu(Tbmenu tbmenu){
		Menu menu = new Menu();
		BeanUtils.copyProperties(tbmenu, menu);
		return menu;
	}

}
