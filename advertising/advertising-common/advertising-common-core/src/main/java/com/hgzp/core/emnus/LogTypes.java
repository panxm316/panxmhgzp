package com.hgzp.core.emnus;

/**
 *
 * 方法功能: 日志操作类型
 * @author peij
 * @date 2023/9/13 8:56
 */
public enum LogTypes {
    /**
     * 新增
     */
    LOG_TYPES_INSERT("新增"),
    /**
     * 修改
     */
    LOG_TYPES_UPDATE("修改"),
    /**
     * 删除
     */
    LOG_TYPES_DELETE("删除");

    private final String typeStr;

    LogTypes(String typeStr) {
        this.typeStr = typeStr;
    }

    public String getTypeStr() {
        return typeStr;
    }
}
