package com.hgzp.core.page;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class BaseQueryInfo {

	private String queryKey; // 查询关键字

	private Date startTime;
	private Date endTime; // 查询结束时间
	private String loginUserId;
	/**
	 * 业务对象缓存key
	 */
	private String cacheDTOKey;

	public BaseQueryInfo(String queryKey, Date startTime, Date endTime) {
		this.queryKey = queryKey;
		this.startTime = startTime;
		this.endTime = endTime;
	}
}
