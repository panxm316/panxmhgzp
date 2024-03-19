package com.hgzp.advertising.pagemodel.ad.vo;

import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 移动端媒体叠次树对象
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
@Accessors(chain = true)
public class MediaFoldTree {

    /**
     * id
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 父id
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long parentId;

    /**
     * 分类（现用于区分不同节点分类标志）媒体类型mediatype 媒体media 叠次fold
     */
    private String type;

    /**
     * 子节点
     */
    private List<MediaFoldTree> children;

    /**
     * 前端是否选中标志
     */
    private boolean checked;

    /**
     * 媒体类型key (paper app wei multi agent tv radio video website)
     */
    private String mediaTypeKey;

    /**
     * 媒体类型名称
     */
    private String mediaTypeName;

    /**
     * 媒体id
     */
    private Long mediaId;

    /**
     * 媒体名称
     */
    private String mediaName;

    /**
     * 叠次id
     */
    private Long foldId;

    /**
     * 叠次名称
     */
    private String foldName;

}
