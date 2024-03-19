package com.hgzp.core.page;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BaseDTO
 * 创建人：peij
 * 类描述：TODO
 * 创建日期：2023/9/12 15:40
 */
@Data
@NoArgsConstructor
public class BaseDTO {
    /**
     * 业务对象缓存key
     */
    private String cacheDTOKey;
}
