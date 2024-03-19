package com.hgzp.advertising.pagemodel.flow.dto;

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * EmpHolidayDTO
 * 创建人：suny
 * 类描述：人员休假信息对象实体
 * 创建日期：2023/10/26 11:03
 *
 * @测试：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@LogData(alias = "休假信息")
public class EmpHolidayDTO extends BaseDTO {
    /**
     * 主键
     */
    @NotNull(message = "休假id不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    @LogData(showColumn = "employname")
    private Long id;

    /**
     * 人员ID
     */
    @NotNull(message = "休假人员id不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    @LogData(alias = "休假人员id")
    private Long employid;

    /**
     * 人员名称
     */
    @NotBlank(message = "休假人员名称不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    @LogData(alias = "休假人员名称")
    private String employname;

    /**
     * 开始日期
     */
    @NotNull(message = "休假开始日期不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    @LogData(alias = "休假开始日期")
    private Date dstartdate;

    /**
     * 结束日期
     */
    @NotNull(message = "休假结束日期不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    @LogData(alias = "休假结束日期")
    private Date denddate;

    /**
     * 接手人员ID
     */
    @NotNull(message = "接手人员id不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    @LogData(alias = "接手人员id")
    private Long newemployid;

    /**
     * 接手人员名称
     */
    @NotBlank(message = "接手人员名称不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    @LogData(alias = "接手人员名称")
    private String newemployname;

    /**
     * 操作员ID
     */
    private Long operatorid;

    /**
     * 操作员名称
     */
    private String operatorname;

    /**
     * 创建日期
     */
    private Date dcreatedate;

    /**
     * 是否启用
     */
    private Boolean buse;

    /**
     * 备注
     */
    private String sremark;
}
