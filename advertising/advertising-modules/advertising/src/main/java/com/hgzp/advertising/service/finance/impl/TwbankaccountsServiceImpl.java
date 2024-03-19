package com.hgzp.advertising.service.finance.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.emnus.file.SfileType;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.finance.dto.BankAccountDTO;
import com.hgzp.advertising.pagemodel.finance.vo.BankAccountVO;
import com.hgzp.advertising.service.finance.TwbankaccountsServiceI;
import com.hgzp.core.model.Twbankaccounthistory;
import com.hgzp.core.model.Twbankaccounts;
import com.hgzp.mapper.finance.TwbankaccounthistoryMapper;
import com.hgzp.mapper.finance.TwbankaccountsMapper;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.exception.UfileException;
import com.hgzp.utils.file.UfileUtil;
import com.hgzp.utils.model.FileInfo;
import com.hgzp.utils.model.LoginUser;
import com.hgzp.utils.model.UfileResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 银行流水单 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-10-26
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TwbankaccountsServiceImpl extends ServiceImpl<TwbankaccountsMapper, Twbankaccounts> implements TwbankaccountsServiceI {
    @Autowired
    TwbankaccountsMapper twbankaccountsMapper;
    @Autowired
    TwbankaccounthistoryMapper twbankaccounthistoryMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public void importBankAccount(FileInfo upfile) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        File file = upfile.getFile();

        ExcelReader reader = ExcelUtil.getReader(file);
        List<BankAccountDTO> exceldataList = reader.readAll(BankAccountDTO.class);

        int datacount = exceldataList.size();
        List<Object> firstLineData = reader.read(0).get(0);

        StringBuffer errMsg = new StringBuffer();
        if (!"账号".equals(firstLineData.get(0).toString().trim())) {
            errMsg.append("请检查第一列是否是账号列!\r");
        }
        if (!"账户名称".equals(firstLineData.get(1).toString().trim())) {
            errMsg.append("请检查第二列是否是账户名称列!\r");
        }
        if (!"交易日期".equals(firstLineData.get(2).toString().trim())) {
            errMsg.append("请检查第三列是否是交易日期列!\r");
        }
        if (!"凭证种类".equals(firstLineData.get(3).toString().trim())) {
            errMsg.append("请检查第四列是否是凭证种类列!\r");
        }
        if (!"贷方发生额".equals(firstLineData.get(6).toString().trim())) {
            errMsg.append("请检查第五列是否是贷方发生额列!\r");
        }
        if (!"对方账号".equals(firstLineData.get(8).toString().trim())) {
            errMsg.append("请检查第六列是否是对方账号列!\r");
        }
        if (!"对方户名".equals(firstLineData.get(9).toString().trim())) {
            errMsg.append("请检查第六列是否是对方户名列!\r");
        }
        if (!"明细类型".equals(firstLineData.get(10).toString().trim())) {
            errMsg.append("请检查第六列是否是明细类型列!\r");
        }
        if (!"备注".equals(firstLineData.get(12).toString().trim())) {
            errMsg.append("请检查第六列是否是备注列!\r");
        }
        if (!"明细编号—交易流水号".equals(firstLineData.get(13).toString().trim())) {
            errMsg.append("请检查第六列是否是明细编号—交易流水号列!\r");
        }
        if (!"交易类别".equals(firstLineData.get(14).toString().trim())) {
            errMsg.append("请检查第六列是否是交易类别列!\r");
        }
        reader.close();
        if (errMsg.length() > 0) {
            upfile.getFile().delete();
            throw new IllegalArgumentException(errMsg.toString());
        }

        UfileResponse response = UfileUtil.postFile(file);
        if (!response.isSuccess()) {
            upfile.getFile().delete();
            throw new UfileException("文件【" + file.getName() + "】统一文件推送失败 ");
        }
        String sFilesha1 = response.getSha1();
        Twbankaccounthistory twbankaccounthistory = new Twbankaccounthistory();
        long bankaccounthistoryid = IdUtil.getSnowflakeNextId();
        twbankaccounthistory.setId(bankaccounthistoryid);
        twbankaccounthistory.setEmployid(user.getUserid());
        twbankaccounthistory.setEmployname(user.getUsername());
        twbankaccounthistory.setSfileformat("." + FilenameUtils.getExtension(upfile.getFileName()));
        twbankaccounthistory.setSfileid(sFilesha1);
        twbankaccounthistory.setSfilesize(String.valueOf(upfile.getFileSize()));
        twbankaccounthistory.setSoriginalfile(upfile.getFileName());
        twbankaccounthistory.setDcreatetime(new Date());
        twbankaccounthistory.setIfilecategory(SfileType.FileType_6.key);
        twbankaccounthistoryMapper.insert(twbankaccounthistory);

        Set<String> orgSet = new HashSet<>();
        for (int i = 0; i < datacount; i++) {
            BankAccountDTO lineData = exceldataList.get(i);
            if (StrUtil.isBlank(lineData.getSborroweraccount())) {
                errMsg.append("第" + (i + 2) + "行 应填写账号!\r");
                continue;
            }
            if (StrUtil.isBlank(lineData.getSborrowername())) {
                errMsg.append("第" + (i + 2) + "行 应填写账户名称!\r");
                continue;
            }
            if (StrUtil.isBlank(lineData.getSlenderaccount())) {
                errMsg.append("第" + (i + 2) + "行 应填写对方账号!\r");
                continue;
            }
            if (StrUtil.isBlank(lineData.getSlendername())) {
                errMsg.append("第" + (i + 2) + "行 应填写对方户名!\r");
                continue;
            }
            if (StrUtil.isBlank(lineData.getStradecode())) {
                errMsg.append("第" + (i + 2) + "行 应填写明细编号—交易流水号!\r");
                continue;
            }
            List<Twbankaccounts> twbankaccountsList = this.lambdaQuery()
                    .eq(Twbankaccounts::getStradecode, lineData.getStradecode())
                    .list();
            if (twbankaccountsList != null && twbankaccountsList.size() > 0) {
                errMsg.append("第" + (i + 2) + "行 明细编号—交易流水号(" + lineData.getStradecode() + ") 已存在重复数据!\r");
                continue;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
            String createTime = lineData.getDtradedate();
            Date date = simpleDateFormat.parse(createTime);

            Twbankaccounts twbankaccounts = new Twbankaccounts();
            BeanUtils.copyProperties(lineData, twbankaccounts);
            long bankaccountid = IdUtil.getSnowflakeNextId();
            twbankaccounts.setDtradedate(date);
            twbankaccounts.setId(bankaccountid);
            twbankaccounts.setBankaccounthistoryid(bankaccounthistoryid);
            twbankaccounts.setDcreatetime(new Date());
            twbankaccounts.setLastoperatorid(user.getUserid());
            twbankaccounts.setLastoperator(user.getUsername());
            twbankaccounts.setNamountallocate(new BigDecimal(0));
            twbankaccounts.setVersion(0L);

            innerInterceptor.recoredLog();
            twbankaccountsMapper.insert(twbankaccounts);
        }

        upfile.getFile().delete();
        if (errMsg.length() > 0) {
            throw new IllegalArgumentException(errMsg.toString());
        }
    }

    @Override
    public IPage<Twbankaccounts> getBankAccountPageList(Page<Twbankaccounts> page, BankAccountVO query) throws Exception {
        String userId = WebUtil.getLoginUser().getUserid().toString();
        LambdaQueryWrapper<Twbankaccounts> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.hasText(query.getQueryKey()), Twbankaccounts::getSlendername, query.getQueryKey());
        lqw.ge(query.getStartTime() != null, Twbankaccounts::getDcreatetime, query.getStartTime());
        if (query.getEndTime() != null) {
            lqw.lt(Twbankaccounts::getDcreatetime, DateUtil.offsetDay(query.getEndTime(), 1));
        }
        lqw.orderByDesc(Twbankaccounts::getDcreatetime);
        return twbankaccountsMapper.selectPage(page, lqw);
    }

    /**
     * 获取未分配完金额的银行流水
     * 方法功能: 银行流水twbankaccount表中namount- nallocated >0的数据
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Twbankaccounts>
     * @author suny
     * @date 2023/12/12 16:23
     */
    @Override
    public IPage<Twbankaccounts> getBalanceBankAccountPageList(Page<Twbankaccounts> page, BankAccountVO query) throws Exception {
        LambdaQueryWrapper<Twbankaccounts> lqw = Wrappers.lambdaQuery();
        lqw.ge(query.getStartTime() != null, Twbankaccounts::getDcreatetime, query.getStartTime());
        if (query.getEndTime() != null) {
            lqw.lt(Twbankaccounts::getDcreatetime, DateUtil.offsetDay(query.getEndTime(), 1));
        }
        lqw.and(StrUtil.isNotBlank(query.getQueryKey()), i -> i.like(Twbankaccounts::getSborrowername, query.getQueryKey())
                .or()
                .like(Twbankaccounts::getSlendername, query.getQueryKey()));
        // 导入金额大于已分配金额（金额有剩余）
        lqw.apply("namount > namountallocate");
        lqw.orderByDesc(Twbankaccounts::getDcreatetime);
        return twbankaccountsMapper.selectPage(page, lqw);
    }
}
