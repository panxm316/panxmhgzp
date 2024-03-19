package com.hgzp.advertisingsys.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hgzp.advertisingsys.service.system.TbdeptexServiceI;
import com.hgzp.core.model.Tbdept;
import com.hgzp.core.model.Tbdeptex;
import com.hgzp.mapper.system.TbdeptMapper;
import com.hgzp.mapper.system.TbdeptexMapper;
import com.hgzp.service.system.impl.BaseTbdeptexServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门扩展表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbdeptexServiceImpl extends BaseTbdeptexServiceImpl implements TbdeptexServiceI {


    @Autowired
    TbdeptMapper tbdeptMapper;
    @Autowired
    TbdeptexMapper tbdeptexMapper;


    @Override
    public void updateDeptExName(Tbdept tbdept) {
        UpdateWrapper<Tbdeptex> updateWrapper = new UpdateWrapper();
        updateWrapper.set("parentname" + tbdept.getIdepth(), tbdept.getSdeptname())
                .eq("parentid" + tbdept.getIdepth(), tbdept.getId());
        this.update(updateWrapper);
    }


    @Override
    public void addDeptEx(Tbdept tbdept) {
        Tbdeptex tbdeptex = new Tbdeptex();
        if (tbdept.getParentid() == null || tbdept.getParentid() == 0) {
            //新增的一级部门
            tbdeptex.setId(tbdept.getId());
            tbdeptex.setDeptid(tbdept.getId());
            tbdeptex.setParentid1(tbdept.getId());
            tbdeptex.setParentname1(tbdept.getSdeptname());
        } else {
            //新增的其他级部门，查出上级节点复制
            Tbdeptex parentDetpEx = tbdeptexMapper.selectById(tbdept.getParentid());
            BeanUtils.copyProperties(parentDetpEx, tbdeptex);
            tbdeptex.setId(tbdept.getId());
            tbdeptex.setDeptid(tbdept.getId());
            switch (tbdept.getIdepth()) {
                case 2:
                    tbdeptex.setParentid2(tbdept.getId());
                    tbdeptex.setParentname2(tbdept.getSdeptname());
                    break;
                case 3:
                    tbdeptex.setParentid3(tbdept.getId());
                    tbdeptex.setParentname3(tbdept.getSdeptname());
                    break;
                case 4:
                    tbdeptex.setParentid4(tbdept.getId());
                    tbdeptex.setParentname4(tbdept.getSdeptname());
                    break;
                case 5:
                    tbdeptex.setParentid5(tbdept.getId());
                    tbdeptex.setParentname5(tbdept.getSdeptname());
                    break;

                default:

            }
        }
        tbdeptexMapper.insert(tbdeptex);
    }


    @Override
    public void resetDeptEx() {
        tbdeptexMapper.delete(null);

        List<Tbdept> tbdeptList = tbdeptMapper.selectList(null);

        Map<Integer, List<Tbdept>> listMap = tbdeptList.stream().collect(Collectors.groupingBy(Tbdept::getIdepth));

        List<Tbdeptex> resultList = new ArrayList<>();


        for (Map.Entry<Integer, List<Tbdept>> entry : listMap.entrySet()) {
            Integer idepth = entry.getKey();
            List<Tbdept> tbdeptIdepthList = entry.getValue();

            for (Tbdept tbdept : tbdeptIdepthList) {
                Tbdeptex tbdeptex = new Tbdeptex();
                tbdeptex.setId(tbdept.getId());
                tbdeptex.setDeptid(tbdept.getId());

                if (idepth == 1) {
                    tbdeptex.setParentid1(tbdept.getId());
                    tbdeptex.setParentname1(tbdept.getSdeptname());

                }
                if (idepth == 2) {
                    Tbdept tbdept1 = listMap.get(1).stream().filter(d -> d.getId().equals(tbdept.getParentid())).findFirst().get();
                    tbdeptex.setParentid1(tbdept.getParentid());
                    tbdeptex.setParentname1(tbdept1.getSdeptname());
                    tbdeptex.setParentid2(tbdept.getId());
                    tbdeptex.setParentname2(tbdept.getSdeptname());
                }
                if (idepth == 3) {
                    Tbdept tbdept2 = listMap.get(2).stream().filter(d -> d.getId().equals(tbdept.getParentid())).findFirst().get();
                    Tbdept tbdept1 = listMap.get(1).stream().filter(d -> d.getId().equals(tbdept2.getParentid())).findFirst().get();
                    tbdeptex.setParentid1(tbdept2.getParentid());
                    tbdeptex.setParentname1(tbdept1.getSdeptname());
                    tbdeptex.setParentid2(tbdept.getParentid());
                    tbdeptex.setParentname2(tbdept2.getSdeptname());
                    tbdeptex.setParentid3(tbdept.getId());
                    tbdeptex.setParentname3(tbdept.getSdeptname());
                }
                if (idepth == 4) {
                    Tbdept tbdept3 = listMap.get(3).stream().filter(d -> d.getId().equals(tbdept.getParentid())).findFirst().get();
                    Tbdept tbdept2 = listMap.get(2).stream().filter(d -> d.getId().equals(tbdept3.getParentid())).findFirst().get();
                    Tbdept tbdept1 = listMap.get(1).stream().filter(d -> d.getId().equals(tbdept2.getParentid())).findFirst().get();

                    tbdeptex.setParentid1(tbdept2.getParentid());
                    tbdeptex.setParentname1(tbdept1.getSdeptname());
                    tbdeptex.setParentid2(tbdept3.getParentid());
                    tbdeptex.setParentname2(tbdept2.getSdeptname());
                    tbdeptex.setParentid3(tbdept.getParentid());
                    tbdeptex.setParentname3(tbdept3.getSdeptname());
                    tbdeptex.setParentid4(tbdept.getId());
                    tbdeptex.setParentname4(tbdept.getSdeptname());
                }
                if (idepth == 5) {
                    Tbdept tbdept4 = listMap.get(4).stream().filter(d -> d.getId().equals(tbdept.getParentid())).findFirst().get();
                    Tbdept tbdept3 = listMap.get(3).stream().filter(d -> d.getId().equals(tbdept4.getParentid())).findFirst().get();
                    Tbdept tbdept2 = listMap.get(2).stream().filter(d -> d.getId().equals(tbdept3.getParentid())).findFirst().get();
                    Tbdept tbdept1 = listMap.get(1).stream().filter(d -> d.getId().equals(tbdept2.getParentid())).findFirst().get();

                    tbdeptex.setParentid1(tbdept2.getParentid());
                    tbdeptex.setParentname1(tbdept1.getSdeptname());
                    tbdeptex.setParentid2(tbdept3.getParentid());
                    tbdeptex.setParentname2(tbdept2.getSdeptname());
                    tbdeptex.setParentid3(tbdept4.getParentid());
                    tbdeptex.setParentname3(tbdept3.getSdeptname());
                    tbdeptex.setParentid4(tbdept.getParentid());
                    tbdeptex.setParentname4(tbdept4.getSdeptname());
                    tbdeptex.setParentid5(tbdept.getId());
                    tbdeptex.setParentname5(tbdept.getSdeptname());
                }
                resultList.add(tbdeptex);
            }

        }
        saveBatch(resultList);
    }


}
