package com.hgzp.advertising.service.ad.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.service.ad.TwadresourceadorderServiceI;
import com.hgzp.core.model.Twadresourceadorder;
import com.hgzp.mapper.ad.TwadresourceadorderMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 广告资源广告订单关联表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-10
 */
@Service
public class TwadresourceadorderServiceImpl extends MyServiceImpl<TwadresourceadorderMapper, Twadresourceadorder> implements TwadresourceadorderServiceI {

    @Autowired
    TwadresourceadorderMapper adresourceadorderMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public List<Long> getOrderIdByresourceIds(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        List<Long> lsOrderIds = new LambdaQueryChainWrapper<>(adresourceadorderMapper)
                .in(Twadresourceadorder::getAdresourceapplicationid, idList)
                .list()
                .stream()
                .map(Twadresourceadorder::getAdorderid)
                .distinct()
                .collect(Collectors.toList());
        return lsOrderIds;
    }

    @Override
    public List<Long> getResourceaIdbyOrderId(String sOrderIds) {
        List<String> idList = Arrays.asList(sOrderIds.split(","));
        List<Long> lsIds = new LambdaQueryChainWrapper<>(adresourceadorderMapper)
                .in(Twadresourceadorder::getAdorderid, idList)
                .list()
                .stream()
                .map(Twadresourceadorder::getAdresourceapplicationid)
                .distinct()
                .collect(Collectors.toList());
        return lsIds;
    }

    @Override
    public void doReSetResourceAdOrder(Long adOrderId, List<Long> resourceIds) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        //获取当前订单的所有关联关系
        List<Twadresourceadorder> lsAdresourceadorder = new LambdaQueryChainWrapper<>(adresourceadorderMapper)
                .eq(Twadresourceadorder::getAdorderid, adOrderId)
                .list();

        //待删除的关联关系Ids
        List<Long> lsdelIds = new ArrayList<>();
        //待新增的关联关系Ids
        List<Long> lsaddIds = new ArrayList<>();
        if (lsAdresourceadorder.size() > 0) {
            for (Twadresourceadorder adresourceadorder : lsAdresourceadorder) {
                if (!resourceIds.contains(adresourceadorder.getAdresourceapplicationid())) {
                    lsdelIds.add(adresourceadorder.getId());
                }
            }
            //待新增的关联关系Ids
            lsaddIds =
                    resourceIds.stream().filter(item -> !lsAdresourceadorder.stream().map(Twadresourceadorder::getAdresourceapplicationid).collect(Collectors.toList()).contains(item)).collect(Collectors.toList());
        } else {
            lsaddIds = resourceIds;
        }
        if (lsdelIds.size() > 0) {
            innerInterceptor.recoredLog();
            adresourceadorderMapper.deleteBatchIds(lsdelIds);
        }
//新增新的关联关系
        for (Long resourceId : lsaddIds) {
            Twadresourceadorder adresourceadorder = new Twadresourceadorder();
            adresourceadorder.setAdorderid(adOrderId);
            adresourceadorder.setAdresourceapplicationid(resourceId);
            adresourceadorder.setId(IdUtil.getSnowflakeNextId());
            adresourceadorder.setCreateempid(user.getUserid());
            adresourceadorder.setCreateempname(user.getUsername());
            adresourceadorder.setDcreatetime(new Date());

            innerInterceptor.recoredLog();
            save(adresourceadorder);
        }
    }

}
