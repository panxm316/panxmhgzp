package com.hgzp.advertising.controller.system;

import com.hgzp.advertising.pagemodel.system.dto.MymenuDTO;
import com.hgzp.advertising.pagemodel.system.vo.MymenuVO;
import com.hgzp.advertising.service.system.MymenuServiceI;
import com.hgzp.advertising.service.system.TbrolemenuServiceI;
import com.hgzp.core.model.Tbmenu;
import com.hgzp.core.page.TreeModel;
import org.springframework.web.bind.annotation.*;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import com.hgzp.core.page.Json;

import java.util.List;

/**
 * MymenuController
 * 创建人：songly
 * 类描述：我的常用菜单设置
 * 创建日期：2024/01/22 9:13
 *
 * @folder system/MymenuController
 */
@Validated
@RestController
@RequestMapping("/system/mymenu")
public class MymenuController extends BaseController {

    @Autowired
    private MymenuServiceI mymenuService;
    @Autowired
    private TbrolemenuServiceI rolemenuService;

    /**
     * 保存我的常用菜单
     *
     * @param  MymenuDTO 我的菜单数据传输对象
     * @return {@link Json}
     * @author muyn
     * @since 2024-01-22
     */
    @PostMapping("/saveMymenu")
    public Json saveMymenu(@RequestBody MymenuDTO MymenuDTO) {
        try {
            mymenuService.saveMymenu(MymenuDTO);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 删除我的常用菜单
     *
     * @param sMenuIds 我的菜单数据传输对象
     * @return {@link Json}
     * @author muyn
     * @since 2024-01-22
     */
    @PostMapping("/delete")
    public Json delete(String sMenuIds) {
        try {
            mymenuService.delete(sMenuIds);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 获取已设置的常用菜单项
     *
     * @return {@link Json}
     * @author muyn
     * @since 2024-01-22
     */
    @GetMapping("/getMymenu")
    public Json<List<MymenuVO>> getTwmymenu() {
        try {
            return Json.success(mymenuService.getTwmymenu());
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

    /**
     * 获取我的角色菜单树
     * 方法功能:
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.TreeModel>>
     * @author songly
     * @date 2024/1/24 10:46
     */
    @GetMapping("/getMyRolemenu")
    public Json<List<TreeModel>> getMyRolemenu() {
        try {
            return Json.success(rolemenuService.getRoleMenuTree());
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail(e.getMessage());
        }
    }

}
