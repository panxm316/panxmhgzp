package com.hgzp.advertising.service.finance;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.core.model.Twcostproject;
import com.hgzp.utils.model.FileInfo;

/**
 * <p>
 * 成本表(项目) 服务类
 * </p>
 *
 * @author muyn
 * @since 2024-03-15
 */
public interface TwcostprojectServiceI extends IService<Twcostproject> {
    void importCostProject(FileInfo upfile) throws Exception;

}
