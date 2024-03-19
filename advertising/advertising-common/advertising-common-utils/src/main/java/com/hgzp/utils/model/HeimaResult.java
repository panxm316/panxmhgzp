package com.hgzp.utils.model;

/**
 * 项目名：hgcb-parent
 * 类全名：com.hgcb.utils.model.HeimaResult
 * 创建人：wangwk
 * 类描述：黑马校对结果
 * 创建日期：2020/9/5 15:12
 */
public class HeimaResult {


    /**
     * ErrCode : 0
     * ErrMsg :
     * PID : null
     */

    private String ErrCode;
    private String ErrMsg;
    private String PID;

    public String getErrCode() {
        return ErrCode;
    }

    public void setErrCode(String ErrCode) {
        this.ErrCode = ErrCode;
    }

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String ErrMsg) {
        this.ErrMsg = ErrMsg;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public boolean isSuccess() {
        return "0".equals(ErrCode);
    }
}
