package com.hgzp.advertising.service.finance;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.core.model.Twcostemploy;
import com.hgzp.utils.model.FileInfo;

/**
 * <p>
 * 成本表(人员) 服务类
 * </p>
 *
 * @author muyn
 * @since 2024-03-15
 */
public interface TwcostemployServiceI extends IService<Twcostemploy> {
    void importCostEmploy(FileInfo upfile) throws Exception;
}
