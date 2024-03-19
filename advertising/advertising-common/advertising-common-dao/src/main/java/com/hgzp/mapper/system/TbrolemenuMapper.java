package com.hgzp.mapper.system;

import com.hgzp.core.model.Tbrolemenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色菜单表 Mapper 接口
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbrolemenuMapper extends BaseMapper<Tbrolemenu> {


    public List<Tbrolemenu> getAuthMenuListById(@Param("roleId") Long roleId);


}
