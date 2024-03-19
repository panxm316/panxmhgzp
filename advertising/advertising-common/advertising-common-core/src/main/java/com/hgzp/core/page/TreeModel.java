package com.hgzp.core.page;

import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TreeModel
 * 创建人：wangwk
 * 类描述：前端显示树模型
 * 创建日期：2023/8/17 15:36
 */
@Data
public class TreeModel {

    /**
     * 图标：部门树的  部门， 人员树的根节点
     */
    public static final String UNIT = "unit";

    /**
     * 人员树的部门节点
     */
    public static final String DEPT = "dept";


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
     * 关键字
     */
    private String skey;

    /**
     * 是否有效
     */
    private boolean buse;

    /**
     * 是否是叶子结点
     */
    private boolean bleaf;

    /**
     * 是否显示选中框, true 不显示， false 显示
     */
    private boolean nocheck;

    /**
     * 父id
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long parentId;

    /**
     * 分类 element-ui 树用（现用于区分不同节点分类标志）媒体类型mediatype 媒体media 叠次fold
     */
    private String stype;

    /**
     * 图标 ztree树用
     */
    private String iconSkin;

    /**
     * 子节点
     */
    private List<TreeModel> children;

    /**
     * 前端是否选中标志
     */
    private boolean checked;

    /**
     * buildTreeModel
     * 方法功能: 将平级树结构递归构造成嵌套树结构
     * @author wangwk
     * @date 2023/8/17 16:35
     * @param treeModelList
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     */
    public static List<TreeModel> buildTreeModel(List<TreeModel> treeModelList){
        if(treeModelList != null){
            List<TreeModel> firstNode = treeModelList.stream()
                    .filter(node -> node.getParentId() == null || node.getParentId() == 0)
                    .collect(Collectors.toList());
            firstNode.forEach(node -> node.setChildren(buildChild(node.getId(), treeModelList)));
            return firstNode;
        }
        return null;
    }

    private static List<TreeModel> buildChild(Long parenId, List<TreeModel> treeModelList){
        List<TreeModel> child = new ArrayList<>();

        List<TreeModel> childlist = treeModelList.stream()
                .filter(node -> parenId.equals(node.getParentId()))
                .collect(Collectors.toList());
        if(childlist.size() != 0){
            child.addAll(childlist);
            childlist.forEach(node -> {
                node.setChildren(buildChild(node.getId(), treeModelList));
            });
        }else{
            return child;
        }
        return child;
    }


}
