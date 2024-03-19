package com.hgzp.core.page;

import com.hgzp.utils.model.LoginUser;
import lombok.Data;

import java.util.List;

/**
 * UserInfoVo
 * 创建人：peij
 * 类描述：登录用户返回的信息
 * 创建日期：2023/8/19 9:49
 */
@Data
public class LoginInfoVo {
    /**
     * 版本号
     */
    private String sysversion;
    /**
     * 权限字符串（客户端）
     */
    private List<String> permissions;
    /**
     * 验证超期
     */
    private boolean passexpired;
    /**
     * 用户信息
     */
    private LoginUser loginuser;
    /**
     * office预览地址
     */
    private String officePreviewUrl;
}
