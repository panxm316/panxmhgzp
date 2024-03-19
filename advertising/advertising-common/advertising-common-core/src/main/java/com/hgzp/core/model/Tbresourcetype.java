package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 资源文件类型
 * </p>
 *
 * @author wwk
 * @since 2023-10-27
 */
@Getter
@Setter
public class Tbresourcetype implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 类型名称
     */
    @LogData(alias = "类型名称")
    private String sname;

    /**
     * 文件格式
     */
    @LogData(alias = "文件格式")
    private String sfileformat;

    /**
     * 中文类型名称
     */
    @LogData(alias = "中文名称")
    private String stypename;

    /**
     * 序号
     */
    @LogData(alias = "序号")
    private Integer isort;

    /**
     * 启用
     */
    @LogData(alias = "启用")
    private Boolean buse;
}
