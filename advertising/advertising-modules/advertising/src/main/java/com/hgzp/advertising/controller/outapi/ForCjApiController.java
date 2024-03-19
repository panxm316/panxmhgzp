package com.hgzp.advertising.controller.outapi;

import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.hgzp.advertising.emnus.schedule.PublishStatus;
import com.hgzp.advertising.pagemodel.api.CJOrderItemDTO;
import com.hgzp.advertising.pagemodel.system.dto.EmpAuthorityDTO;
import com.hgzp.advertising.service.ad.TwadresourceapplicationServiceI;
import com.hgzp.advertising.service.ad.TworderServiceI;
import com.hgzp.advertising.service.ad.TworderitemServiceI;
import com.hgzp.advertising.service.api.TworderitemfilesServiceI;
import com.hgzp.advertising.service.customer.TwcustomerServiceI;
import com.hgzp.advertising.service.media.*;
import com.hgzp.advertising.service.schedule.OrderitemarrangeServiceI;
import com.hgzp.advertising.service.schedule.TbfoldpageplanServiceI;
import com.hgzp.advertising.service.system.*;
import com.hgzp.core.constant.CacheConstants;
import com.hgzp.core.constant.system.AdminFlagConstants;
import com.hgzp.core.model.*;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.LoginInfoVo;
import com.hgzp.core.web.BaseController;
import com.hgzp.pagemodel.api.CJAdPrinDTO;
import com.hgzp.service.RedisService;
import com.hgzp.utils.SecurityUtils;
import com.hgzp.utils.file.UfileUtil;
import com.hgzp.utils.model.LoginUser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * ForCjApiController
 * 创建人：suny
 * 类描述：分类广告相关就接口（CJ）
 * 创建日期：2024/2/22 10:50
 *
 * @folder outapi/ForCjApiController
 */
@RestController
@RequestMapping("/outapi/forcjapi")
public class ForCjApiController extends BaseController {
    /**
     * tbadengage 广告表 对应 tworder
     * tbpublishdetail 广告明细表 对应 tworderitem
     * pagetypeset 安排表 对应 tworderitemarrange
     * adClsspec adspec 规格表 对应 tbadspec (只查找分类广告的)
     * paperstyleset 版心 对应 tbpagesize
     * adSort 广告类别 对应  tbadindustry （广告行业）
     */
    @Value("${ufile.uFileURL}")
    private String uFileURL;
    @Value("${ufile.uFileURLOut}")
    private String uFileURLOut;
    @Value("${ufile.uWebURL}")
    private String uWebURL;
    @Value("${ufile.uFileDown}")
    private String uFileDown;
    @Value("${ufile.uExtURL}")
    private String uExtURL;
    @Autowired
    private TbmediaServiceI tbmediaService;
    @Autowired
    private TbfoldServiceI tbfoldService;
    @Autowired
    private TbfoldareaverServiceI tbfoldareaverService;
    @Autowired
    private TbfoldpageplanServiceI tbfoldpageplanService;
    @Autowired
    private TworderServiceI orderServiceI;
    @Autowired
    private TworderitemServiceI orderitemServiceI;
    @Autowired
    private OrderitemarrangeServiceI orderitemarrangeServiceI;
    @Autowired
    private TbpagesizeServiceI tbpagesizeServiceI;
    @Autowired
    private TbadspecServiceI tbadspecServiceI;
    @Autowired
    private TbadindustryServiceI tbadindustryServiceI;
    @Autowired
    private TworderitemfilesServiceI tworderitemfilesServiceI;
    @Autowired
    TbemployServiceI tbemployService;
    @Autowired
    RedisService redisService;

    @Autowired
    TbroleServiceI tbroleService;
    @Autowired
    TbdeptServiceI tbdeptService;
    @Autowired
    TwcustomerServiceI twcustomerService;
    @Autowired
    TwadresourceapplicationServiceI twadresourceapplicationServiceI;
    @Autowired
    TwparameterServiceI twparameterServiceI;

    /**
     * 用户登录
     * 方法功能: 用户登录
     *
     * @param vo
     * @return java.lang.String
     * @author suny
     * @date 2024/3/11 18:53
     */
    @PostMapping("/CheckUserPermission")
    public String CheckUserPermission(@RequestBody LoginVO vo) {
        // 域用户验证
//        Json json = DomainUserValidation(UserName, Pwd);
        // 普通登录
        Json json = loginCommon(vo.UserName, vo.Pwd);
        if (json.isSuccess()) {
            return "123;" + json.getObj();
        }
        return "false";
    }

