package com.hgzp.advertisingsys.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hgzp.advertisingsys.service.system.TbmenuServiceI;
import com.hgzp.advertisingsys.service.system.TbrolemenuServiceI;
import com.hgzp.core.model.Tbmenu;
import com.hgzp.core.model.Tbrolemenu;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.TreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MenuController
 * 创建人：wangwk
 * 类描述：菜单设置controller
 * 创建日期：2023/8/16 16:51
 * @folder system/MenuController
 */
@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    TbmenuServiceI tbmenuService;
    @Autowired
    TbrolemenuServiceI tbrolemenuService;


    /**
     * 获取所有的菜单
     * 方法功能:  获取所有的菜单
     * @author wangwk
     * @date 2023/8/17 16:37
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.model.Tbmenu>>
     */
    @GetMapping("/getMenuList")
    public Json<List<Tbmenu>> getMenuList(){
        List<Tbmenu> allmenu = tbmenuService.getMenuList();
        return Json.success(allmenu);
    }


    /**
     * 树形显示所有可用的菜单
     * 方法功能: 树形显示所有可用的菜单
     * @author wangwk
     * @date 2023/8/29 13:42
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.page.TreeModel>>
     */
    @GetMapping("/getMenuTree")
    public Json<List<TreeModel>> getMenuTree() {
        List<TreeModel> menuTree = tbmenuService.getMenuTree();
        return Json.success(menuTree);
    }


    /**
     * 保存菜单
     * 方法功能: 保存菜单
     * @author wangwk
     * @date 2023/8/17 8:43
     * @param tbmenu 菜单
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/saveMenu")
    public Json saveMenu(@RequestBody Tbmenu tbmenu){
        tbmenuService.save(tbmenu);
        return Json.success();
    }

    /**
     * 菜单更新
     * 方法功能: 菜单更新
     * @author wangwk
     * @date 2023/8/17 8:46
     * @param tbmenu
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/updateMenu")
    public Json updateMenu(@RequestBody Tbmenu tbmenu){
        tbmenuService.updateById(tbmenu);
        return Json.success();
    }

    /**
     * 根据id删除菜单
     * 方法功能:  根据id删除菜单
     * @author wangwk
     * @date 2023/8/17 8:52
     * @param id
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/deleteMenuById")
    public Json deleteMenuById(Long id){
        LambdaQueryWrapper<Tbrolemenu> lqw = Wrappers.lambdaQuery();
        lqw.eq(Tbrolemenu::getMenuid,id);
        long count = tbrolemenuService.count(lqw);
        if(count > 0){
            return Json.fail("菜单已经赋权不能删除");
        }
        tbmenuService.removeById(id);
        return Json.success();
    }

    /**
     * 获取下一个序号
     * 方法功能: 获取下一个序号
     * @author wangwk
     * @date 2023/8/22 15:08
     * @param
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     */
    @GetMapping("/getMaxSort")
    public Json<Integer> getMaxSort(){
        return Json.success(tbmenuService.getMaxSort());
    }




}