package com.hgzp.service.system;

import com.hgzp.core.model.Tbemploy;
import com.hgzp.service.common.IMyService;

/**
 * <p>
 * 人员表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface BaseTbemployServiceI extends IMyService<Tbemploy> {


    /**
     * getTbemployByLoginName
     * 方法功能:  根据用户名获取人员信息
     * @author wangwk
     * @date 2023/8/18 14:42
     * @param account
     * @return com.hgzp.core.model.Tbemploy
     */
    Tbemploy getTbemployByLoginName(String account);

    /**
     * updatePersonSetting
     * 方法功能: 个人设置更新信息
     * @author wangwk
     * @date 2023/9/11 13:13
     * @param employ
     * @return void
     */
    void updatePersonSetting(Tbemploy employ) throws Exception;

    /**
     * updatePassWord
     * 方法功能: 个人设置修改密码
     * @author wangwk
     * @date 2023/9/11 13:14
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return void
     */
    void updatePassWord(String oldPassword, String newPassword) throws Exception;

    /**
     * updateEmployImg
     * 方法功能:  个人设置更新头像
     * @author wangwk
     * @date 2023/9/11 13:14
     * @param imgBase64
     * @return void
     */
    void updateEmployImg(String imgBase64) throws Exception;

    /**
     * 初始化人员密码的过期时间
     * 方法功能: 初始化人员密码的过期时间
     * @author wangwk
     * @date 2023/11/6 10:04
     * @param empId
     * @return void
     */
    void updateDpasslastmodtimebyId(Long empId) throws Exception;
}
