package com.hgzp.advertising.pagemodel.flow;
import com.hgzp.core.model.ProcessInstanceRecord;
import lombok.Data;

/**
 ProcessInstanceRecordVo
 创建人：songly
 类描述：TODO
 创建日期：2023/11/14 12:46
 */
@Data
public class ProcessInstanceRecordVo extends ProcessInstanceRecord {
    /** 业务Id*/
    private String    businessId;
    /**业务名称*/
    private String    businessName;
}