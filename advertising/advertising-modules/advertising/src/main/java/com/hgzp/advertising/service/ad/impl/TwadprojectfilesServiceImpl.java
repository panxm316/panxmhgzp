package com.hgzp.advertising.service.ad.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.service.ad.TwadprojectfilesServiceI;
import com.hgzp.core.model.Tbemployrole;
import com.hgzp.core.model.Twadprojectfiles;
import com.hgzp.core.model.Twresources;
import com.hgzp.mapper.ad.TwadprojectfilesMapper;
import com.hgzp.mapper.resource.TwresourcesMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author new wei
 * @date 2024/3/16 9:14
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class TwadprojectfilesServiceImpl extends MyServiceImpl<TwadprojectfilesMapper, Twadprojectfiles> implements TwadprojectfilesServiceI {
    @Autowired
    private TwadprojectfilesMapper adprojectfilesMapper;
    @Autowired
    private TwresourcesMapper resourcesMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Override
    public List<Twadprojectfiles> getAdprojectfilesList(String adprojectid) {
        List<Twadprojectfiles> adprojectfilesList =this.lambdaQuery().eq(Twadprojectfiles::getAdprojectid,adprojectid).list();
        return adprojectfilesList;
    }

    @Override
    public Twadprojectfiles getAdprojectfilesbyId(String id) {
        Twadprojectfiles adprojectfiles = this.getById(id);
        return adprojectfiles ;
    }

    @Override
    public void deleteAdprojectfiles(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        innerInterceptor.recoredLog();
        this.removeByIds(idList);
    }

    @Override
    public void saveAdprojectfiles(Twadprojectfiles twadprojectfiles) {
        try{
            LoginUser loginUser = WebUtil.getLoginUser();
            twadprojectfiles.setEmployid(loginUser.getUserid());
            innerInterceptor.recoredLog();
            save(twadprojectfiles);

            //写twresources表
            Twresources resource = new Twresources();
            resource.setDcreatetime(new Date());
            resource.setEmployid(loginUser.getUserid());
            resource.setSdescription(twadprojectfiles.getSdescription());
            resource.setSfileformat(twadprojectfiles.getSfileformat());
            resource.setSfileid(twadprojectfiles.getSfileid());
            resource.setSfilesize(twadprojectfiles.getSfilesize());
            resource.setSfiletype(twadprojectfiles.getSfiletype());
            resource.setSoriginalfile(twadprojectfiles.getSoriginalfile());

            innerInterceptor.recoredLog();
            resourcesMapper.insert(resource);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateAdprojectfiles(Twadprojectfiles twadprojectfiles) {
        try{
            LoginUser loginUser = WebUtil.getLoginUser();
            twadprojectfiles.setEmployid(loginUser.getUserid());
            innerInterceptor.recoredLog();
            updateById(twadprojectfiles);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAdprojectfilesByAdprojectid(String adprojectid) {
        List<Long> idList =  new LambdaQueryChainWrapper<>(adprojectfilesMapper)
                .select(Twadprojectfiles::getId)
                .eq(Twadprojectfiles::getAdprojectid, adprojectid)
                .list()
                .stream()
                .map(Twadprojectfiles::getId)
                .collect(Collectors.toList());
        innerInterceptor.recoredLog();
        this.removeByIds(idList);
    }
}
