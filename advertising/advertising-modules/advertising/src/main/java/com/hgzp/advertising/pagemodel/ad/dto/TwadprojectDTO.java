package com.hgzp.advertising.pagemodel.ad.dto;

import com.hgzp.core.model.Twadproject;
import com.hgzp.core.model.Twadprojectfiles;
import lombok.Data;

import java.util.List;

/**
 TwadprojectDTO
 创建人：songly
 类描述：TODO
 创建日期：2024/3/15 14:40
 */
@Data
public class TwadprojectDTO extends Twadproject {

    /**
     * 流程Id
     */
    private String flowId;

    /**     * 项目文件列表     */
    private List<Twadprojectfiles> projectfiles;
}