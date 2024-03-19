package com.hgzp.advertisingsys.service.system.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.hgzp.advertisingsys.pagemodel.system.dto.MenuactionDTO;
import com.hgzp.advertisingsys.pagemodel.system.vo.MenuactionVO;
import com.hgzp.advertisingsys.service.system.TbmenuactionServiceI;
import com.hgzp.advertisingsys.service.system.TbmenuactionscopeServiceI;
import com.hgzp.core.model.Tbmenuaction;
import com.hgzp.core.model.Tbmenuactionscope;
import com.hgzp.core.model.Tbscope;
import com.hgzp.service.system.impl.BaseTbmenuactionServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单行为表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbmenuactionServiceImpl extends BaseTbmenuactionServiceImpl implements TbmenuactionServiceI {

    @Autowired
    TbmenuactionscopeServiceI tbmenuactionscopeService;



    @Override
    public Map<Long, List<Tbmenuaction>> getMenuActionListGroupByMenuId(List<Long> menuIdList) {
        Map<Long, List<Tbmenuaction>> listMap = getMenuActionListByMenuIdList(menuIdList)
                .stream()
                .collect(Collectors.groupingBy(Tbmenuaction::getMenuid));
        return listMap;
    }


    @Override
    public List<MenuactionVO> getMenuActionListByMenuId(Long menuId) {
        List<MenuactionVO> result = new ArrayList<>();

        List<Tbmenuaction> tbmenuactionList = this.lambdaQuery()
                .eq(Tbmenuaction::getMenuid, menuId)
                .orderByAsc(Tbmenuaction::getIsort)
                .list();

        if(tbmenuactionList.size() == 0){
            return result;
        }
        List<Long> menuActionIdList = tbmenuactionList.stream()
                .map(Tbmenuaction::getId)
                .collect(Collectors.toList());

        Map<Long, List<Tbscope>> scopeGroupByMenuActionId = tbmenuactionscopeService.getScopeGroupByMenuActionId(menuActionIdList);

        for (Tbmenuaction tbmenuaction : tbmenuactionList) {
            List<Tbscope> tbscopes = scopeGroupByMenuActionId.get(tbmenuaction.getId());
            MenuactionVO menuactionVo = new MenuactionVO(tbmenuaction);
            if(CollectionUtil.isNotEmpty(tbscopes)){
                menuactionVo.setScopeId(tbscopes.get(0).getId());
                menuactionVo.setScopeName(tbscopes.get(0).getSname());
            }
            result.add(menuactionVo);
        }
        return result;
    }

    @Override
    public void saveMenuAction(MenuactionDTO menuactionDTO){
        long menuActionId = IdUtil.getSnowflakeNextId();

        Tbmenuaction tbmenuaction = new Tbmenuaction();
        BeanUtils.copyProperties(menuactionDTO, tbmenuaction);
        tbmenuaction.setId(menuActionId);
        this.save(tbmenuaction);

        if(menuactionDTO.getScopeId() != null){
            Tbmenuactionscope tbmenuactionscope = new Tbmenuactionscope(menuActionId, menuactionDTO.getScopeId());
            tbmenuactionscopeService.save(tbmenuactionscope);
        }

    }

    @Override
    public void updateMenuAction(MenuactionDTO menuactionDTO){
        Tbmenuaction tbmenuaction = new Tbmenuaction();
        BeanUtils.copyProperties(menuactionDTO, tbmenuaction);
        this.updateById(tbmenuaction);

        Tbmenuactionscope tbmenuactionscope = tbmenuactionscopeService.lambdaQuery()
                .eq(Tbmenuactionscope::getMenuactionid, menuactionDTO.getId())
                .one();

        if(menuactionDTO.getScopeId() != null){
            if(tbmenuactionscope == null){
                Tbmenuactionscope newActionScope = new Tbmenuactionscope(tbmenuaction.getId(), menuactionDTO.getScopeId());
                tbmenuactionscopeService.save(newActionScope);
            }else{
                tbmenuactionscope.setScopeid(menuactionDTO.getScopeId());
                tbmenuactionscopeService.updateById(tbmenuactionscope);
            }

        }else{
            if(tbmenuactionscope != null){
                tbmenuactionscopeService.removeById(tbmenuactionscope);
            }
        }

    }

    @Override
    public void deleteMenuAction(String ids){
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        Long count = tbmenuactionscopeService.lambdaQuery()
                .in(Tbmenuactionscope::getMenuactionid, idList)
                .count();
        if(count > 0){
            throw new IllegalStateException("菜单行为已关联范围！请解除关联后在删除。");
        }
        this.removeBatchByIds(idList);
    }

    @Override
    public boolean checkActionKeyCodeOnly(String actionId, String keyCode){
        Long count = this.lambdaQuery()
                .eq(Tbmenuaction::getSkeycode, keyCode)
                .ne(StrUtil.isNotBlank(actionId), Tbmenuaction::getId, actionId)
                .count();
        return count == 0;
    }



}
