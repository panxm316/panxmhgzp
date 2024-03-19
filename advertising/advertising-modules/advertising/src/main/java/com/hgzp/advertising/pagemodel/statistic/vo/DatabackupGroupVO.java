package com.hgzp.advertising.pagemodel.statistic.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.hgzp.core.model.Twdatabackupgroup;
import com.hgzp.core.page.BaseQueryInfo;
import lombok.*;
import lombok.experimental.Accessors;
import static com.hgzp.core.constant.ValidateParam.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 * 数据轧账总表 VO 前端页面视图对象
 * </p>
 *
 * @author muyn
 * @since 2024-01-19
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DatabackupGroupVO extends BaseQueryInfo {

    /**
     * 主键
     */
    private Long id;
    /**
     * 创建者id
     */
    private Long createempid;
    /**
     * 创建者名称
     */
    private String createempname;
    /**
     * 创建日期
     */
    private Date dcreatetime;
    /**
     * 名称
     */
    private String databackupname;
    /**
     * 汇总类型(广告明细-orderitem，核销明细：apportiondetail)
     */
    private String datatype;
    /**
     * 开始日期
     */
    private Date dstartdate;
    /**
     * 结束日期
     */
    private Date denddate;
    /**
     * 记录数
     */
    private Integer nrecordsize;
    /**
     * 明细表名
     */
    private String sdetailtablename;
    /**
     * 说明
     */
    private String sdescription;

    public static DatabackupGroupVO parseToVO(Twdatabackupgroup entity) {
        DatabackupGroupVO databackupgroupVO = new DatabackupGroupVO();
        BeanUtils.copyProperties(entity, databackupgroupVO);
        return databackupgroupVO;
    }

    public static Twdatabackupgroup parseToEntity(DatabackupGroupVO vo) {
        Twdatabackupgroup entity = new Twdatabackupgroup();
        BeanUtils.copyProperties(vo, entity);
        return entity;
    }
}
