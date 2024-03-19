package com.hgzp.mapper.statistic;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgzp.core.model.Twdatabackupdetail1;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 数据轧账明细表示例（广告明细） Mapper 接口
 * </p>
 *
 * @author muyn
 * @since 2024-01-19
 */
public interface Twdatabackupdetail1Mapper extends BaseMapper<Twdatabackupdetail1> {
    /**
     * 根据查询条件和表名查询数据轧账明细分页列表
     * 方法功能:  根据查询条件和表名查询数据轧账明细分页列表
     *
     * @return
     */
    List<Twdatabackupdetail1> selectByTablename(@Param("tablename") String tablename, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("mediaid") Long mediaid,
                                                @Param("customerName") String customerName, @Param("deptid") Long deptid, @Param("employid") Long employid,
                                                @Param("pageNum") Long pageNum, @Param("pageSize") Long pageSize);

    /**
     * 根据查询条件和表名查询数据轧账明细总数
     * 方法功能: 根据查询条件和表名查询数据轧账明细总数
     *
     * @param tablename
     * @param startTime
     * @param endTime
     * @param mediaid
     * @param customerName
     * @param deptid
     * @param employid
     * @return java.lang.Long
     * @author suny
     * @date 2024/1/20 10:58
     */
    Long getCountByTable(@Param("tablename") String tablename, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("mediaid") Long mediaid,
                         @Param("customerName") String customerName, @Param("deptid") Long deptid, @Param("employid") Long employid);

