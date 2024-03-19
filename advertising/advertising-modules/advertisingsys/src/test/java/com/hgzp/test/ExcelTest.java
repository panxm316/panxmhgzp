package com.hgzp.test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;

import java.util.List;
import java.util.Map;

/**
 * ExcelTest
 * 创建人：wangwk
 * 类描述：
 * 创建日期：2023/9/2 12:42
 */
public class ExcelTest {

    public static void main(String[] args) {

        ExcelReader reader = ExcelUtil.getReader("D:\\deptemp.xlsx");
        List<List<Object>> readAll = reader.read();
        List<Map<String, Object>> maps = reader.readAll();
        List<DeptEmpImportModel> deptEmpImportModels = reader.readAll(DeptEmpImportModel.class);
        System.out.println(deptEmpImportModels);


    }



}
