package com.hgzp.advertisingsys.controller.system;

import com.hgzp.advertisingsys.pagemodel.system.dto.MenuactionDTO;
import com.hgzp.advertisingsys.pagemodel.system.vo.MenuactionVO;
import com.hgzp.advertisingsys.service.system.TbmenuactionServiceI;
import static com.hgzp.core.constant.ValidateParam.*;

import com.hgzp.core.page.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * MenuActionController
 * 创建人：wangwk
 * 类描述：菜单按钮Controller
 * 创建日期：2023/8/30 12:47
 *
 * @folder system/MenuActionController
 */
@Validated
@RestController
@RequestMapping("/system/menuaction")
public class MenuActionController {

    @Autowired
    TbmenuactionServiceI tbmenuactionService;


    /**
     * 根据菜单获取及菜单按钮信息
     * 方法功能: 根据菜单获取 菜单按钮信息
     * @author wangwk
     * @date 2023/8/30 15:18
     * @param menuId  菜单id
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.advertising.pagemodel.system.vo.MenuactionVo>>
     */
    @GetMapping("/getMenuActionListByMenuId")
    public Json<List<MenuactionVO>> getMenuActionListByMenuId(@NotNull(message = "ID不能为空") Long menuId){
        List<MenuactionVO> menuActionList = tbmenuactionService.getMenuActionListByMenuId(menuId);
        return Json.success(menuActionList);
    }

    /**
     * 保存菜单按钮
     * 方法功能: 保存菜单按钮
     * @author wangwk
     * @date 2023/8/30 16:18
     * @param menuactionDTO
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/saveMenuAction")
    public Json saveMenuAction(@Validated(value = add.class) @RequestBody MenuactionDTO menuactionDTO){
        tbmenuactionService.saveMenuAction(menuactionDTO);
        return Json.success();
    }


    /**
     * 更新菜单按钮
     * 方法功能: 更新菜单按钮
     * @author wangwk
     * @date 2023/8/31 16:43
     * @param menuactionDTO
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/updateMenuAction")
    public Json updateMenuAction(@Validated(value = edit.class) @RequestBody MenuactionDTO menuactionDTO){
        tbmenuactionService.updateMenuAction(menuactionDTO);
        return Json.success();
    }


    /**
     * 删除菜单行为
     * 方法功能: 删除菜单行为
     * @author wangwk
     * @date 2023/8/31 17:11
     * @param ids ids为菜单行为id，多个 以逗号隔开
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/deleteMenuAction")
    public Json deleteMenuAction(@NotBlank(message = "ids 不可为空") String ids){
        tbmenuactionService.deleteMenuAction(ids);
        return Json.success();
    }


    /**
     * 验证菜单按钮keycode的唯一性
     * 方法功能:   验证菜单按钮keycode的唯一性
     * @author wangwk
     * @date 2023/9/1 9:15
     * @param actionId 行为id
     * @param keyCode  行为
     * @return com.hgzp.core.page.Json<java.lang.Boolean>
     */
    @GetMapping("/checkActionKeyCodeOnly")
    public Json<Boolean> checkActionKeyCodeOnly(String actionId, @NotBlank(message = "keyCode不可为空") String keyCode){
        boolean codeOnly = tbmenuactionService.checkActionKeyCodeOnly(actionId, keyCode);
        return Json.success(codeOnly);
    }








}