    /**
     * 域验证
     * 方法功能: 域验证
     *
     * @param username
     * @param password
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2024/3/5 9:19
     */
    private Json DomainUserValidation(String username, String password) {
        String domain = "your_domain"; // 设置要连接的域名称

        Hashtable<String, Object> env = new Hashtable<>();
        env.put("com.sun.jndi.ldap.connect.timeout", "5000"); // 设置超时时间为5秒
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://" + domain); // 设置LDAP服务器地址

        DirContext ctx = null;
        Json json = new Json();
        try {
            ctx = new InitialDirContext(env);

            // 进行身份验证
//            String username = "your_username"; // 输入要验证的用户名
//            String password = "your_password"; // 输入要验证的密码
            String searchBase = "dc=example,dc=" + domain; // 根据需求修改查询基本路径
            String filter = "(sAMAccountName={0})"; // 根据需求修改过滤条件

            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            NamingEnumeration<?> results = ctx.search(searchBase, MessageFormat.format(filter, username), controls);

            if (results != null && results.hasMore()) {
                Tbemploy tbemploy = tbemployService.getTbemployByLoginName(username);
                System.out.println("认证成功！");

                if (tbemploy == null) {
                    return Json.fail("用户名或密码错误");
                }
                if (!tbemploy.getBinner()) {
                    return Json.fail("外部人员禁止登录");
                }
                if (tbemploy.getBadminflag() == AdminFlagConstants.ADMIN) {
                    return Json.fail("管理员禁止登录");
                }

                String accessToken = getToken(tbemploy);
                if (StringUtils.hasText(accessToken)) {
                    return Json.success("登录成功", accessToken);
                } else {
                    return Json.fail("登录失败");
                }
                // 在这里添加其他操作或业务逻辑
            } else {
                System.out.println("认证失败！");
                json.setSuccess(false);
            }
        } catch (NamingException e) {
            System.err.println("发生错误：" + e.getMessage());
            json.setSuccess(false);
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception e) {

                }
            }
            return json;
        }
    }

    /**
     * 普通登录验证
     * 方法功能: 普通登录验证
     *
     * @param account
     * @param password
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2024/3/5 12:52
     */
    private Json loginCommon(String account, String password) {

        Tbemploy tbemploy = tbemployService.getTbemployByLoginName(account);
        if (tbemploy == null) {
            return Json.fail("用户名或密码错误");
        }
        if (!tbemploy.getBinner()) {
            return Json.fail("外部人员禁止登录");
        }
        if (tbemploy.getBadminflag() == AdminFlagConstants.ADMIN) {
            return Json.fail("管理员禁止登录");
        }
        String userpassword = tbemploy.getSpassword();
        if (!SecurityUtils.matchesPassword(password, userpassword)) {
            return Json.fail("用户名或密码错误");
        }

        String accessToken = getToken(tbemploy);
        if (StringUtils.hasText(accessToken)) {
            return Json.success("登录成功", accessToken);
        } else {
            return Json.fail("登录失败");
        }
    }

    /**
     * 生成token并将用户信息存储
     * 方法功能: 生成token并将用户信息存储
     *
     * @param tbemploy
     * @return java.lang.String
     * @author suny
     * @date 2024/3/5 12:52
     */
    private String getToken(Tbemploy tbemploy) {
        LoginInfoVo loginInfoVo = new LoginInfoVo();
        try {
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
            redisService.setCacheObject(CacheConstants.PC_LOGIN_TOKEN_KEY + user.getLoginname(), user,
                    CacheConstants.EXPIRATION, TimeUnit.MINUTES);

            //人员类型权限
            EmpAuthorityDTO empAuthorityDTO = tbroleService.getEmpAuthIdsByEmpId(tbemploy.getId());
            redisService.setCacheObject(StrUtil.format(CacheConstants.EMP_AUTH_ID_LIST, tbemploy.getId()),
                    empAuthorityDTO, CacheConstants.EXPIRATION, TimeUnit.MINUTES);

            user.setAccessToken(accessToken);
            loginInfoVo.setLoginuser(user);
            return accessToken;
        } catch (Exception e) {
            return "";
        }
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
        user.setBsalesman(baseEmploy.getBsalesman() == null ? false : baseEmploy.getBsalesman());
        user.setBlead(baseEmploy.getBlead() == null ? false : baseEmploy.getBlead());
        user.setSimg(StrUtil.isNotBlank(baseEmploy.getSimg()) ? baseEmploy.getSimg() : "");
        // 依据bleader，取得管理的部门
        user.setAuthedDeptIds(tbemployService.getAuthedDeptIdsByLeader(baseEmploy));


        return user;
    }

    /**
     * 菜单权限
     * 方法功能:// 录入器录入	=1	// 录入器预排	=2   // 分类排版		=3
     * （默认返回123）
     *
     * @return java.lang.String
     * @author suny
     * @date 2024/3/6 16:09
     */
    @PostMapping("/GetRightsArrange")
    public String GetRightsArrange() {
        return "123";
    }

    /**
     * 获取广告报纸xml
     * 方法功能: 获取广告报纸xml
     *
     * @return java.lang.String
     * @author suny
     * @date 2024/3/6 16:08
     */
    @PostMapping("/GetAdPaperName")
    public String GetAdPaperName() {
        List<DataCombo> mediaDataCombo = tbmediaService.getMediaDataComboByType("paper");
        JSONArray ja = new JSONArray();
        for (DataCombo dataCombo : mediaDataCombo) {
            JSONObject jo = new JSONObject();
            jo.put("paperCname", dataCombo.getName());
            jo.put("paperEname", dataCombo.getName());
            ja.put(jo);
        }
        String xmlStr = XML.toString(ja, "Table");
        return getXmlStr(xmlStr);
    }

    /**
     * 获取报纸的叠次内容
     * 方法功能: 获取报纸的叠次内容
     *
     * @return java.lang.String
     * @author suny
     * @date 2024/3/6 16:08
     */
    @PostMapping("/GetPaperFold")
    public String GetPaperFold() {
        List<Tbfold> tbfoldList = tbfoldService.getFoldListByMediaTypeForCJ("paper");
        JSONArray ja = new JSONArray();
        for (Tbfold tbfold : tbfoldList) {
            JSONObject jo = new JSONObject();
            jo.put("foldCode", tbfold.getId());
            jo.put("foldName", tbfold.getSname());
            ja.put(jo);
        }
        String xmlStr = XML.toString(ja, "Table");
        return getXmlStr(xmlStr);
    }

    /**
     * 获取指定叠次的叠次地域版次内容
     * 方法功能: 获取指定叠次的叠次地域版次内容
     *
     * @param vo
     * @return java.lang.String
     * @author suny
     * @date 2024/3/11 19:54
     */
    @PostMapping("/GetFoldVer")
    public String GetFoldVer(@RequestBody GetFoldVerVO vo) {
        List<Tbfoldareaver> tbfoldareaverList = tbfoldareaverService.getFoldAreaverListByFoldIdForCJ(vo.FoldId);
        JSONArray ja = new JSONArray();
        for (Tbfoldareaver tbfoldareaver : tbfoldareaverList) {
            JSONObject jo = new JSONObject();
            jo.put("foldVerCode", tbfoldareaver.getId());
            jo.put("areaName", tbfoldareaver.getSname());
            ja.put(jo);
        }
        String xmlStr = XML.toString(ja, "Table");
        return getXmlStr(xmlStr);
    }

    /**
     * 获取指定叠次的版数内容, 数据来自版面计划表
     * 方法功能:  获取指定叠次的版数内容, 数据来自版面计划表
     *
     * @param vo
     * @return java.lang.String
     * @author suny
     * @date 2024/3/11 19:54
     */
    @PostMapping("/GetFoldPages")
    public String GetFoldPages(@RequestBody FoldPagesVO vo) {
        String FoldId = vo.FoldId;
        String foldver = vo.foldver;
        String XDate = vo.XDate;
        XDate = formatDate(XDate);
        List<Tbfoldpageplan> tbfoldpageplanList = tbfoldpageplanService.getFoldPagePlaneListByFoldIdForCJ(FoldId, foldver, XDate);
        JSONArray ja = new JSONArray();
        for (Tbfoldpageplan tbfoldpageplan : tbfoldpageplanList) {
            JSONObject jo = new JSONObject();
            jo.put("pagenum", tbfoldpageplan.getPagenum());
            ja.put(jo);
        }
        String xmlStr = XML.toString(ja, "Table");
        return getXmlStr(xmlStr);
    }

    /**
     * 获取树结构的广告类别
     * 方法功能: 获取树结构的广告类别
     *
     * @param
     * @return java.lang.String
     * @author suny
     * @date 2024/3/11 19:54
     */
    @PostMapping("/GetAdSortTree")
    public String GetAdSortTree() {
        List<Tbadindustry> tbadindustryList = tbadindustryServiceI.getTbAdIndustryListForCJ();
        JSONArray ja = new JSONArray();
        for (Tbadindustry tbadindustry : tbadindustryList) {
            JSONObject jo = new JSONObject();
            jo.put("sortCode", tbadindustry.getId());
            jo.put("sortName", tbadindustry.getSname());
            jo.put("localId", tbadindustry.getId());
            jo.put("parentid", tbadindustry.getParentid());
            jo.put("sortDesc", tbadindustry.getSdesc());
            jo.put("filepath", tbadindustry.getSfilepath());
            jo.put("filename", tbadindustry.getSfilename());
            jo.put("picturepath", tbadindustry.getPicturepath());
            ja.put(jo);
        }
        String xmlStr = XML.toString(ja, "Table");
        return getXmlStr(xmlStr);
    }

    /**
     * 保存分类广告类头的设计
     * 方法功能:   保存分类广告类头的设计
     *
     * @param vo
     * @return java.lang.String
     * @author suny
     * @date 2024/3/11 19:54
     */
    @PostMapping("/SaveSortAdSet")
    public String SaveSortAdSet(@RequestBody SaveSortAdSetVO vo) {
        String id = vo.id;
        String path = vo.path;
        String filename = vo.filename;
        Json json = tbadindustryServiceI.SaveSortAdSetForCJ(id, path, filename);
        return json.isSuccess() ? "true" : "false";
    }

    /**
     * 保存广告排版信息
     * 方法功能: 保存广告排版信息，修改最后结果标志pagestatus='安排'      where = publstatus=’确定’  status=’有效’    pagestatus=’订制’
     *
     * @param vo
     * @return java.lang.String
     * @author suny
     * @date 2024/3/11 19:54
     */
    @PostMapping("/SaveAdTypeSet")
    public String SaveAdTypeSet(@RequestBody SaveAdTypeSetVO vo) {
        String id = vo.id;
        String pubDate = vo.pubDate;
        String pubStatus = vo.pubStatus;
        String issueDate = vo.issueDate;
        String issueFolder = vo.issueFolder;
        String foldVersion = vo.foldVersion;
        String issuePage = vo.issuePage;
        pubDate = formatDate(pubDate);
        issueDate = formatDate(issueDate);

        List<String> ids = Arrays.asList(id.split(";"));
        List<String> pubDates = Arrays.asList(pubDate.split(";"));
        for(int i = 0; i < ids.size(); i++){
            Json json = orderitemarrangeServiceI.updateItemArrangeByCJ(ids.get(i), pubDates.get(i), pubStatus, issueDate, issueFolder, foldVersion, issuePage);
            if(!json.isSuccess()){
                return "false";
            }
        }
//        Json json = orderitemarrangeServiceI.updateItemArrangeByCJ(id, pubDate, pubStatus, issueDate, issueFolder, foldVersion, issuePage);
        return "true";
    }

    /**
     * 删除广告排版信息
     * 方法功能: 删除广告排版信息
     *
     * @param vo
     * @return java.lang.String
     * @author suny
     * @date 2024/3/11 19:53
     */
    @PostMapping("/DelAdTypeSet")
    public String DelAdTypeSet(@RequestBody DelAdTypeSetVO vo) {
        String inforid = vo.inforid;
        String pubDate = vo.pubDate;
        pubDate = formatDate(pubDate);
        Json json = orderitemarrangeServiceI.DelAdTypeSetForCJ(inforid, pubDate);
        return json.isSuccess() ? "true" : "false";
    }

    /**
     * 录入器预排上载保存
     * 方法功能: 录入器预排上载保存
     *
     * @param vo
     * @return java.lang.String
     * @author suny
     * @date 2024/3/11 19:53
     */
    @PostMapping("/SaveAdTypeSetPrepare")
    public String SaveAdTypeSetPrepare(@RequestBody SaveAdTypeSetPrepareVO vo) {

        String id = vo.id;
        String path = vo.path;
        String filename = vo.filename;
        String width = vo.width;
        String height = vo.height;

        Json json = orderitemServiceI.updateItemByCJ(id, path, filename, width, height);
        return json.isSuccess() ? "true" : "false";
    }

    /**
     * 获取树结构的广告类别及类别下广告内容
     * 方法功能: 获取树结构的广告类别及类别下广告内容
     *
     * @param vo
     * @return java.lang.String
     * @author suny
     * @date 2024/3/11 19:53
     */
    @PostMapping("/GetAdTypeSetTree")
    public String GetAdTypeSetTree(@RequestBody AdTypeSetTreeVO vo) {
        String typeFlag = vo.typeFlag;
        String XDate = vo.XDate;
        String FoldId = vo.FoldId;
        String FoldVer = vo.FoldVer;
        String FoldPage = vo.FoldPage;
        String EndDate = vo.EndDate;
        XDate = formatDate(XDate);
        EndDate = formatDate(EndDate);

        //时间段,使用预定时间的，update 2005－09－21 engagetime改为publdate，使用见报日期
        Tworderitem tworderitem = new Tworderitem();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            tworderitem.setPrestartdate(sdf.parse(XDate));
            tworderitem.setPreenddate(sdf.parse(EndDate));
        } catch (Exception e) {
//            e.printStackTrace();
        }
        tworderitem.setFoldid(Long.valueOf(FoldId));
        tworderitem.setFoldareaverid(Long.valueOf(FoldVer));
        List<CJOrderItemDTO> orderItemForCJ = orderitemServiceI.getOrderItemForCJ(tworderitem);
        JSONArray ja = new JSONArray();
        for (CJOrderItemDTO cjOrderItemDTO : orderItemForCJ) {
            JSONObject jo = new JSONObject();
            Arrays.stream(CJOrderItemDTO.class.getDeclaredFields()).map(field -> {
                try {
                    field.setAccessible(true); // 使字段可访问
                    jo.put(field.getName(), field.get(cjOrderItemDTO));
                    return jo;
                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
                }
                return null;
            }).forEach(o -> {
//                System.out.println(o);
            });
            ja.put(jo);
        }
        String xmlStr = XML.toString(ja, "Table");
        return getXmlStr(xmlStr);
    }

    /**
     * 根据id获取广告信息
     * 方法功能:  根据id获取广告信息
     *
     * @param vo
     * @return java.lang.String
     * @author suny
     * @date 2024/3/11 19:53
     */
    @PostMapping("/GetAdFromID")
    public String GetAdFromID(@RequestBody AdFromIDVO vo) {
        String ID = vo.ID;
        String sDate = vo.sDate;
        sDate = formatDate(sDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Tworderitem tworderitem = new Tworderitem();
        tworderitem.setSordernum(ID);
        try {
            tworderitem.setCreatetime(sdf.parse(sDate));
        } catch (Exception e) {
//            e.printStackTrace();
        }
        List<CJOrderItemDTO> orderItemForCJ = orderitemServiceI.getOrderItemForCJ(tworderitem);
        JSONArray ja = new JSONArray();
        for (CJOrderItemDTO cjOrderItemDTO : orderItemForCJ) {
            JSONObject jo = new JSONObject();
            Arrays.stream(CJOrderItemDTO.class.getDeclaredFields()).map(field -> {
                try {
                    field.setAccessible(true); // 使字段可访问
                    jo.put(field.getName(), field.get(cjOrderItemDTO));
                    return jo;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return null;
            }).forEach(o -> {
//                System.out.println(o);
            });
            ja.put(jo);
        }
        String xmlStr = XML.toString(ja, "Table");
        return getXmlStr(xmlStr);
    }

    /**
     * 获取广告信息
     * 方法功能: 获取广告信息
     *
     * @param vo
     * @return java.lang.String
     * @author suny
     * @date 2024/3/11 19:53
     */
    @PostMapping("/GetSingleAdFromID")
    public String GetSingleAdFromID(@RequestBody SingleAdFromIDVO vo) {
        String ID = vo.ID;

        List<Tworder> orderList = orderServiceI.getOrdersBySordernumForCJ(ID);
        if (orderList.size() == 0) {
            return "";
        }
        List<Twadresourcefiles> adresourcefiles = twadresourceapplicationServiceI.getResourceFilesByOrderIdForCJ(orderList.get(0).getId().toString());
        List<String> picturepath = adresourcefiles.stream().map(twadresourcefiles -> {
            return UfileUtil.getStaticUrl(twadresourcefiles.getSfileid(), twadresourcefiles.getSfileformat());
        }).collect(Collectors.toList());
        JSONObject jo = new JSONObject();
        jo.put("adcontent", orderList.get(0).getSadcontent());
        jo.put("adtitle", orderList.get(0).getSadtitle());
        jo.put("picturepath", String.join(";", picturepath));  //----------------------------资源文件地址

        String xmlStr = XML.toString(jo, "Table");
        System.out.println(xmlStr);
        return getXmlStr(xmlStr);
    }

    /**
     * 获取版心列表
     * 方法功能:获取版心列表
     *
     * @param
     * @return java.lang.String
     * @author suny
     * @date 2024/3/11 19:53
     */
    @PostMapping("/getfont")
    public String getfont() {
        List<Tbpagesize> pageSizeList = tbpagesizeServiceI.getPageSizeList();
        JSONArray ja = new JSONArray();
        for (Tbpagesize tbpagesize : pageSizeList) {
            JSONObject jo = new JSONObject();
            jo.put("colwidth", tbpagesize.getIcolwidth());
            jo.put("fontsize", tbpagesize.getNfontsize());
            ja.put(jo);
        }
        String xmlStr = XML.toString(ja, "Table");
        return getXmlStr(xmlStr);
    }

    /**
     * 获取广告文件地址
     * 方法功能: 获取广告文件地址
     *
     * @param vo
     * @return java.lang.String
     * @author suny
     * @date 2024/3/11 19:52
     */
    @PostMapping("/getpath")
    public String getpath(@RequestBody GetPathVO vo) {
        String strdate = vo.strdate;
        String strid = vo.strid;
        strdate = formatDate(strdate);
        //
        List<Tworderitem> tworderitemList = orderitemServiceI.getFilePathBySordernum(strid, strdate);
        if (tworderitemList.size() == 0) {
            return "";
        }
        JSONObject jo = new JSONObject();
        jo.put("path", tworderitemList.get(0).getSfilename());
        String xmlStr = XML.toString(jo, "Table");
        return getXmlStr(xmlStr);
    }

    /**
     * 获取广告规格
     * 方法功能: 获取广告规格(只取分类广告的规格)
     *
     * @param
     * @return java.lang.String
     * @author suny
     * @date 2024/3/11 19:52
     */
    @PostMapping("/GetSpec")
    public String GetSpec() {
        List<Tbadspec> adspecList = tbadspecServiceI.getAdspecListForCJ();
        JSONArray ja = new JSONArray();
        for (Tbadspec tbadspec : adspecList) {
            JSONObject jo = new JSONObject();
            jo.put("specName", tbadspec.getSname());
            jo.put("width", tbadspec.getNwidth());
            jo.put("height", tbadspec.getNheight());
            ja.put(jo);
        }
        String xmlStr = XML.toString(ja, "Table");
        return getXmlStr(xmlStr);
    }

    /**
     * 获取统一文件地址
     * 方法功能: 获取统一文件地址
     * uFileURL: 内网上传地址
     * uFileURLOut: 外网上传地址
     * uWebURL: 文件预览地址
     * uFileDown: 文件下载地址
     * uExtURL: 文件转换地址
     *
     * @param
     * @return java.lang.String
     * @author suny
     * @date 2024/3/12 8:58
     */
    @PostMapping("/GetFilePathinfo")
    public String GetFilePathinfo() {
        JSONObject jo = new JSONObject();
        jo.put("uFileURL", uFileURL); // 内网上传地址
        jo.put("uFileURLOut", uFileURLOut); // 外网上传地址
        jo.put("uWebURL", uWebURL); // 文件预览地址
        jo.put("uFileDown", uFileDown); // 文件下载地址
        jo.put("uExtURL", uExtURL); // 文件转换地址
        String xmlStr = XML.toString(jo, "Table");
        return getXmlStr(xmlStr);
    }

    /**
     * 按合同号取得广告信息
     * 方法功能: 按合同号取得广告信息
     *
     * @param vo
     * @return java.lang.String
     * @author suny
     * @date 2024/3/11 19:52
     */
    @PostMapping("/GetAdTypeSetTreeByID")
    public String GetAdTypeSetTreeByID(@RequestBody AdTypeSetTreeByIDVO vo) {
        String compactid = vo.compactid;
        //String compactid, String UserName
        Tworderitem tworderitem = new Tworderitem();
        tworderitem.setScontractnum(compactid);
        List<CJOrderItemDTO> orderItemForCJ = orderitemServiceI.getOrderItemForCJ(tworderitem);
        JSONArray ja = new JSONArray();
        for (CJOrderItemDTO cjOrderItemDTO : orderItemForCJ) {
            JSONObject jo = new JSONObject();
            Arrays.stream(CJOrderItemDTO.class.getDeclaredFields()).map(field -> {
                try {
                    field.setAccessible(true); // 使字段可访问
                    jo.put(field.getName(), field.get(cjOrderItemDTO));
                    return jo;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return null;
            }).forEach(o -> {
//                System.out.println(o);
            });
            ja.put(jo);
        }
        String xmlStr = XML.toString(ja, "Table");
        return getXmlStr(xmlStr);
    }

    /**
     * 获取打印值
     * 方法功能: 获取打印值
     * //客户名称，广告类别（小类），计价方式，单价，次数，总价
     * // 图片价格，标题价格，字，行，则，格等价格
     *
     * @param vo
     * @return java.lang.String
     * @author suny
     * @date 2024/3/11 19:52
     */
    @PostMapping("/GetPrintValue")
    public String GetPrintValue(@RequestBody PrintValueVO vo) {
        String inforId = vo.inforId;
        String strdate = vo.strdate;
        strdate = formatDate(strdate);

        List<Tworder> tworderList = orderServiceI.getOrdersBySordernumForCJ(inforId);
        if (tworderList == null || tworderList.size() == 0) {
            return Json.fail("没有找到订单信息").toString();
        }
        List<Tworderitem> tworderitemList = orderitemServiceI.getListByOrderIdForCJ(tworderList.get(0).getId().toString());
        if (tworderitemList == null || tworderitemList.size() == 0) {
            return Json.fail("没有找到订单明细信息").toString();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String finalStrdate = strdate;
        List<Tworderitem> tworderitemList1 = tworderitemList.stream().filter(item -> {
            Date prestartdate = item.getPrestartdate();
            String d = sdf.format(prestartdate);
            return d.equals(finalStrdate);
        }).collect(Collectors.toList());
        if (tworderitemList1.size() == 0) {
            return Json.fail("没有找到订单明细信息").toString();
        }
        // 明细表： 客户名称、广告类别、单价,计价方式
        Tworderitem tworderitem = tworderitemList1.get(0);
        if (tworderitem == null) {
            return Json.fail("没有找到订单明细信息").toString();
        }
        Tworder tworder = orderServiceI.getById(tworderitem.getOrderid());
        JSONObject jo = new JSONObject();
        jo.put("advertiser", tworder.getCustomername()); // 客户名称
        jo.put("kinddetail", tworder.getAdindustryname()); // 广告行业
        jo.put("adprice", tworderitem.getAmountreceivable());  // 应收金额
        jo.put("compactid", tworderitem.getScontractnum()); // 合同号
        jo.put("agent", StringUtils.hasText(tworder.getAgencyname()) ? tworder.getAgencyname() : StringUtils.hasText(tworder.getAgentname()) ? tworder.getAgentname(): "无"); // 代理名称
        jo.put("width", tworderitem.getNwidth()); // 广告宽度
        jo.put("height", tworderitem.getNheight()); // 广告高度
        jo.put("unitnum", tworderitem.getAdspecname()); // 广告规格
        jo.put("pricetype", "按格"); // ---------------------------广告类型：按格、按字、按行，现在都是按格

        jo.put("pubcount", tworderitemList.size()); // +++++++++++根据合同号取明细总数（审批通过）
        List<Twadresourcefiles> adresourcefiles = twadresourceapplicationServiceI.getResourceFilesByOrderIdForCJ(tworder.getId().toString());
        // 根据是否有资源，有资源是1++++++++++++++++
        jo.put("ppflg", adresourcefiles.size()); // ------------------------价格计算标志/是否上传图片070114牟加
        Twcustomer twcustomer = twcustomerService.getById(tworder.getCustomerid());
        jo.put("aduseraddr", tworder.getSaddress()); // 客户地址
        jo.put("linkmantel", tworder.getSmobilephone()); // 客户电话
        jo.put("adlinkman", tworder.getScontacts()); // 客户联系人
        BigDecimal totalprice = new BigDecimal(0);
        for (Tworderitem item : tworderitemList) {
            totalprice = totalprice.add(item.getAmountreceivable());
        }
        jo.put("totalprice", totalprice); // 总价
        // 参数表获取
        jo.put("titleprice", twparameterServiceI.getParameterByKey("titleprice")); // ------------------------标题价格
        jo.put("picprice", twparameterServiceI.getParameterByKey("picprice")); // ------------------------图片价格
        jo.put("titlelinewords", twparameterServiceI.getParameterByKey("titlelinewords")); // -------------------------标题行字数
        jo.put("textlinewords", twparameterServiceI.getParameterByKey("textlinewords")); // -------------------------正文行字数

        jo.put("unitprice", 0); // ------------------------单价
        jo.put("titlewords", tworderitem.getSadtitle().length()); // ---------标题字数
        jo.put("titlelines", 1); // ------------标题行数
        jo.put("textwords", tworderitem.getSadcontent().length()); // ------------正文字数
        jo.put("adPath", tworderitem.getSfilename()); // ------------------------广告文件地址

        String xmlStr = XML.toString(jo, "Table");
        return getXmlStr(xmlStr);
    }

    /**
     * 添加打印排版表
     * 方法功能: 添加打印排版表
     *
     * @param vo
     * @return java.lang.String
     * @author suny
     * @date 2024/3/11 19:52
     */
    @PostMapping("/GetPrintTypeSet")
    public String GetPrintTypeSet(@RequestBody GetPrintTypeSetVO vo) {
        String XDate = vo.XDate;
        String EndDate = vo.EndDate;
        String FoldId = vo.FoldId;
        String FoldVer = vo.FoldVer;
        String FoldPage = vo.FoldPage;
        XDate = formatDate(XDate);
        EndDate = formatDate(EndDate);
        List<CJAdPrinDTO> orderAndItemListForCJ = orderServiceI.getCJAdPrinListForCJ(XDate, EndDate, FoldId, FoldVer, FoldPage);
        JSONArray ja = new JSONArray();
        for (CJAdPrinDTO cjAdPrinDTO : orderAndItemListForCJ) {
            JSONObject jo = new JSONObject();
            Arrays.stream(CJAdPrinDTO.class.getDeclaredFields()).map(field -> {
                try {
                    field.setAccessible(true); // 使字段可访问
                    jo.put(field.getName(), field.get(cjAdPrinDTO));
                    return jo;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return null;
            }).forEach(o -> {
//                System.out.println(o);
            });
            ja.put(jo);
        }
        String xmlStr = XML.toString(ja, "Table");
        return getXmlStr(xmlStr);
    }

    /**
     * 获取打印表
     * 方法功能:获取打印表
     *
     * @param vo
     * @return java.lang.String
     * @author suny
     * @date 2024/3/11 19:51
     */
    @PostMapping("/GetPrintTypeTable")
    public String GetPrintTypeTable(@RequestBody getPrintTypeTableVO vo) {
        String inforIds = vo.inforIds;
        String[] inforIdArr = inforIds.split(",");
        JSONArray ja = new JSONArray();
        for (String inforId : inforIdArr) {
            List<Tworder> tworderList = orderServiceI.getOrdersBySordernumForCJ(inforId);
            if (tworderList.size() == 0) {
                continue;
            }
            Tworder tworder = tworderList.get(0);
            List<Tworderitem> tworderitemList = orderitemServiceI.getListByOrderIdForCJ(tworder.getId().toString());
            if (tworderitemList.size() == 0) {
                continue;
            }
            Tworderitem tworderitem = tworderitemList.get(0);
            if (tworderitem == null) {
                continue;
            }
            JSONObject jo = new JSONObject();
            // 将Date时间格式化为字符串yyyy-MM-dd
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String engagetime = sdf.format(tworder.getCreatetime());
            String publdate = sdf.format(tworderitem.getPrestartdate());

            jo.put("custname", tworder.getCustomername()); // 客户名称
            jo.put("adkind", tworder.getAdindustryname()); // 广告行业
            jo.put("custid", tworder.getCustomerid()); // 客户id
            jo.put("publdate", publdate); // 刊发时间

            jo.put("pubrequest", tworderitem.getIpublishstatus() == PublishStatus.PUBLISH_Arrange.key ? "编辑安排" : "指定日期版面"); // 根据元广告系统apflag判断，本系统没有，默认为 编辑安排
            jo.put("filedate", tworderitem.getCreatetime()); // 录入日期


            jo.put("remark", tworderitem.getSremark());//------------------// 合同表：备注

            int foretimes = tworderitemList.size();
            int arrtimes = tworderitemList.stream().map(m -> m.getIpublishstatus()).filter(f -> f.equals(PublishStatus.getKeyByName("安排"))).collect(Collectors.toList()).size();
            int pubtimes = tworderitemList.stream().map(m -> m.getIpublishstatus()).filter(f -> f.equals(PublishStatus.getKeyByName("已发布"))).collect(Collectors.toList()).size();

            jo.put("foretimes", foretimes);//------------------明细表：预定次数
            jo.put("arrtimes", arrtimes);//------------------明细表：见报次数
            jo.put("pubtimes", pubtimes);// ------------------明细表：安排次数,,安排次数等于 安排次数加见报次数


            ja.put(jo);
        }
        String xmlStr = XML.toString(ja, "Table");
        return getXmlStr(xmlStr);
    }

    /**
     * 将字符串转换为xml格式
     * 方法功能:
     *
     * @param xmlStr
     * @return java.lang.String
     * @author suny
     * @date 2024/2/23 10:07
     */
    private String getXmlStr(String xmlStr) {
        xmlStr = "  <?xml version=\"1.0\" encoding=\"utf-8\" ?> \n" +
                "  <String xmlns=\"http://hg.com/webservices/\"><NewDataSet>" + xmlStr + "</NewDataSet></String> ";
        return xmlStr;
    }

    /**
     * 将日期格式化
     * 方法功能: 将日期格式化
     *
     * @param XDate
     * @return java.lang.String
     * @author suny
     * @date 2024/3/12 14:03
     */
    private String formatDate(String XDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date d = sdf.parse(XDate);
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            XDate = sdf.format(d);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return XDate;
    }
}

class LoginVO {
    public String UserName;
    public String Pwd;
}

class GetFoldVerVO {
    public String FoldId;
}

class FoldPagesVO {
    public String FoldId;
    public String foldver;
    public String XDate;
}

class AdSortTreeVO {
    public String xType;
}

class SaveSortAdSetVO {
    public String id;
    public String path;
    public String filename;
}

class SaveAdTypeSetVO {
    /**
     * 广告明细id
     */
    public String id;
    /**
     * 发布日期
     */
    public String pubDate;
    /**
     * 发布状态
     */
    public String pubStatus;
    /**
     * 刊报日期
     */
    public String issueDate;
    /**
     * 刊报叠次
     */
    public String issueFolder;
    /**
     * 刊报叠次地域版本
     */
    public String foldVersion;
    /**
     * 刊报版号
     */
    public String issuePage;
}

class SaveAdTypeSetPrepareVO {
    /**
     * 订单编号
     */
    public String id;
    public String path;
    public String filename;
    public String width;
    public String height;
}

class DelAdTypeSetVO {
    /**
     * 订单编号
     */
    public String inforid;
    public String pubDate;
}

class AdTypeSetTreeVO {
    /**
     * 使用广告的分类（面积为1，分类为0）
     */
    public String typeFlag;
    /**
     * 日期
     */
    public String XDate;
    /**
     * 叠次
     */
    public String FoldId;
    /**
     * 叠次地域版本
     */
    public String FoldVer;
    /**
     * 叠次版号
     */
    public String FoldPage;
    /**
     * 结束日期
     */
    public String EndDate;
}

class AdFromIDVO {
    /**
     * 订单编号
     */
    public String ID;
    public String sDate;
}

class SingleAdFromIDVO {
    /**
     * 订单编号
     */
    public String ID;
}

class GetPathVO {
    public String strdate;
    /**
     * 合同号
     */
    public String strid;
}

class AdTypeSetTreeByIDVO {
    /**
     * 合同号
     */
    public String compactid;
}

class PrintValueVO {
    /**
     * 订单编号
     */
    public String inforId;
    /**
     * 日期
     */
    public String strdate;
}

class GetPrintTypeSetVO {
    /**
     * 使用广告的分类（面积为1，分类为0）
     */
    public String typeFlag;
    /**
     * 日期
     */
    public String XDate;
    /**
     * 叠次
     */
    public String FoldId;
    /**
     * 叠次地域版本
     */
    public String FoldVer;
    /**
     * 版面类别
     */
    public String FoldPage;
    /**
     * 结束日期
     */
    public String EndDate;
}

class getPrintTypeTableVO {
    /**
     * 订单编号
     */
    public String inforIds;
}

