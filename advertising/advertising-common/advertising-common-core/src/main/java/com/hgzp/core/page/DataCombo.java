package com.hgzp.core.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DataCombo
 * 创建人：wangwk
 * 类描述：下拉列表对象
 * 创建日期：2023/8/24 13:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataCombo implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private Boolean bdefault;

	public DataCombo(String id, String name){
		this.id = id;
		this.name = name;
	}

	

}
