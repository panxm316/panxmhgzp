package com.hgzp.mapper.system;

import com.hgzp.core.model.Tbrole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbroleMapper extends BaseMapper<Tbrole> {




    public List<Map> selectScope(@Param("sql") String sql);

}
