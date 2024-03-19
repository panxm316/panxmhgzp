package com.hgzp.advertising.pagemodel.price.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AdDisplayFormVO
 * 创建人：suny
 * 类描述：广告形式查询对象
 * 创建日期：2023/11/6 10:18
 *
 * @测试：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdDisplayFormVO extends BaseQueryInfo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 广告形式名称
     */
    private String sname;

    /**
     * 媒体类型key
     */
    private String mediatypekey;

    /**
     * 媒体类型名称
     */
    private String mediatypename;
}
