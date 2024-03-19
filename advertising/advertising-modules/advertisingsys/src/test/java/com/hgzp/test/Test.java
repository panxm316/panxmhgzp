package com.hgzp.test;

import com.hgzp.AdvertisingSysApplication;
import com.hgzp.advertisingsys.service.system.TbdeptServiceI;
import com.hgzp.advertisingsys.service.system.TbdeptexServiceI;
import com.hgzp.mapper.system.TbdeptMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

/**
 * Test
 * 创建人：wangwk
 * 类描述：
 * 创建日期：2023/8/31 9:54
 */
@SpringBootTest(classes = AdvertisingSysApplication.class)
public class Test {

    @Autowired
    TbdeptexServiceI tbdeptexService;
    @Autowired
    TbdeptServiceI tbdeptServiceI;
    @Autowired
    TbdeptMapper tbdeptMapper;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    @org.junit.jupiter.api.Test
    public void test(){
        tbdeptexService.resetDeptEx();
    }

    @org.junit.jupiter.api.Test
    public void importdept() throws Exception {
        File file = new File("D:\\deptemp.xlsx");
        tbdeptServiceI.importDeptAndEmpData(file);
    }

    @org.junit.jupiter.api.Test
    public void testSql(){
//        tbdeptMapper.updateSdeptnameById("newdata", 1696809347203284992L);
//        tbdeptMapper.updateSdeptnameinIds("newdata122", 3, new Long[]{1696809347203284992L, 1699307910481543168L});
//        tbdeptMapper.updateTest();


    }



}
