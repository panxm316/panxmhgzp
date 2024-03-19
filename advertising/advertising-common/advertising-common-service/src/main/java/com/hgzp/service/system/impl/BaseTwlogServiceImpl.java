package com.hgzp.service.system.impl;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.core.model.Twlog;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.mapper.system.TwlogMapper;
import com.hgzp.service.system.BaseTwlogServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/***
 *
 * 方法功能: 操作日志
 * @author CGD
 * @date 2023/9/6 13:38
 * @param null
 * @return
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseTwlogServiceImpl extends ServiceImpl<TwlogMapper, Twlog> implements BaseTwlogServiceI {
    @Autowired
    TwlogMapper logMapper;

    @Override
    public Page<Twlog> getLogPageList(Page<Twlog> page, BaseQueryInfo query, String slogtype) {
        LambdaQueryChainWrapper<Twlog> twlogLqw = this.lambdaQuery();
        twlogLqw.ge(query.getStartTime() != null, Twlog::getDoperatordate, query.getStartTime());
        if (query.getEndTime() != null) {
            twlogLqw.lt(Twlog::getDoperatordate, DateUtil.offsetDay(query.getEndTime(), 1));
        }
        Page<Twlog> twlogPage = twlogLqw
                .eq(StrUtil.isNotBlank(slogtype), Twlog::getSlogtype, slogtype)
                .and(StrUtil.isNotBlank(query.getQueryKey()), i -> i.like(Twlog::getEmployname, query.getQueryKey())
                        .or()
                        .like(Twlog::getSremark, query.getQueryKey()))
                .page(page);

        return twlogPage;
    }
}
