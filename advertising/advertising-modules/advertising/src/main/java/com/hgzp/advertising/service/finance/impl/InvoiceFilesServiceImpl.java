package com.hgzp.advertising.service.finance.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.finance.dto.InvoiceFilesDTO;
import com.hgzp.advertising.service.finance.InvoiceFilesServiceI;
import com.hgzp.advertising.service.resource.TwresourcesServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.model.Twinvoicefiles;
import com.hgzp.core.model.Twresources;
import com.hgzp.mapper.finance.TwinvoicefilesMapper;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 发票文件表(用于存放电子发票) 服务实现类
 * </p>
 *
 * @author muyn
 * @since 2023-12-26
 */
@Service
public class InvoiceFilesServiceImpl extends ServiceImpl<TwinvoicefilesMapper, Twinvoicefiles> implements InvoiceFilesServiceI {
    @Value("${ufile.uWebURL}")
    private String uWebURL;
    @Autowired
    private TwresourcesServiceI sourceServiceI;
    @Autowired
    private HgDataChangeRecorderInnerInterceptor innerInterceptor;

    /**
     * 保存发票文件
     * 方法功能:保存发票文件
     *
     * @param dtoList
     * @param user
     * @return void
     * @author yanz
     * @date 2023/12/28 15:24
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInvoiceFiles(List<InvoiceFilesDTO> dtoList, LoginUser user) {
        List<Twinvoicefiles> invoicefilesList = dtoList.stream().map(InvoiceFilesDTO::parseToEntity).collect(Collectors.toList());
        innerInterceptor.recoredLog();
        boolean saved = this.saveBatch(invoicefilesList);
        if (!saved) {
            throw new DataExistException("保存发票文件失败");
        }
        // 检查Twresource表，没记录加记录
        handleTwresourcesOperations(invoicefilesList, user);
    }

    /**
     * 据发票表id（InvoiceId）获取相关的文件
     * 方法功能:据发票表id（InvoiceId）获取相关的文件
     *
     * @param invoiceId
     * @return java.util.List<com.hgzp.advertising.pagemodel.finance.dto.InvoiceFilesDTO>
     * @author yanz
     * @date 2023/12/28 16:14
     */
    @Override
    public List<InvoiceFilesDTO> getFilesByInvoiceId(Long invoiceId) {
        List<Twinvoicefiles> list = this.lambdaQuery().eq(Twinvoicefiles::getInvoiceid, invoiceId).list();
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<InvoiceFilesDTO> dtolist = list.stream().map(InvoiceFilesDTO::parseToDTO).collect(Collectors.toList());
        // url和bdelete 没有注入
        dtolist = dtolist.stream().map(o -> {
            o.setUrl(uWebURL + o.getSfileid());
            return o;
        }).collect(Collectors.toList());
        return dtolist;
    }

    @Transactional(rollbackFor = Exception.class)
    public void handleTwresourcesOperations(List<Twinvoicefiles> files, LoginUser user) {
        List<String> sfileids = sourceServiceI.listObjs(
                Wrappers.<Twresources>lambdaQuery().in(Twresources::getSfileid, files.stream().map(Twinvoicefiles::getSfileid).collect(Collectors.toList()))  // 20231229 suny
                        .select(Twresources::getSfileid),
                Object::toString
        );
        List<Twresources> toSave = new ArrayList<>();
        for (Twinvoicefiles file : files) {
            if (file.getSfileid() != null && sfileids.contains(file.getSfileid())) {
                continue;
            }
            Twresources source = Twresources.from(file);
            source.setEmployid(user.getUserid());
            toSave.add(source);
        }
        innerInterceptor.recoredLog();
        boolean saved = sourceServiceI.saveBatch(toSave);
        if (!saved) {
            throw new DataExistException("保存发票文件Resouce记录失败");
        }
    }
}
