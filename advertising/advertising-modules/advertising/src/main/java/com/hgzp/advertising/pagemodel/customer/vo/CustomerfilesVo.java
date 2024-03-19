package com.hgzp.advertising.pagemodel.customer.vo;

import com.hgzp.core.model.Twcustomerfiles;
import com.hgzp.core.model.Twworkreports;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 CustomerfilesVo
 创建人：songly
 类描述：TODO
 创建日期：2023/11/2 13:28
 */
@Data
public class CustomerfilesVo extends Twcustomerfiles {
    /**
     * Url
     */
    private String durl;
}