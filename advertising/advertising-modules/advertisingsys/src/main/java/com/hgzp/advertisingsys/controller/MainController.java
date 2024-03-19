package com.hgzp.advertisingsys.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.google.code.kaptcha.Producer;
import com.hgzp.advertisingsys.service.system.TbdeptServiceI;
import com.hgzp.advertisingsys.service.system.TbemployServiceI;
import com.hgzp.advertisingsys.service.system.TbmenuServiceI;
import com.hgzp.annotation.LogRecord;
import com.hgzp.core.constant.CacheConstants;
import com.hgzp.core.constant.system.AdminFlagConstants;
import com.hgzp.core.constant.system.MenuClassConstants;
import com.hgzp.core.exception.CaptchaException;
import com.hgzp.core.model.Tbdept;
import com.hgzp.core.model.Tbemploy;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.LoginInfoVo;
import com.hgzp.core.page.RouterVo;
import com.hgzp.service.RedisService;
import com.hgzp.utils.SecurityUtils;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import com.hgzp.utils.security.SM2Utils;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 * MainController
 * 创建人：wangwk
 * 类描述：菜单设置controller
 * 创建日期：2023/8/4 13:13
 * @folder MainController
 */
@RestController
@RequestMapping("/main")
public class MainController {

    @Resource(name = "captchaProducerMath")
    Producer captchaProducerMath;
    @Autowired
    RedisService redisService;
    @Autowired
    TbmenuServiceI tbmenuService;
    @Autowired
    TbemployServiceI tbemployService;
    @Autowired
    TbdeptServiceI tbdeptService;

    @Value("${HighSecurityLevel}")
    boolean highSecurityLevel;

    /**
     * 用户登录
     * 方法功能: 用户登录
     *
     * @param account
     * @param password
     * @param uuid
     * @param code
     * @param request
     * @param response
     * @param session
     * @return com.hgcb.core.page.Json
     * @throws Exception
     */
    @LogRecord(title = "用户登录")
    @PostMapping("/login")
    public Json login(String account, String password, String uuid, String code, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        // 传递过来的密码使用sm2加密，在此处进行解密  20230403 测试添加
        password = SM2Utils.decryptByPrivateKey(password);

        checkCaptcha(code, uuid);

        if (highSecurityLevel) {
            return loginByHighSecurityLevel(account, password, request, response);
        } else {
            return loginCommon(account, password, request, response);
        }


    }

    /**
     *  高安全级别登录验证
     * 方法功能: 高安全级别登录验证、密码过期、密码错误次数验证.<br/>
     * @param account:
     * @param password:
     * @param request:
     * @param response:
     * @return com.hgzp.core.page.Json
     * @throws
     * @author wangwk
     */
    private Json loginByHighSecurityLevel(String account, String password, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Tbemploy tbemploy = tbemployService.getTbemployByLoginName(account);
        if (tbemploy == null) {
            return Json.fail("用户名或密码错误");
        }
        LoginInfoVo loginInfoVo = new LoginInfoVo();

        String userpassword = tbemploy.getSpassword();
        if (!SecurityUtils.matchesPassword(password, userpassword)) {
            //todo
            Long lockLoginTime = 5L;//parameterService.getParameterValue(ParameterEnum.LOGINERROR_LOCKTIME);
            int tryLoginTimes = 3;//parameterService.getParameterValue(ParameterEnum.TRYLOGINTIMES);
            Integer retryCount = redisService.getCacheObject(CacheConstants.PWD_ERR_CNT_KEY + tbemploy.getSusername());
            if (retryCount == null) {
                retryCount = 0;
            }
            if (retryCount >= tryLoginTimes) {
                String errMsg = String.format("密码输入错误%s次，帐户锁定%s分钟", tryLoginTimes, lockLoginTime);
                return Json.fail(errMsg);
            }
            retryCount = retryCount + 1;
            redisService.setCacheObject(CacheConstants.PWD_ERR_CNT_KEY + tbemploy.getSusername(), retryCount, lockLoginTime, TimeUnit.MINUTES);

            return Json.fail("用户名或密码错误");
        }
        if (!tbemploy.getBinner()) {
            return Json.fail("外部人员禁止登录");
        }
        if(AdminFlagConstants.NORMAL_USER == tbemploy.getBadminflag()){
            return Json.fail("禁止登录");
        }

        if (redisService.hasKey(CacheConstants.PWD_ERR_CNT_KEY + tbemploy.getSusername())) {
            redisService.deleteObject(CacheConstants.PWD_ERR_CNT_KEY + tbemploy.getSusername());
        }

        LoginUser user = createLoginUser(tbemploy);

        String accessToken = JWT.create()
                .setPayload("deptid", user.getDeptid())
                .setPayload("loginname", user.getLoginname())
                .setPayload("deptname", user.getDeptname())
                .setPayload("id", user.getUserid())
                .setPayload("username", user.getUsername())
                .setPayload("loginTime", new Date())
                .setSigner(JWTSignerUtil.none()).sign();
        user.setAccessToken(accessToken);
        redisService.setCacheObject(CacheConstants.PC_LOGIN_TOKEN_KEY + user.getLoginname(), user, CacheConstants.EXPIRATION, TimeUnit.MINUTES);


        user.setAccessToken(accessToken);
        loginInfoVo.setLoginuser(user);
        loginInfoVo.setPassexpired(user.isPassexpired());

        return Json.success("登录成功", loginInfoVo);
    }