    /**
     * 将订单刊期数据备份到数据轧账明细表
     * 方法功能: 将订单刊期数据备份到数据轧账明细表
     *
     * @param tablename
     * @param databackupgroupid
     * @param startTime
     * @param endTime
     * @return void
     * @author suny
     * @date 2024/1/20 14:21
     */
    void updateDataBackupDetail(@Param("tablename") String tablename, @Param("databackupgroupid") Long databackupgroupid, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 创建新的明细表
     * 方法功能: 创建新的明细表
     *
     * @param tableName
     * @return void
     * @author suny
     * @date 2024/1/20 14:22
     */
    @Insert("CREATE TABLE ${tableName} (id                   bigint not null auto_increment comment '自增列',\n" +
            "   databackupgroupid    bigint comment '数据备份组表id',\n" +
            "   orderid              bigint comment '订单id',\n" +
            "   sordernum            varchar(50) comment '订单号',\n" +
            "   scontractnum         varchar(50) comment '合同号',\n" +
            "   adprojectid          bigint comment '广告项目id',\n" +
            "   adprojectname        varchar(500) comment '广告项目名称',\n" +
            "   ordercreateempid     bigint comment '订单创建者id',\n" +
            "   ordercreateempname   varchar(200) comment '订单创建者名称',\n" +
            "   ordercreatetime      timestamp comment '订单创建日期',\n" +
            "   orderibooktype       int(11) comment '订单录入方式 0-正常 1-广告预约 2-快速预约 3-补刊',\n" +
            "   businessentityid     bigint comment '经营主体id',\n" +
            "   businessentityname   varchar(50) comment '经营主体名称',\n" +
            "   customerid           bigint comment '客户id',\n" +
            "   customername         varchar(500) comment '客户名称',\n" +
            "   scontacts            varchar(100) comment '联系人',\n" +
            "   saddress             varchar(500) comment '地址',\n" +
            "   smobilephone         varchar(50) comment '联系人电话',\n" +
            "   spostcode            varchar(10) comment '邮编',\n" +
            "   deptid               bigint comment '部门id',\n" +
            "   deptname             varchar(200) comment '部门名称',\n" +
            "   iorderkind           int(11) comment '订单类别 0-本部广告1-记者站广告 2-编辑广告 3-上门广告',\n" +
            "   employid             bigint comment '主业务员id',\n" +
            "   employname           varchar(200) comment '主业务员名称',\n" +
            "   agencytid            bigint comment '代理公司id',\n" +
            "   agencyname           varchar(500) comment '代理公司名称',\n" +
            "   agencyemployid       bigint comment '代理公司业务员id',\n" +
            "   agencyemployname     varchar(200) comment '代理公司业务员名称',\n" +
            "   agentid              bigint comment '内容生产方id',\n" +
            "   agentname            varchar(500) comment '内容生产方',\n" +
            "   agentemployid        bigint comment '内容生产方业务员id',\n" +
            "   agentemployname      varchar(200) comment '内空生产方业务员名称',\n" +
            "   adindustyid          bigint comment '行业id',\n" +
            "   adindustryname       varchar(50) comment '行业名称',\n" +
            "   adtypeid             bigint comment '广告类型id',\n" +
            "   adtypename           varchar(50) comment '广告类型名称',\n" +
            "   sorderadtitle        varchar(500) comment '广告标题',\n" +
            "   sorderadcontent      varchar(1000) comment '广告内容',\n" +
            "   borderuse            bit comment '是否有效',\n" +
            "   borderdelete         bit comment '是否删除',\n" +
            "   sopinion             varchar(500) comment '负责人意见',\n" +
            "   iorderapprovestatus  int(11) comment '审核状态(0-待审、1-在审、2-通过、3-否决、4-撤销、5-无效）',\n" +
            "   bspecial             bit comment '是否特殊订单(0 否 1-是)',\n" +
            "   sspecialreason       varchar(1000) comment '特殊原因',\n" +
            "   orderitemid          bigint comment '广告明细主键',\n" +
            "   createempid          bigint comment '创建者id',\n" +
            "   createempname        varchar(200) comment '创建者名称',\n" +
            "   createtime           timestamp comment '创建日期',\n" +
            "   ibooktype            int(11) comment '录入方式 0-正常 1-广告预约 2-快速预约 3-补刊',\n" +
            "   mediatypekey         varchar(50) comment '媒体类型key',\n" +
            "   mediatypename        varchar(200) comment '媒体类型名称',\n" +
            "   mediaid              bigint comment '媒体id',\n" +
            "   medianame            varchar(200) comment '媒体名称',\n" +
            "   prestartdate         timestamp comment '预见报开始日期',\n" +
            "   preenddate           timestamp comment '预见报结束日期',\n" +
            "   foldid               bigint comment '叠次id',\n" +
            "   foldname             varchar(50) comment '叠次名称',\n" +
            "   foldareaverid        bigint comment '叠次版本id',\n" +
            "   foldareavername      varchar(50) comment '叠次版本名称',\n" +
            "   addisplayformid      bigint comment '广告形式id',\n" +
            "   addisplayformname    varchar(50) comment '广告形式名称',\n" +
            "   pagesortid           bigint comment '版面类别id',\n" +
            "   pagesortname         varchar(50) comment '版面类别名称',\n" +
            "   adcolorid            bigint comment '色彩id',\n" +
            "   adcolorname          varchar(50) comment '色彩名称',\n" +
            "   adspecid             bigint comment '规格id',\n" +
            "   adspecname           varchar(50) comment '规格名称',\n" +
            "   nwidth               decimal(18,2) comment '宽',\n" +
            "   nheight              decimal(18,2) comment '高',\n" +
            "   weeksettingid        bigint comment '星期id',\n" +
            "   weeksettingname      varchar(50) comment '星期名称',\n" +
            "   foldpageplanid       bigint comment '版面id',\n" +
            "   sadtitle             varchar(500) comment '广告标题',\n" +
            "   sadcontent           varchar(1000) comment '广告内容',\n" +
            "   foldpageplanname     varchar(50) comment '版面名称',\n" +
            "   baseprice            decimal(18,2) comment '刊例价',\n" +
            "   priceitemid          bigint comment '价格明细表id',\n" +
            "   normalprice          decimal(18,2) comment '标准价格',\n" +
            "   amountreceivable     decimal(18,2) comment '应收金额',\n" +
            "   amountreceived       decimal(18,2) comment '已收金额',\n" +
            "   amountarrearage      decimal(18,2) comment '欠款金额',\n" +
            "   ndiscountrate        decimal(18,2) comment '折扣率',\n" +
            "   amountachievement    decimal(18,2) comment '业绩金额',\n" +
            "   dachievementdate     timestamp comment '业绩时间',\n" +
            "   namountcost          decimal(18,2) comment '成本金额',\n" +
            "   iaddapprovestatus    int(11) comment '加刊审批状态',\n" +
            "   ichangeapprovestatus int(11) comment '改刊审批状态',\n" +
            "   istopapprovestatus   int(11) comment '停刊审批状态',\n" +
            "   idiscountapprovestatus int(11) comment '折扣审批状态',\n" +
            "   iapprovestatus       int(11) comment '订单审批状态',\n" +
            "   ipublishstatus       int(11) comment '发布状态0-预约 1-预订 2-待发布 3-安排 4-见报 5-已发布 6-上架 7-下架',\n" +
            "   buse                 bit comment '是否有效',\n" +
            "   bdelete              bit comment '是否删除',\n" +
            "   breportreason        bit comment '是否推送填报欠款原因',\n" +
            "   sremark              varchar(500) comment '备注',\n" +
            "   iitemcode            bigint comment '刊期编码(自增列)',\n" +
            "   ipublishcheckstatus  int comment '刊发检测状态(0-正常 1-未刊发 2-刊发错误',\n" +
            "   spublishcheckcontent  varchar(1000) comment '刊发检测报告',\n" +
            "   primary key (id))")
    void createTable(@Param("tableName") String tableName);
}
