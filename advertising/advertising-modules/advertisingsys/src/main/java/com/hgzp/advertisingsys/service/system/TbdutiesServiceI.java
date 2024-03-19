package com.hgzp.advertisingsys.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.pagemodel.system.vo.DutiesVO;
import com.hgzp.core.model.Tbduties;
import com.hgzp.core.page.DataCombo;
import com.hgzp.service.system.BaseTbdutiesServiceI;

import java.util.List;

/**
 * <p>
 * 人员职务表 服务类
 * </p>
 *
 * @author yanz
 * @since 2023-08-28
 */
public interface TbdutiesServiceI extends BaseTbdutiesServiceI {

    /**
     * getDutyPageList
     * 方法功能:分页：获取人员职务
     *
     * @param page
     * @param vo
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbduties>
     * @author yanz
     * @date 2023/8/31 15:34
     */
    IPage<Tbduties> getDutyPageList(Page<Tbduties> page, DutiesVO vo);

    /**
     * deleteDutyByIds
     * 方法功能:据id批量删除
     *
     * @param idList
     * @return boolean
     * @author yanz
     * @date 2023/8/28 10:29
     */
    boolean deleteDutyByIds(List<String> idList);

    /**
     * isDuplicateSname
     * 方法功能:是否存在同名对象
     *
     * @param id
     * @param sname
     * @return boolean
     * @author yanz
     * @date 2023/8/28 10:31
     */
    boolean isDuplicateSname(Long id, String sname);

    /**
     * getDutiesCombo
     * 方法功能:获取人员职务下拉列表（PS: Tbduties 没有 default 属性，按 iSort 从小到大）
     *
     * @param
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     * @author yanz
     * @date 2023/9/1 11:17
     */
    List<DataCombo> getDutiesCombo() throws Exception;
}
