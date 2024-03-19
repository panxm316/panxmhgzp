package com.hgzp.core.page;

import java.util.Date;

/**
 * websocket消息类
 * 方法功能:
 * @author peij
 * @date 2023/10/23 15:51
 * @param null
 * @return
 */
public class WebSocketMessage {
	/**  消息id     */
	private String messageId;

	/**  消息发送者   */
	private String fromUser;
	/**  消息接收者   */
	private String toUser;
	/**  消息类型     */
	private int type;
	/**
	 * 消息标题
	 */
	private String messageTitle;
	/**  消息内容     */
	private String messageContent;
	/**  发送日期     */
	private Date date;
	/**
	 * 未读消息条数
	 */
	private Long messageCount;
	/**
	 * 流程实例id
	 */
	private String processInstanceId;

	private Date processInstanceCreate;
	
	/**   静态常量 枚举值   用于  消息类型赋值  简洁直观，根据需要自行添加*/
	public static final int HEART_CHECK_MSG = -1;
	/**  系统消息    */
	public static final int SYSTEM_MSG = 0;
	/**  个人消息    */
	public static final int PERSON_MSG = 1;
	/**  错误消息    */
	public static final int ERROR_MSG = 2;
	/**
	 *   工作流待办消息
	 */
	public static final int FLOW_TodoTask_MSG = 3;
	/**
	 *   工作流通过消息
	 */
	public static final int FLOW_ApprovePass_MSG = 4;
	/**
	 *   工作流否决消息
	 */
	public static final int FLOW_ApproveReject_MSG = 5;

	public WebSocketMessage(){}

	public WebSocketMessage(String toUser, int type, Long messageCount) {
		this.toUser = toUser;
		this.type = type;
		this.messageCount = messageCount;
	}

	public WebSocketMessage(String fromUser, String toUser, int type, String content) {
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.type = type;
		this.messageContent = content;
		this.date = new Date();
	}

	public WebSocketMessage(int type) {
		this.type = type;
	}
	public String getFromUser() {
		return fromUser;
	}
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}
	public String getToUser() {
		return toUser;
	}
	public void setToUser(String toUser) {
		this.toUser = toUser;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	public String getMessageTitle() {
		return messageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public Long getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(Long messageCount) {
		this.messageCount = messageCount;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Date getProcessInstanceCreate() {
		return processInstanceCreate;
	}

	public void setProcessInstanceCreate(Date processInstanceCreate) {
		this.processInstanceCreate = processInstanceCreate;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	@Override
	public String toString() {
		return "WebSocketMessage{" +
				"messageId=" + messageId +
				", fromUser='" + fromUser + '\'' +
				", toUser='" + toUser + '\'' +
				", type=" + type +
				", messageTitle='" + messageTitle + '\'' +
				", messageContent='" + messageContent + '\'' +
				", date=" + date +
				", messageCount=" + messageCount +
				", processInstanceId='" + processInstanceId + '\'' +
				", processInstanceCreate=" + processInstanceCreate +
				'}';
	}
}
