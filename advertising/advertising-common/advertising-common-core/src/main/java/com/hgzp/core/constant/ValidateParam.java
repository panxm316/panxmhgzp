package com.hgzp.core.constant;

/**
 * 创建人：wangwk
 * 类描述：字段校验分组
 * 创建日期：2023/8/15 10:40
 */
public interface ValidateParam {

    /**
     * 参数校验分组：增加
     */
    public @interface add {
    }

    /**
     * 参数校验分组：编辑
     */
    public @interface edit {
    }

    /**
     * 参数校验分组：删除
     */
    public @interface delete {
    }

    /**
     * 参数校验分组：详情
     */
    public @interface detail {
    }

    /**
     * 参数校验分组：分页
     */
    public @interface page {
    }

    /**
     * 参数校验分组：列表
     */
    public @interface list {
    }

    /**
     * 参数校验分组：保存广告预约前置校验
     */
    @interface savePreOrder {
    }

}
