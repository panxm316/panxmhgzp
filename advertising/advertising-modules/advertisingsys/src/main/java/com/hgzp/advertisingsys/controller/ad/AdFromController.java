package com.hgzp.advertisingsys.controller.ad;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.core.model.Tbadfrom;
import com.hgzp.core.page.*;
import com.hgzp.core.web.BaseController;
import com.hgzp.advertisingsys.service.ad.TbadfromServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AdFromController
 * 创建人：muyn
 * 类描述：广告来源信息设置
 * 创建日期：2023/8/17 11:54
 * @folder ad/AdFromController
 */
@RequestMapping(value = "/ad/adfrom")
@RestController
public class AdFromController extends BaseController {
    @Autowired
    private TbadfromServiceI tbAdFromService;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 通过ID查询单条来源数据
     * 方法功能: 通过ID查询单条来源数据
     * @author muyn
     * @date 2023/8/17 10:58
     * @param id 主键
     * @return com.hgzp.core.page.Json
     */
    @GetMapping(value = "/getTbAdFromById")
    public Json<Tbadfrom> getTbAdFromById(String id){
        Tbadfrom tbAdFrom=tbAdFromService.getById(id);
        return Json.success(tbAdFrom);
    }

    /**
     * 获取来源树
     * 方法功能:   获取来源树
     * @author muyn
     * @date 2023/8/17 10:58
     * @param queryInfo
     * @return com.hgzp.core.page.Json
     */
    @GetMapping(value = "/getTbAdFromTree")
    public Json<List<TreeModel>> getTbAdFromTree(TreeQueryVo queryInfo){
        List<TreeModel> tbAdFromListTree = tbAdFromService.getTbAdFromTree(queryInfo);
        // 将平缓的树列表转为带children的树结构
        // List<TreeModel> tbAdFromListTree = TreeModel.buildTreeModel(tbAdFromListTreeTmp);
        return Json.success(tbAdFromListTree);
    }

    /**
     * 分页查询广告来源
     * 方法功能: 分页查询广告来源
     * @author muyn
     * @date 2023/8/17 10:58
     * @param pageRequeste
     * @param queryInfo
     * @return com.hgzp.core.page.Json
     */
    @GetMapping(value = "/getTbAdFromPageList")
    public Json<List<Tbadfrom>> getTbAdFromPageList(PageRequest pageRequeste, TreeQueryVo queryInfo){
        Page page = getPage(pageRequeste);
        IPage<Tbadfrom> tbAdFromIPage = tbAdFromService.getAdFromPageList(page, queryInfo);
        return Json.success(tbAdFromIPage);
    }

    /**
     * 新增广告来源数据
     * 方法功能: 新增广告来源数据
     * @author muyn
     * @date 2023/8/17 10:58
     * @param tbAdFrom 实例对象
     * @return 实例对象
     */
    @PostMapping(value = "/saveTbAdFrom")
    public Json saveTbAdFrom(@RequestBody Tbadfrom tbAdFrom){
        tbAdFromService.saveAdFrom(tbAdFrom);
        return Json.success(tbAdFrom);
    }

    /**
     * 更新广告来源数据
     * 方法功能:   更新广告来源数据
     * @author muyn
     * @date 2023/8/17 10:58
     * @param tbAdFrom 实例对象
     * @return 实例对象
     */
    @PostMapping(value = "/updateTbAdFrom")
    public Json updateTbAdFrom(@RequestBody Tbadfrom tbAdFrom){
        tbAdFromService.updateAdFrom(tbAdFrom);
        return  Json.success();
    }

    /**
     * 通过主键删除广告来源数据
     * 方法功能:   通过主键删除广告来源数据
     * @author muyn
     * @date 2023/8/17 10:58
     * @param tbAdFromIds 主键
     * @return 是否成功
     */
    @PostMapping(value = "/deleteTbAdFrom")
    public Json deleteTbAdFrom(String tbAdFromIds){
        innerInterceptor.recoredLog();
        tbAdFromService.removeByIds(CollUtil.newArrayList(tbAdFromIds.split(",")));
        return Json.success();
    }


    /**
     * 获取最大序号
     * 方法功能:  获取最大序号
     * @author muyn
     * @date 2023/8/17 10:58
     * @return Json
     */
    @GetMapping("/getMaxSort")
    public Json getMaxSort() {
        Integer maxSort = tbAdFromService.getMaxSort();
        return Json.success(maxSort);
    }

    /**
     * 重置来源扩展
     * 方法功能:  重置来源扩展
     * @author muyn
     * @date 2023/2/2 10:58
     * @return Json
     */
    @GetMapping("/resetAdFromExtend")
    public Json resetAdFromExtend() {
        tbAdFromService.resetAdFromExtend();
        return Json.success();
    }
}
