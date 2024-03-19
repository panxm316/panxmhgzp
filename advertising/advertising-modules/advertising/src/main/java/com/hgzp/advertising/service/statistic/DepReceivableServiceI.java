package com.hgzp.advertising.service.statistic;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hgzp.advertising.pagemodel.statistic.vo.AdreceivedVO;
import com.hgzp.core.model.Twcustomer;
import com.hgzp.core.page.Json;
import com.hgzp.pagemodel.ad.AdCustomerResourceVO;
import com.hgzp.pagemodel.schedule.OrderPublishQueryResultVo;
import com.hgzp.pagemodel.statistic.AdChangeVo;
import com.hgzp.pagemodel.statistic.SynQueryResultVO;
import com.hgzp.pagemodel.statistic.SynQueryVO;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

/**
 * AreaReceivedServiceI
 * 创建人：lhl
 * 类描述：业务部门应收汇总报表服务类
 * 创建日期：2024/2/1
 */
public interface DepReceivableServiceI {
    /**
     * 业务部门广告应收业绩完成汇总表
     * 方法功能: 业务部门广告应收业绩完成汇总表
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/2/1
     */
    public AdreceivedVO exportDeptReceivable();

    /**
     * 业务部门广告应收业绩完成汇总表导出EXCEL
     * 方法功能: 业务部门广告应收业绩完成汇总表导出EXCEL
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/2/1
     */
    public XSSFWorkbook exportDeptReceivableExcel();

    /**
     * 区域完成情况统计表
     * 方法功能: 区域完成情况统计表
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/2/3
     */
    public AdreceivedVO exportAreaReceivable();

    /**
     * 区域完成情况统计表导出EXCEL
     * 方法功能: 区域完成情况统计表导出EXCEL
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/2/3
     */
    public XSSFWorkbook exportAreaReceivableExcel();

    /**
     * 区域应收情况统计表
     * 方法功能: 区域应收情况统计表
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/2/5
     */
    public AdreceivedVO exportAreaIncomeReceivable();

    /**
     * 区域应收情况统计表导出EXCEL
     * 方法功能: 区域应收情况统计表导出EXCEL
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/2/5
     */
    public XSSFWorkbook exportAreaIncomeReceivableExcel();

    /**
     * 广告应收情况分析
     * 方法功能: 广告应收情况分析
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/2/18
     */
    public AdreceivedVO exportReceivableAnalysis(String mediaId,String dateTime);

    /**
     * 广告应收情况导出EXCEL
     * 方法功能: 广告应收导出EXCEL
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/2/19
     */
    public XSSFWorkbook exportReceivableAnalysisExcel(String mediaId,String dateTime);

    /**
     * 查询变动订单
     * 方法功能: 查询变动订单
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/2/20
     */

    public IPage<AdChangeVo> queryChangeOrder(String dateTime, int pageNum, int pageSize);

    /**
     * 查询变动订单导出EXCEL
     * 方法功能: 查询变动订单导出EXCEL
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/2/21
     */
    public XSSFWorkbook exportChangeOrderExcel(String dateTime);

    /**
     * 综合查询
     * 方法功能: 综合查询
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/2/26
     */
    public IPage<SynQueryResultVO> synQuery(SynQueryVO vo, int pageNum, int pageSize);

    /**
     * 客户来源查询
     * 方法功能: 客户来源查询
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/3/12
     */
    public List<AdCustomerResourceVO> customerResourceQuery(String level, String startTime, String endTime);

    /**
     * 客户明细查询
     * 方法功能: 客户明细查询
     *
     * @param
     * @return
     * @author lhl
     * @date 2024/3/13
     */
    public List<Twcustomer> getCustomerDetail(Long id, String startTime, String endTime);
}
