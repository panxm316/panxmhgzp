package com.hgzp.advertising.service.system.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.system.vo.OpinionsReq;
import com.hgzp.advertising.pagemodel.system.vo.OpinionsVO;
import com.hgzp.advertising.service.flow.TbflowServiceI;
import com.hgzp.advertising.service.system.OpinionsServiceI;
import com.hgzp.core.model.Tbflow;
import com.hgzp.core.model.Twopinions;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.mapper.system.TwopinionsMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 常用审批意见 服务实现类
 * </p>
 *
 * @author songly
 * @since 2024-03-07
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OpinionsServiceImpl extends MyServiceImpl<TwopinionsMapper, Twopinions> implements OpinionsServiceI {

    @Autowired
    private HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Resource
    private TwopinionsMapper twopinionsMapper;
    @Autowired
    private TbflowServiceI flowService;

    @Override
    public IPage<OpinionsVO> getOpinionsPageList(Page<Twopinions> page, OpinionsReq query) throws Exception {
        LoginUser loginUser = WebUtil.getLoginUser();
        LambdaQueryWrapper<Twopinions> lqw = Wrappers.lambdaQuery();
        //多个关键字以空格间隔
        if (StrUtil.isNotBlank(query.getQueryKey())) {
            String[] keywords = query.getQueryKey().trim().split(" ");

            lqw.and(item -> {
                for (String skey : keywords) {
                    item.or(StrUtil.isNotBlank(skey), i -> i.like(Twopinions::getSopinion, skey)
                    );
                }
            });
        }
        lqw.and(i -> i.eq(Twopinions::getCreateempid, loginUser.getUserid()).or().eq(Twopinions::getBcommon, 1))
                .eq(ObjectUtil.isNotNull(query.getSflowtype()), Twopinions::getSflowtype, query.getSflowtype())
                .eq(ObjectUtil.isNotNull(query.getIpasstype()), Twopinions::getIpasstype, query.getIpasstype());

        IPage<Twopinions> lsResult = twopinionsMapper.selectPage(page, lqw);
        Page<OpinionsVO> reslutPage = new Page<OpinionsVO>();
        if (lsResult.getTotal() == 0) {
            return reslutPage;
        }
        List<Tbflow> lsTbflow = flowService.getFlowTypeList();
        List<OpinionsVO> lsOpinionsData = new ArrayList<>();
        for (Twopinions record : lsResult.getRecords()) {
            OpinionsVO opinionsVo = new OpinionsVO();
            BeanUtils.copyProperties(record, opinionsVo);
            Tbflow tbflow =
                    lsTbflow.stream().filter(item -> item.getSkey().equals(record.getSflowtype())).findFirst().orElse(null);
            if (tbflow != null) {
                opinionsVo.setSflowtypename(tbflow.getSname());
            }
            lsOpinionsData.add(opinionsVo);
        }
        reslutPage.setRecords(lsOpinionsData);
        reslutPage.setTotal(lsResult.getTotal());
        return reslutPage;
    }

    @Override
    public List<OpinionsVO> getOpinionsList(OpinionsReq query) throws Exception {
        LoginUser loginUser = WebUtil.getLoginUser();
        LambdaQueryWrapper<Twopinions> lqw = Wrappers.lambdaQuery();
        //多个关键字以空格间隔
        if (StrUtil.isNotBlank(query.getQueryKey())) {
            String[] keywords = query.getQueryKey().trim().split(" ");

            lqw.and(item -> {
                for (String skey : keywords) {
                    item.or(StrUtil.isNotBlank(skey), i -> i.like(Twopinions::getSopinion, skey)
                    );
                }
            });
        }
        lqw.and(i -> i.eq(Twopinions::getCreateempid, loginUser.getUserid()).or().eq(Twopinions::getBcommon, 1))
                .eq(ObjectUtil.isNotNull(query.getSflowtype()), Twopinions::getSflowtype, query.getSflowtype())
                .eq(ObjectUtil.isNotNull(query.getIpasstype()), Twopinions::getIpasstype, query.getIpasstype());

        List<Twopinions> lsResult = twopinionsMapper.selectList(lqw);
        List<OpinionsVO> lsOpinionsData = new ArrayList<>();
        if (lsResult.size() == 0) {
            return lsOpinionsData;
        }
        List<Tbflow> lsTbflow = flowService.getFlowTypeList();
        for (Twopinions record : lsResult) {
            OpinionsVO opinionsVo = new OpinionsVO();
            BeanUtils.copyProperties(record, opinionsVo);
            Tbflow tbflow =
                    lsTbflow.stream().filter(item -> item.getSkey().equals(record.getSflowtype())).findFirst().orElse(null);
            if (tbflow != null) {
                opinionsVo.setSflowtypename(tbflow.getSname());
            }
            lsOpinionsData.add(opinionsVo);
        }
        return lsOpinionsData;
    }

    @Override
    public void saveOpinions(Twopinions twopinions) throws Exception {
        if (StrUtil.isBlank(twopinions.getSopinion())) {
            throw new Exception("意见不能为空!");
        }
        if (isExistopinions(twopinions)) {
            throw new Exception("已存在相同的意见!");
        }
        //如果审批类型为空 则认为是通用
        if (StrUtil.isBlank(twopinions.getSflowtype())) {
            twopinions.setBcommon(true);
        }
        LoginUser loginUser = WebUtil.getLoginUser();
        twopinions.setCreateempid(loginUser.getUserid());
        twopinions.setCreateempname(loginUser.getUsername());
        twopinions.setCreatedate(new Date());
        innerInterceptor.recoredLog();
        save(twopinions);
    }

    //判断是否存在相同的意见
    public boolean isExistopinions(Twopinions twopinions) throws Exception {
        LoginUser loginUser = WebUtil.getLoginUser();
        Long count = new LambdaQueryChainWrapper<>(twopinionsMapper)
                .eq(Twopinions::getSopinion, twopinions.getSopinion())
                .and(i -> i.eq(Twopinions::getCreateempid, loginUser.getUserid()).or().eq(Twopinions::getBcommon, true))
                .ne(twopinions.getId() != null, Twopinions::getId, twopinions.getId())
                .count();
        return count > 0;
    }

    @Override
    public void deleteOpinions(String ids) throws Exception {
        List<Long> IdList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        innerInterceptor.recoredLog();
        removeBatchByIds(IdList);
    }

    @Override
    public OpinionsVO getById(Long id) {
        Twopinions twopinions = getById(id);
        OpinionsVO opinionsVO = new OpinionsVO();
        Tbflow tbflow = flowService.getFlowTypeByKey(twopinions.getSflowtype());
        opinionsVO.setSflowtypename(tbflow.getSname());
        BeanUtils.copyProperties(twopinions, opinionsVO);
        return opinionsVO;
    }

    @Override
    public void updateOpinions(Twopinions twopinions) throws Exception {
        if (StrUtil.isBlank(twopinions.getSopinion())) {
            throw new Exception("意见不能为空!");
        }
        if (isExistopinions(twopinions)) {
            throw new Exception("已存在相同的意见!");
        }
        //如果审批类型为空 则认为是通用
        if (StrUtil.isBlank(twopinions.getSflowtype())) {
            twopinions.setBcommon(true);
        }
        LoginUser loginUser = WebUtil.getLoginUser();
        innerInterceptor.recoredLog();
        updateById(twopinions);
    }

}
