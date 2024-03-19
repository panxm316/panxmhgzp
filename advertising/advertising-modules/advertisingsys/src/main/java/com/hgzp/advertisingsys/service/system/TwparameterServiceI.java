package com.hgzp.advertisingsys.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.pagemodel.system.dto.ParameterDTO;
import com.hgzp.advertisingsys.pagemodel.system.vo.RoleVO;
import com.hgzp.advertisingsys.service.system.impl.TwparameterServiceImpl;
import com.hgzp.core.model.Tbmedia;
import com.hgzp.core.model.Tbrole;
import com.hgzp.core.model.Twparameter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.PageRequest;
import org.apache.poi.ss.formula.functions.T;

/**
 * <p>
 * 系统参数表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-15
 */
public interface TwparameterServiceI extends IService<Twparameter> {



    void saveParm(ParameterDTO parameterDTO);

    void updateParm(ParameterDTO parameterDTO);

    IPage<Twparameter> getParameterPageList(Page<Twparameter> page, BaseQueryInfo info) throws Exception;
}

