package com.hgzp.advertising.service.finance.impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.finance.dto.BankAccountDTO;
import com.hgzp.advertising.pagemodel.finance.dto.CostEmployDTO;
import com.hgzp.advertising.service.finance.TwcostemployServiceI;
import com.hgzp.core.model.Twcostemploy;
import com.hgzp.mapper.finance.TwcostemployMapper;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.FileInfo;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * <p>
 * 成本表(人员) 服务实现类
 * </p>
 *
 * @author muyn
 * @since 2024-03-15
 */
@Service
public class TwcostemployServiceImpl extends ServiceImpl<TwcostemployMapper, Twcostemploy> implements TwcostemployServiceI {

    @Autowired
    private HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public void importCostEmploy(FileInfo upfile) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        File file = upfile.getFile();

        ExcelReader reader = ExcelUtil.getReader(file);
        List<CostEmployDTO> exceldataList = reader.readAll(CostEmployDTO.class);

        int datacount = exceldataList.size();
        List<Object> firstLineData = reader.read(0).get(0);
        StringBuffer errMsg = new StringBuffer();
        if (!"账号".equals(firstLineData.get(0).toString().trim())) {
            errMsg.append("请检查第一列是否是账号列!\r");
        }
    }
}