    /**
     *  普通登录
     * 方法功能: 普通登录
     *
     * @param account
     * @param password
     * @param request
     * @param response
     * @return com.hgcb.core.page.Json
     * @author wangwk
     * @date 2021/4/14 18:34
     */
    private Json<LoginInfoVo> loginCommon(String account, String password, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LoginInfoVo loginInfoVo = new LoginInfoVo();
        Tbemploy tbemploy = tbemployService.getTbemployByLoginName(account);
        if (tbemploy == null) {
            return Json.fail("用户名或密码错误");
        }
        if (!tbemploy.getBinner()) {
            return Json.fail("外部人员禁止登录");
        }
        if(AdminFlagConstants.NORMAL_USER == tbemploy.getBadminflag()){
            return Json.fail("禁止登录");
        }

        String userpassword = tbemploy.getSpassword();
        if (!SecurityUtils.matchesPassword(password, userpassword)) {
            return Json.fail("用户名或密码错误");
        }

        LoginUser user = createLoginUser(tbemploy);

        String accessToken = JWT.create()
                .setPayload("deptid", user.getDeptid())
                .setPayload("loginname", user.getLoginname())
                .setPayload("deptname", user.getDeptname())
                .setPayload("id", user.getUserid())
                .setPayload("username", user.getUsername())
                .setPayload("loginTime", new Date())
                .setSigner(JWTSignerUtil.none()).sign();
        user.setAccessToken(accessToken);

        redisService.setCacheObject(CacheConstants.PC_LOGIN_TOKEN_KEY + user.getLoginname(), user, CacheConstants.EXPIRATION, TimeUnit.MINUTES);

        user.setAccessToken(accessToken);
        loginInfoVo.setLoginuser(user);

        return Json.success("登录成功", loginInfoVo);
    }

    /**
     * 获取登录用户信息
     * 方法功能:  获取登录用户信息
     * @author wangwk
     * @date 2023/8/18 15:39
     * @param
     * @return com.hgzp.core.page.Json
     */
    @RequestMapping("/getInfo")
    public Json<LoginInfoVo> getInfo() throws Exception {
        LoginInfoVo loginInfoVo = new LoginInfoVo();
        LoginUser u = WebUtil.getLoginUser();
        //peij 20210817 系统版本号
        String sysVersion = ResourceBundle.getBundle("version").getString("version");
        //todo 不查
        Tbemploy emp = tbemployService.getTbemployByLoginName(u.getLoginname());
//        List<String> permissions = roleManageService.getUserPermissions(emp.getSloginname());

        if (!"".equals(emp.getSimg())) {
            u.setSimg(emp.getSimg());
        }

        //peij 20230418 cassys地址
//        String cassysURL = baseSetParameterService.getParameterValue(ParameterEnum.CASSYS_URL);

        loginInfoVo.setSysversion(StringUtils.hasText(sysVersion) ? sysVersion : "");
        loginInfoVo.setLoginuser(u);
        // loginInfoVo.setPermissions(permissions);

        return Json.success(loginInfoVo);
    }


