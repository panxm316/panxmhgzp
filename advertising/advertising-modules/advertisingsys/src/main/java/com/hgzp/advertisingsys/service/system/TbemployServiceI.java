package com.hgzp.advertisingsys.service.system;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.pagemodel.system.dto.EmployDTO;
import com.hgzp.advertisingsys.pagemodel.system.vo.EmployVO;
import com.hgzp.advertisingsys.pagemodel.system.vo.OrgTreeVo;
import com.hgzp.core.model.Tbemploy;
import com.hgzp.core.page.Json;
import com.hgzp.service.system.BaseTbemployServiceI;

import java.util.List;

/**
 * <p>
 * 人员表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbemployServiceI extends BaseTbemployServiceI {


    /**
     * getEmployPageList
     * 方法功能:  人员管理的人员列表
     * @author wangwk
     * @date 2023/8/21 15:50
     * @param page  分页
     * @param query  查询条件
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.hgzp.core.model.Tbemploy>
     */
    IPage<EmployVO> getEmployPageList(Page<Tbemploy> page, EmployVO query) throws Exception;


    /**
     * getEmployInfoById
     * 方法功能: 根据id查询人员详情
     * @author wangwk
     * @date 2023/9/15 9:21
     * @param id 人员id
     * @return com.hgzp.advertising.pagemodel.system.vo.EmployVO
     */
    EmployVO getEmployInfoById(Long id);

    /**
     * saveEmploy
     * 方法功能: 保存人员
     * @author wangwk
     * @date 2023/8/21 17:24
     * @param employ
     * @return void
     */
    void saveEmploy(EmployDTO employ) throws Exception;


    /**
     * updateEmploy
     * 方法功能: 修改人员
     * @author wangwk
     * @date 2023/8/22 17:21
     * @param employ
     * @return void
     */
    void updateEmploy(EmployDTO employ) throws Exception;



    /**
     * deleteEmp
     * 方法功能:
     * @author wangwk
     * @date 2023/8/23 11:04
     * @param id
     * @return void
     */
    void deleteEmp(Long id) throws Exception;


    /**
     * disableEmps
     * 方法功能: 批量 禁用/启用  人员
     * @author wangwk
     * @date 2023/8/23 14:09
     * @param ids 人员id，多个用英文逗号分隔
     * @param buse 是否启用
     * @return void
     */
    void disableEmps(String ids, boolean buse);

    /**
     * countEmpByRoleId
     * 方法功能: 通过角色id 判断是否还有人员与角色相关联
     * @author peij
     * @date 2023/8/31 9:47
     * @param roleId
     * @return int
     */
    Long countEmpByRoleId(String roleId);

    /**
     * updateBatchEmpRoles
     * 方法功能:  批量设置人员角色
     * @author wangwk
     * @date 2023/9/2 10:10
     * @param empIds  人员id，多个用逗号分隔
     * @param roleIds  角色id，多个用逗号分隔
     * @return void
     */
    void updateBatchEmpRoles(String empIds, String roleIds) throws Exception;
    /**
     * getEmployInfo
     * 方法功能: 获取当前人员信息，包括部门和角色名称
     * @author suny
     * @date 2023/9/12 15:36
     * @param
     * @return com.hgzp.advertising.pagemodel.system.vo.EmployVO
     */
    EmployVO getEmployInfo() throws Exception;

    /**
     * getEmployPageListByRoleId
     * 方法功能: 根据角色id获取人员列表
     * @author CGD
     * @date 2023/9/15 17:02
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.hgzp.advertising.pagemodel.system.vo.EmployVO>
     */
    public Page<EmployVO> getEmployPageListByRoleId(Page<Tbemploy> page, EmployVO query) throws Exception;

    /**
     * getEmployPageListNotByRoleId
     * 方法功能:  获取不包含角色人员的人员列表
     * @author CGD
     * @date 2023/9/15 17:00
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.hgzp.advertising.pagemodel.system.vo.EmployVO>
     */
    public Page<EmployVO> getEmployPageListNotByRoleId(Page<Tbemploy> page, EmployVO query) throws Exception;


    /**
     * saveEmployByRole
     * 方法功能:  给角色新增加人员
     * @author CGD
     * @date 2023/9/16 13:52
     * @param roleId
     * @param empIds
     * @return void
     */
    public void saveEmployByRole(Long roleId, String empIds) throws Exception;

    /**
     * deleteRoleById
     * 方法功能:  根据角色id重置角色上的人员关联
     * @author CGD
     * @date 2023/9/18 11:28
     * @param roleId
     * @return void
     */
    public void deleteRoleById(Long roleId) throws Exception;


    /**
     * deleteRoleByEmploy
     * 方法功能: 删除角色上的某些人员
     * @author CGD
     * @date 2023/9/16 15:48
     * @param roleId
     * @param empIds
     * @return void
     */
    public void deleteRoleByEmploy(Long roleId, String empIds) throws Exception;

    /**
     * getOrgTreeData
     * 方法功能: 查询所有的员工
     *
     * @param deptId    部门id
     * @param type      只查询部门架构
     * @param showLeave 是否显示离职员工
     * @return java.lang.Object
     * @author yanz
     * @date 2023/10/14 11:28
     */
    public Json<Dict> getOrgTreeData(String deptId, String type, Boolean showLeave);

    /**
     * getOrgTreeUser
     * 方法功能: 模糊搜索用户
     * @author yanz
     * @date 2023/10/14 13:54
     * @param userName 用户名/拼音/首字母
     * @return java.lang.Object 匹配到的用户
     */
    Json<List<OrgTreeVo>> getOrgTreeUser(String userName);
}
