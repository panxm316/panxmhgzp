package com.hgzp.advertising.service.system.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.system.dto.MymenuDTO;
import com.hgzp.advertising.pagemodel.system.vo.MymenuVO;
import com.hgzp.advertising.service.system.MymenuServiceI;
import com.hgzp.advertising.service.system.TbroleServiceI;
import com.hgzp.advertising.service.system.TbrolemenuServiceI;
import com.hgzp.core.model.*;
import com.hgzp.core.page.TreeModel;
import com.hgzp.mapper.system.TbmenuMapper;
import com.hgzp.mapper.system.TwmymenuMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import com.hgzp.service.system.BaseTbemployroleServiceI;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 我的菜单 服务实现类
 * </p>
 *
 * @author muyn
 * @since 2024-01-22
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MymenuServiceImpl extends MyServiceImpl<TwmymenuMapper, Twmymenu> implements MymenuServiceI {

    @Autowired
    BaseTbemployroleServiceI tbemployroleService;
    @Autowired
    private TbrolemenuServiceI tbrolemenuService;
    @Autowired
    TbmenuMapper tbmenuMapper;
    @Autowired
    private HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public void saveMymenu(MymenuDTO mymenuDTO) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        //获取已设置的菜单
        List<Twmymenu> lsMyMenus = this.lambdaQuery()
                .eq(Twmymenu::getCreateempid, user.getUserid())
                .eq(Twmymenu::getBbuiltin, false)
                .list();

        for (Twmymenu item : mymenuDTO.getLsSelectmenu()) {
            Long menuid = item.getMenuid();
            Twmymenu twmymenu = lsMyMenus.stream().filter(i -> i.getMenuid().equals(menuid)).findFirst().orElse(null);

            if(ObjectUtil.isNotNull(twmymenu)){
                //修改
                twmymenu.setIsort(item.getIsort());
                boolean success = updateById(twmymenu);
                if (!success) {
                    throw new RuntimeException("保存失败");
                }
            }else {
                //新增
                Tbmenu menuitem = tbmenuMapper.selectById(menuid);
                Twmymenu twmymenuNew = new Twmymenu();
                twmymenuNew.setCreateempid(user.getUserid());
                twmymenuNew.setCreateempname(user.getUsername());
                twmymenuNew.setDcreatetime(new Date());
                twmymenuNew.setEmploytype(user.getBsalesman() ? 1 : 0);
                twmymenuNew.setMenuid(menuitem.getId());
                twmymenuNew.setParentmenuid(menuitem.getParentid());
                twmymenuNew.setBbuiltin(false);
                twmymenuNew.setBuse(true);
                twmymenuNew.setIsort(item.getIsort());
                //innerInterceptor.recoredLog();
                boolean success = save(twmymenuNew);
                if (!success) {
                    throw new RuntimeException("保存失败");
                }
            }
        }
    }

    @Override
    public void delete(String sMenuIds) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        List<Long> menuIdList = Arrays.stream(sMenuIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<Twmymenu> lsMyMenu = this.lambdaQuery()
                .eq(Twmymenu::getCreateempid, user.getUserid())
                .eq(Twmymenu::getBbuiltin, false)
                .in(Twmymenu::getMenuid, menuIdList)
                .list();
        if (lsMyMenu.size() > 0) {
            List<Long> myMenuIds = lsMyMenu.stream().map(Twmymenu::getId).collect(Collectors.toList());
            boolean success = removeByIds(myMenuIds);
            if (!success) {
                throw new RuntimeException("删除失败");
            }
        }
    }

    @Override
    public Twmymenu getById(Serializable id) {
        Twmymenu twmymenu = super.getById(id);
        if (ObjectUtil.isNull(twmymenu)) {
            throw new RuntimeException("Twmymenu不存在");
        }
        return twmymenu;
    }

    @Override
    public List<MymenuVO> getTwmymenu() throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        //获取我的角色ID
        List<Long> roleIdList = tbemployroleService.getRoldIdListByUserId(user.getUserid());

        //查出所有的菜单
        Map<Long, List<Tbmenu>> menuListGroupByRoldId = tbrolemenuService.getMenuListGroupByRoldId(roleIdList);
        //获取我的菜单ID
        List<Twmymenu> lsTwmymenu = this.lambdaQuery()
                .eq(Twmymenu::getCreateempid, user.getUserid())
                .eq(Twmymenu::getBbuiltin, false)
                .orderByAsc(Twmymenu::getIsort)
                .list();

        //获取角色内菜单
        List<Long> menuIdList = tbrolemenuService.lambdaQuery()
                .select(Tbrolemenu::getMenuid)
                .in(Tbrolemenu::getRoleid, roleIdList)
                .list()
                .stream()
                .map(Tbrolemenu::getMenuid)
                .distinct()
                .collect(Collectors.toList());

        //处理不存在的菜单项
        if (lsTwmymenu.size() > 0) {
            List<Long> lsMenuIds = lsTwmymenu.stream().map(Twmymenu::getMenuid).collect(Collectors.toList());
            //删除不存在的菜单项
            List<Long> notExistmenuIds =lsMenuIds.stream().filter(item -> !menuIdList.contains(item)).collect(Collectors.toList());
            if (notExistmenuIds.size() > 0) {
                List<Long> myMenuIds =
                        lsTwmymenu.stream().filter(item -> notExistmenuIds.contains(item.getMenuid())).map(Twmymenu::getId).collect(Collectors.toList());
                boolean success = removeByIds(myMenuIds);
                if (!success) {
                    throw new RuntimeException("删除失败");
                }
                //删除集合中不存在的菜单项
                List<Twmymenu> lsdelItem =
                        lsTwmymenu.stream().filter(item -> myMenuIds.contains(item.getId())).collect(Collectors.toList());
                lsTwmymenu.removeAll(lsdelItem);
            }
        }

        List<MymenuVO> lsMymenuVO = new ArrayList<>();
        if (lsTwmymenu.size() > 0) {
            List<Long> lsMenuIds = lsTwmymenu.stream().map(Twmymenu::getMenuid).collect(Collectors.toList());
            List<Tbmenu> lsMenu = tbmenuMapper.selectBatchIds(lsMenuIds);
            Map<Long, Tbmenu> mapMenu = lsMenu.stream().collect(Collectors.toMap(Tbmenu::getId, tbmenu -> tbmenu));
            for (Twmymenu twmymenu : lsTwmymenu) {
                MymenuVO mymenuVO = MymenuVO.parseToVO(twmymenu);
                mymenuVO.setMenuName(mapMenu.get(twmymenu.getMenuid()).getSname());
                lsMymenuVO.add(mymenuVO);
            }
        }
        return lsMymenuVO;
    }

}
