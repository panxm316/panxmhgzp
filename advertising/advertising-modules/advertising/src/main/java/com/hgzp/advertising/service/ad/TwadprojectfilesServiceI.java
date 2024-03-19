package com.hgzp.advertising.service.ad;

import com.hgzp.core.model.Twadprojectfiles;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * @author songly
 * 描述：广告项目文件表 服务类
 * @date 2024/3/16 9:15
 */
public interface TwadprojectfilesServiceI extends IMyService<Twadprojectfiles> {
    /**
     * 获取
     * 方法功能:
     *
     * @param adprojectid
     * @return java.util.List<com.hgzp.core.model.Twadprojectfiles>
     * @author songly
     * @date 2024/3/16 9:51
     */
    List<Twadprojectfiles> getAdprojectfilesList(String adprojectid);

    /**
     * 根据Id获取文件信息
     * 方法功能:
     *
     * @param id
     * @return com.hgzp.core.model.Twadprojectfiles
     * @author songly
     * @date 2024/3/16 9:52
     */
    Twadprojectfiles getAdprojectfilesbyId(String id);

    /**
     * 根据Id删除文件信息
     * 方法功能:
     *
     * @param ids
     * @return void
     * @author songly
     * @date 2024/3/16 9:54
     */
    void deleteAdprojectfiles(String ids);

    /**
     * 保存项目文件信息
     * 方法功能:
     *
     * @param twadprojectfiles
     * @return void
     * @author songly
     * @date 2024/3/16 9:54
     */
    void saveAdprojectfiles(Twadprojectfiles twadprojectfiles);

    /**
     * 更新项目文件信息
     * 方法功能:
     *
     * @param twadprojectfiles
     * @return void
     * @author songly
     * @date 2024/3/16 10:14
     */
    void updateAdprojectfiles(Twadprojectfiles twadprojectfiles);

    /**
     * 删除项目的所有文件信息
     * 方法功能:
     *
     * @param adprojectid
     * @return void
     * @author songly
     * @date 2024/3/16 9:55
     */
    void deleteAdprojectfilesByAdprojectid(String adprojectid);

}
