package com.hgzp.service.system.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hgzp.core.constant.CacheConstants;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.exception.DataNotSupportException;
import com.hgzp.core.model.Tbemploy;
import com.hgzp.mapper.system.TbemployMapper;
import com.hgzp.service.RedisService;
import com.hgzp.service.common.HgDataChangeRecorderInnerInterceptorI;
import com.hgzp.service.common.impl.MyServiceImpl;
import com.hgzp.service.system.BaseTbemployServiceI;
import com.hgzp.utils.SecurityUtils;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import com.hgzp.utils.security.SM2Utils;
import com.hgzp.utils.security.SM4Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 人员表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public class BaseTbemployServiceImpl extends MyServiceImpl<TbemployMapper, Tbemploy> implements BaseTbemployServiceI {

    @Autowired
    TbemployMapper tbemployMapper;
    @Autowired
    RedisService redisService;


    @Override
    public Tbemploy getTbemployByLoginName(String account){
        Tbemploy tbemploy = new LambdaQueryChainWrapper<>(tbemployMapper)
                .eq(Tbemploy::getSloginname, account)
                .or()
                .eq(Tbemploy::getSphone, SM4Utils.encrypt(account))
                .one();
        return tbemploy;
    }


    @Override
    public void updatePersonSetting(Tbemploy employ) throws Exception {
        //判断手机号是否重复
        if(phoneExist(employ.getSphone(), employ.getId())){
            throw new DataExistException("手机号已存在");
        }
        //判断登录名是否重复
        if(loginNameExist(employ.getSloginname(), employ.getId())){
            throw new DataExistException("登录名已存在");
        }

        Tbemploy tbemploy = new Tbemploy();
        BeanUtils.copyProperties(employ, tbemploy);
        tbemploy.setSloginname(WebUtil.getLoginUser().getLoginname());

        LambdaUpdateWrapper<Tbemploy> lmdw = new LambdaUpdateWrapper<>();
        lmdw.set(Tbemploy::getSemail, employ.getSemail())
                .eq(Tbemploy::getId, employ.getId());
        HgDataChangeRecorderInnerInterceptorI logInterceptor = SpringUtil.getBean(HgDataChangeRecorderInnerInterceptorI.class);
        logInterceptor.recoredLog();
        tbemployMapper.update(tbemploy, lmdw);

        updateRedisLoginUser(tbemploy);
    }

    @Override
    public void updatePassWord(String oldPassword, String newPassword) throws Exception {
        LoginUser loginUser = WebUtil.getLoginUser();
        Tbemploy tbemploy = tbemployMapper.selectById(loginUser.getUserid());

        boolean matchesPassword = SecurityUtils.matchesPassword(SM2Utils.decryptByPrivateKey(oldPassword), tbemploy.getSpassword());
        if(!matchesPassword){
            throw new DataNotSupportException("旧密码不正确");
        }
        tbemploy.setSpassword(SecurityUtils.encryptPassword(SM2Utils.decryptByPrivateKey(newPassword)));
        tbemployMapper.updateById(tbemploy);

        updateRedisLoginUser(tbemploy);

    }

    @Override
    public void updateEmployImg(String imgBase64) throws Exception {
        LoginUser loginUser = WebUtil.getLoginUser();
        Tbemploy tbemploy = new Tbemploy();
        tbemploy.setId(loginUser.getUserid());
        tbemploy.setSimg(imgBase64);
        tbemploy.setSloginname(loginUser.getLoginname());
        tbemployMapper.updateById(tbemploy);

        updateRedisLoginUser(tbemploy);
    }


    /**
     * phoneExist
     * 方法功能: 判断手机号是否重复
     * @author wangwk
     * @date 2023/8/21 16:45
     * @param sphone 电话号码
     * @param employId 人员id
     * @return boolean
     */
    public boolean phoneExist(String sphone, Long employId){
        Long count = this.lambdaQuery()
                .eq(Tbemploy::getSphone,sphone)
                .ne(employId != null, Tbemploy::getId, employId)
                .count();
        return count > 0;
    }

    /**
     * loginNameExist
     * 方法功能: 判断登录名是否重复
     * @author wangwk
     * @date 2023/8/21 16:47
     * @param loginName 登录名
     * @param employId 人员id
     * @return boolean
     */
    public boolean loginNameExist(String loginName, Long employId){
        Long count = this.lambdaQuery()
                .eq(Tbemploy::getSloginname, loginName)
                .ne(employId != null, Tbemploy::getId, employId)
                .count();
        return count > 0;
    }

    public void updateRedisLoginUser(Tbemploy employ){
        String key = CacheConstants.PC_LOGIN_TOKEN_KEY + employ.getSloginname();
        long expire = redisService.getExpire(key);
        LoginUser user = redisService.getCacheObject(key);
        if(StrUtil.isNotBlank(employ.getSimg())){
            user.setSimg(employ.getSimg());
        }
        if(StrUtil.isNotBlank(employ.getSphone())){
            user.setPhone(employ.getSphone());
        }
        if(StrUtil.isNotBlank(employ.getSusername())){
            user.setUsername(employ.getSusername());
        }
        redisService.setCacheObject(key, user, CacheConstants.EXPIRATION, TimeUnit.MINUTES);

    }

    /**
     * 初始化人员密码的过期时间
     * 方法功能: 初始化人员密码的过期时间
     * @author wangwk
     * @date 2023/11/6 10:04
     * @param empId
     * @return void
     */
    @Override
    public void updateDpasslastmodtimebyId(Long empId) throws Exception {
        this.lambdaUpdate()
                .set(Tbemploy::getDpasslastmodtime, new Date())
                .eq(Tbemploy::getId, empId)
                .update();
    }

}
