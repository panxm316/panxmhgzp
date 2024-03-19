package com.hgzp.advertising.pagemodel.finance.dto;

import com.hgzp.core.model.Twbankaccounthistory;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * BankAccountHistoryDTO
 * 创建人：suny
 * 类描述：银行流水历史对象
 * 创建日期：2023/10/31 16:29
 *
 * @测试：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountHistoryDTO extends BaseDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 操作员id
     */
    private Long employid;

    /**
     * 操作员名称
     */
    private String employname;

    /**
     * 文件格式
     */
    private String sfileformat;

    /**
     * 统一文件ID
     */
    private String sfileid;

    /**
     * 文件大小
     */
    private String sfilesize;

    /**
     * 源文件名
     */
    private String soriginalfile;

    /**
     * 创建日期
     */
    private Date dcreatetime;

    /**
     * 5 文件类型(银行流水bankaccount)
     */
    private Integer ifilecategory;

    /**
     * 文件描述
     */
    private String sdescription;
    /**
     * 预览地址
     */
    private String durl;

    public BankAccountHistoryDTO(Twbankaccounthistory twbankaccounthistory) {
        BeanUtils.copyProperties(twbankaccounthistory, this);
    }
}
