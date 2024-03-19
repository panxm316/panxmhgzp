package com.hgzp.advertising.service.customer.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerfilesVo;
import com.hgzp.advertising.service.customer.TwcustomerfilesServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.model.*;
import com.hgzp.mapper.customer.TwcustomerfilesMapper;
import com.hgzp.mapper.resource.TwresourcesMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.file.UfileUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户文件表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-10-25
 */
@Service
public class TwcustomerfilesServiceImpl extends MyServiceImpl<TwcustomerfilesMapper, Twcustomerfiles> implements TwcustomerfilesServiceI {

    @Autowired
    TwcustomerfilesMapper filesMapper;
    @Autowired
    TwresourcesMapper resourcesMapper;

    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public List<CustomerfilesVo> getCustomerFiles(String sCustomerId) {
        List<Twcustomerfiles> fileslist = new LambdaQueryChainWrapper<>(filesMapper)
                .eq(Twcustomerfiles::getCustomerid, sCustomerId)
                .orderByAsc(Twcustomerfiles::getDcreatetime)
                .list();
        List<CustomerfilesVo> result = new ArrayList<>();
        for (Twcustomerfiles record : fileslist) {
            CustomerfilesVo temp = new CustomerfilesVo();
            BeanUtils.copyProperties(record, temp);
            String url = UfileUtil.getStaticUrl(record.getSfileid(), record.getSfileformat());
            temp.setDurl(url);
            result.add(temp);
        }

        return result;
    }

    @Override
    public void saveCustomerFiles(List<Twcustomerfiles> lsCustomerfiles) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        for (Twcustomerfiles item : lsCustomerfiles) {
            if (isDuplicateInfo(item)) {
                throw new DataExistException("已存在相同的文件[" + item.getSoriginalfile() + "]  ");
            }
            Date d = new Date();
            item.setDcreatetime(d);
            innerInterceptor.recoredLog();
            save(item);
            //写twresources表
            Twresources resource = new Twresources();
            resource.setDcreatetime(d);
            resource.setEmployid(user.getUserid());
            resource.setSdescription(item.getSdescription());
            resource.setSfileformat(item.getSfileformat());
            resource.setSfileid(item.getSfileid());
            resource.setSfilesize(item.getSfilesize());
            resource.setSfiletype(item.getSfiletype());
            resource.setSoriginalfile(item.getSoriginalfile());

            innerInterceptor.recoredLog();
            resourcesMapper.insert(resource);
        }

    }

    @Override
    public void updateCustomerFiles(List<Twcustomerfiles> lsCustomerfiles) throws Exception {
        //检索数据表中已有附件
        LambdaQueryWrapper<Twcustomerfiles> wrapperB = Wrappers.lambdaQuery();
        wrapperB.eq(Twcustomerfiles::getCustomerid, lsCustomerfiles.get(0).getCustomerid())
                .eq(Twcustomerfiles::getBdelete, 0);
        List<Twcustomerfiles> lsFilesDB = filesMapper.selectList(wrapperB);
        //数据库中如果在新加的列表不存在就删除
        innerInterceptor.recoredLog();
        if (lsFilesDB.size() > 0) {
            for (Twcustomerfiles item : lsFilesDB) {
                boolean bExist = lsCustomerfiles.stream().anyMatch(i -> i.getId()!=null&& i.getId().equals(item.getId()));
                if (!bExist) {
                    filesMapper.deleteById(item);
                }
            }
        }

        innerInterceptor.recoredLog();
        for (Twcustomerfiles item : lsCustomerfiles) {
            if (isDuplicateInfo(item)) {
                throw new DataExistException("已存在相同的文件[" + item.getSoriginalfile() + "] ");
            }
            if (item.getId() == null) {
                save(item);
            } else {
                if (filesMapper.updateById(item) == 0) {
                    throw new OptimisticLockingFailureException("数据已改变，请刷新后再重新操作！");
                }
            }
        }
    }

//    @Override
//    public void recoveryFilesByIds(String ids, boolean bCustomerId) throws Exception {
//        String[] split = ids.split(",");
//        List<Twcustomerfiles> lsCustomerfiles = new ArrayList<>();
//        if (bCustomerId) {
//            lsCustomerfiles = this.lambdaQuery()
//                    .in(Twcustomerfiles::getCustomerid, split)
//                    .list();
//        } else {
//            lsCustomerfiles = this.lambdaQuery()
//                    .in(Twcustomerfiles::getId, split)
//                    .list();
//        }
//        innerInterceptor.recoredLog();
//        for (Twcustomerfiles t : lsCustomerfiles) {
//            t.setBdelete(false);
//            filesMapper.updateById(t);
//        }
//
//    }

    @Override
    public void deleteFilesByIds(String ids, boolean bCustomerId) throws Exception {
        String[] split = ids.split(",");
        List<Twcustomerfiles> lsCustomerfiles = new ArrayList<>();
        if (bCustomerId) {
            lsCustomerfiles = this.lambdaQuery()
                    .in(Twcustomerfiles::getCustomerid, split)
                    .list();
        } else {
            lsCustomerfiles = this.lambdaQuery()
                    .in(Twcustomerfiles::getId, split)
                    .list();
        }
        innerInterceptor.recoredLog();
        for (Twcustomerfiles t : lsCustomerfiles) {
            //已修改为可以实际删除
//            t.setBdelete(true);
//            filesMapper.updateById(t);
            filesMapper.deleteById(t);
        }
    }

    //是否存在相同的记录
    @Override
    public boolean isDuplicateInfo(Twcustomerfiles ustomerfiles) {
        LambdaQueryWrapper<Twcustomerfiles> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Twcustomerfiles::getSfileid, ustomerfiles.getSfileid())
                .eq(Twcustomerfiles::getCustomerid, ustomerfiles.getCustomerid())
                .ne(ustomerfiles.getId() != null, Twcustomerfiles::getId, ustomerfiles.getId())
                .ne(Twcustomerfiles::getBdelete, true);
        return filesMapper.selectOne(wrapper) != null;
    }

    @Override
    public void combineCustomerFiles(String customerIds, Long newcustomerId) throws Exception {
        List<Twcustomerfiles> lsMainCustomerFile = this.lambdaQuery()
                .eq(Twcustomerfiles::getCustomerid, newcustomerId)
                .list();

        //被合并客户资料信息
        String[] split = customerIds.split(",");
        List<Twcustomerfiles> lsSubCustomer = this.lambdaQuery()
                .in(Twcustomerfiles::getCustomerid, split)
                .list();

        for (Twcustomerfiles itemFile : lsSubCustomer) {
            //sfileid存在不添加
            int iCount =
                    lsMainCustomerFile.stream().filter(s -> s.getSfileid().equals(itemFile.getSfileid())).collect(Collectors.toList()).size();
            if (iCount > 0) {
                continue;
            }
            itemFile.setCustomerid(newcustomerId);
            innerInterceptor.recoredLog();
            updateById(itemFile);
        }
    }
}
