package com.hgzp.mapper.system;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgzp.core.model.Twlog;

public interface TwlogMapper extends BaseMapper<Twlog> {
    int deleteByPrimaryKey(String sguid);

    Twlog selectByPrimaryKey(String sguid);

    int updateByPrimaryKeySelective(Twlog record);

    int updateByPrimaryKey(Twlog record);
}