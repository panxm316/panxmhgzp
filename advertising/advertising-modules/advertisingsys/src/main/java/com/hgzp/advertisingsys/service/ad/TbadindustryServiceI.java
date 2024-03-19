package com.hgzp.advertisingsys.service.ad;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.pagemodel.ad.vo.AdindustryVO;
import com.hgzp.core.model.Tbadindustry;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.page.TreeQueryVo;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * TbadindustryServiceI
 * 广告行业信息 服务类
 * @author muyn
 * @since 2023-08-15
 */
public interface TbadindustryServiceI extends IMyService<Tbadindustry> {
    /**
     * deleteAdIndustry
     * 通过主键删除数据
     * @param tbAdIndustryIds 主键串
     * @return 是否成功
     */
    void deleteAdIndustry(String tbAdIndustryIds);

    /**
     * getAdIndustryPageList
     * 方法功能:  广告行业
     * @author muyn
     * @date 2023/8/17 10:04
     * @param page
     * @param queryInfo
     * @return Json
     */
    IPage<AdindustryVO> getAdIndustryPageList(Page<Tbadindustry> page, TreeQueryVo queryInfo);
    /**
     * 根据id获取行业信息
     * 方法功能:
     * @author songly
     * @date 2024/3/18 10:45
     * @param id
     * @return com.hgzp.advertisingsys.pagemodel.ad.vo.AdindustryVO
     */
    AdindustryVO getTbAdIndustryById(String id);

    /**
     * getTbAdIndustryTree
     * 方法功能： 广告行业树
     * @author: muyn
     * @param queryInfo
     * @return
     */
    List<TreeModel> getTbAdIndustryTree(TreeQueryVo queryInfo);

    /**
     * saveIndustryExtend
     * 方法功能： 保存行业扩展
     * @author: muyn
     * @param adIndustry 行业对象
     * @return
     */
    void saveIndustryExtend(Tbadindustry adIndustry);

    /**
     * saveAdIndustry
     * 方法功能： 重写保存行业方法，增加了判断是否重名
     * @author: muyn
     * @param tbAdIndustry 行业对象
     * @return void
     */
    void saveAdIndustry(Tbadindustry tbAdIndustry);

    /**
     * updateAdIndustry
     * 方法功能： 重写更新行业方法，增加了判断是否重名
     * @author: muyn
     * @param tbAdIndustry 行业对象
     * @return void
     */
    void updateAdIndustry(Tbadindustry tbAdIndustry);

    /**
     * resetAdIndustryExtend
     * 方法功能： 重置行业扩展表
     * @author: muyn
     * @return void
     */
    void resetAdIndustryExtend();
}
