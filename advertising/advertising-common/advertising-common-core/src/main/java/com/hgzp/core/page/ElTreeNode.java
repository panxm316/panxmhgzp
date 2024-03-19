package com.hgzp.core.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * ElTreeNode
 * <br>
 * 用来映射 Element Plus 树节点
 *
 * @author wangwk
 * @see <a href="https://element-plus.org/zh-CN/component/tree-select.html">参考链接</a>
 * @since 2023-12-06
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
@Accessors(chain = true)
public class ElTreeNode {
    private String value;
    private String label;
    private List<ElTreeNode> children;
    private boolean disabled;
    /**
     * 扩展对象
     */
    private Object extObj;
}
