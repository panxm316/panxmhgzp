package com.hgzp.core.emnus;

/**
 * 项目名：hgyuqing-parent
 * 类全名：com.hgzp.common.ResultDefines
 * 创建人：wangwk
 * 类描述：全局返回码
 * 创建日期：2019/4/12 13:55
 */
public enum ResultDefines {

    /**
     * 访问正常
     */
    SUCCESS(20000, "访问正常"),
    /**
     * 用户未登录
     */
    USER_NOT_LOGGED_IN(50001, "用户未登录"),
    /**
     * 未授权访问
     */
    NOT_AUTHORIZATION(50002, "未授权访问"),
    /**
     * 验证码已过期
     */
    KAPTCHA_EXCEPTION(50003, "验证码已过期"),
    /**
     * 数据已存在
     */
    DATA_EXIST(50004, "数据已存在"),
    /**
     * 参数错误
     */
    IllegalArgument(50005, "参数错误"),
    /**
     * 存在子数据
     */
    ChildData_Exist(50006, "存在子数据"),
    /**
     * 用户账户异常
     */
    Account_Exception(50007, "用户状态异常"),
    /**
     * 功能异常"
     */
    Function_Exception(50008,"功能异常"),

    /**
     * 数据不存在"
     */
    DATA_NOTEXIT(50009,"数据不存在"),

    /**
     * 数据分析中
     */
    Data_Analyzing(50010, "数据分析中"),

    /**
     * 数据库死锁
     */
    SYSTEM_BUSY(50011, "系统繁忙，请稍后重试！"),

    /**
     * 统一文件异常
     */
    UFILE_EXCEPTION(50012, "统一文件异常！"),

    /**
     * 数据不支持
     */
    DATA_NOT_SUPPORT(50013, "数据不支持！"),

    /**
     * 状态异常
     */
    IllegalState(50014, "状态异常"),

    DATA_OUT_OF_DATE(50015, "数据发生变化，请刷新再试！"),

    /**
     * 网络请求异常
     */
    HTTP_CONNECT_ERROR(50016, "网络请求异常！"),

    /**
     * 文件上传未完成
     */
    FILE_UPLOAD_UNFINISHED(50017, "文件上传未完成"),

    /**
     * 网络请求异常
     */
    SYS_ERROR(60000, "对不起！系统错误，错误代码:"),




    /**
     * 前端自定义错误信息
     */
    CUS_ERROR_MESSAGE(70000,"自定义错误信息");



    private final Integer code;
    private final String message;

    ResultDefines(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
