package com.hgzp.advertising.pagemodel.customer.vo;


import com.hgzp.core.model.ProcessInstanceRecord;
import com.hgzp.core.model.Twcustomer;
import com.hgzp.core.model.Twcustomerbelong;
import com.hgzp.core.model.Twcustomerfiles;
import lombok.Data;

import java.util.List;

/**
 CustomerVo
 创建人：songly
 类描述：TODO
 创建日期：2023/10/25 14:50
 */
@Data
public class CustomerVo extends Twcustomer {
    /**
     * 父级名称
     */
    private String parentName;
    /**
     * 流程Id
     */
    private String flowId;
    /** 客户资料 */
    private List<CustomerfilesVo> customerfiles;
    /** 客户归属 */
    private  List<CustomerBelongVo> customerbelong;
    /** 历史流程 */
    private  List<ProcessInstanceVo> customerProcessInstance;
}