    /**
     * 获取登录用户菜单
     * 方法功能: 获取登录用户菜单
     * @author wangwk
     * @date 2023/8/18 10:13
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.page.RouterVo>>
     */
    @RequestMapping("/getRouters")
    public Json<List<RouterVo>> getRouters() throws Exception {
        LoginUser u = WebUtil.getLoginUser();
        List<RouterVo> menus = tbmenuService.getMenuRouterByUserId(u.getUserid(), MenuClassConstants.PUBLICMENU);
        return Json.success(menus);
    }


    /**
     * 系统登出
     * 方法功能: 系统登出
     *
     * @param request
     * @param response
     * @param session
     * @return
     * @author Peij
     * @since JDK 1.7
     */
    @RequestMapping("/logOut")
    public Json logOut(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {

        String accessToken = WebUtil.getToken(request);
        if (StringUtils.hasText(accessToken)) {
            JWT jwt = JWT.of(accessToken);
            Object loginname = jwt.getPayload("loginname");
            redisService.deleteObject(CacheConstants.PC_LOGIN_TOKEN_KEY + loginname);
        }
        return Json.success();
    }




    public void checkCaptcha(String code, String uuid) throws CaptchaException {
        if (StrUtil.isEmpty(code)) {
            throw new CaptchaException("验证码不能为空");
        }
        if (StrUtil.isEmpty(uuid)) {
            throw new CaptchaException("验证码已失效");
        }
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        String captcha = redisService.getCacheObject(verifyKey);
        redisService.deleteObject(verifyKey);

        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException("验证码错误");
        }
    }


    /**
     * 获取验证码图片
     * 方法功能:验证码图片
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     * @author Peij
     * @since JDK 1.7
     */
    @RequestMapping("/captcha-image")
    public Json getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 保存验证码信息
        String uuid = IdUtil.simpleUUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;

        String capText = captchaProducerMath.createText();
        String capStr = capText.substring(0, capText.lastIndexOf("@"));
        String code = capText.substring(capText.lastIndexOf("@") + 1);
        BufferedImage image = captchaProducerMath.createImage(capStr);

        redisService.setCacheObject(verifyKey, code, 2L, TimeUnit.MINUTES);

        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);

        Dict reslut = Dict.create()
                .set("uuid", uuid)
                .set("img", Base64.encodeBase64String(os.toByteArray()));


        return Json.success(reslut);
    }


    private LoginUser createLoginUser(Tbemploy baseEmploy) throws Exception {

        Tbdept tbdept = tbdeptService.getById(baseEmploy.getDeptid());

        LoginUser user = new LoginUser();


        Long deptId = tbdept == null ? null : tbdept.getId();
        String deptName = tbdept == null ? "" : tbdept.getSdeptname();
        String myphone = baseEmploy.getSphone() == null ? "" : baseEmploy.getSphone();

        user.setUserid(baseEmploy.getId());
        user.setLoginname(baseEmploy.getSloginname());
        user.setAdminflag(baseEmploy.getBadminflag());
        user.setDeptid(deptId);
        user.setDeptname(deptName);
        user.setPhone(myphone);
        user.setUsername(baseEmploy.getSusername());
        user.setSmanagedepts(baseEmploy.getSmanagedepts());
        user.setBinner(baseEmploy.getBinner());

        //muyn210415增加判断是否需要提示密码过期
        if (highSecurityLevel) {
            String expireDays = "30";//baseSetParameterService.getParameterValue(ParameterEnum.PASSWORD_EXPIRE_DAYS);
            Date dtLastModTime = baseEmploy.getDpasslastmodtime();
            if (dtLastModTime == null) {
                dtLastModTime = new Date();
                tbemployService.updateDpasslastmodtimebyId(baseEmploy.getId());
            }
            user.setPassexpired(false);
            int iDays = (int) (((new Date()).getTime() - dtLastModTime.getTime()) / (1000 * 3600 * 24));
            if (iDays > Integer.parseInt(expireDays)) {
                user.setPassexpired(true);
            }
        }

        return user;
    }

}
