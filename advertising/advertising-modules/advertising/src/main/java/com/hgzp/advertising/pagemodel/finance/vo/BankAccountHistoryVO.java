package com.hgzp.advertising.pagemodel.finance.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * BankAccountHistoryVO
 * 创建人：suny
 * 类描述：TODO
 * 创建日期：2023/10/27 15:32
 *
 * @测试：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountHistoryVO extends BaseQueryInfo {
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
}
