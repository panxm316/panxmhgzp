package com.hgzp.advertisingsys.service.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.core.model.Tbadprintitem;
import com.hgzp.core.model.Tbbusinessentity;

import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 经营主体 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbbusinessentityServiceI extends IMyService<Tbbusinessentity> {

    /**
     * 方法名称: deleteBusinessentity
     * 方法功能:   多个id “,”分隔删除
     *
     * @param ids
     * @return void
     * @author CGD
     * @date 2023/8/16 15:58
     */
    void deleteBusinessentity(String ids) throws Exception;

    /***
     * getBusinessentityPageList
     * 方法功能:  经营主体分页查询
     * @author CGD
     * @date 2023/8/17 10:04
     * @param page
     * @param querykey
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbbusinessentity>
     */
    IPage<Tbbusinessentity> getBusinessentityPageList(Page<Tbbusinessentity> page, String querykey);

    /**
     * isExistBusinessentity
     * 方法功能: 判断经营主体是否存在同名参数
     * @author CGD
     * @date 2023/8/21 16:37
     * @param tbbusinessentity
     * @return boolean
     */
    public boolean isExistBusinessentity(Tbbusinessentity tbbusinessentity);
    /**
     * doDefaultLogic
     * 方法功能: 处理只有一下默认项功能
     * @author songly
     * @date 2023/9/15 10:34
     * @return   Json
     */
    Json doDefaultLogic(Tbbusinessentity tbbusinessentity);

    /**
     * getBzEntityTypeCombo
     * 方法功能:获取经营主体类型列表
     * @author yanz
     * @date 2024/1/25 16:00
     * @param
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     */
    List<DataCombo> getBzEntityTypeCombo();
}
