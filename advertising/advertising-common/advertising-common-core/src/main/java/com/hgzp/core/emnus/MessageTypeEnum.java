package com.hgzp.core.emnus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息类型枚举
 */
@Getter
@AllArgsConstructor
public enum MessageTypeEnum {

    TODO_TASK("TodoTask","待办任务"),
    APPROVE_PASS("ApprovePass","审批通过"),
    APPROVE_REJECT("ApproveReject","审批驳回"),
    TODO_DEBTREASON("TodoDebtReason","欠款原因待办"),
    TODO_TSAKNOTICE("TaskNotice","任务额度完成情况通知"),
    TODO_NOTICE("Notice","通知")
    ;

    private String type;

    private String name;
    public static String getNameByKey(String key) {
        for (MessageTypeEnum value : MessageTypeEnum.values()) {
            if(value.type.equals(key)){
                return value.name;
            }
        }
        return null;
    }

}
