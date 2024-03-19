package com.hgzp.utils.file;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


/**
 *   导出excel工具类
 * 
 * @param <T> 展示的对象
 * @version  jdk1.7
 *
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class ExportExcelUtil<T> {
	
	//private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * 导出excel工具类.<br/> 
	 * 
	 * @author wangwk 
	 * @param col  导出列
	 * @param title	列标题
	 * @param excelName	 文件名
	 * @param data	数据
	 * @return   excel
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException 
	 * @throws ParseException 
	 * @since JDK 1.7
	 */
	public HSSFWorkbook exportExcel(String col,String title,String excelName,List data) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ParseException{
		
		String[] excelHeader = title.split(",");
		String[] cols = col.split(",");
		HSSFWorkbook wb = new HSSFWorkbook();  
        HSSFSheet sheet = wb.createSheet(excelName);  
        HSSFRow row = sheet.createRow(0);  
        HSSFCellStyle style = wb.createCellStyle();  
        
        //设置字体
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short)15);
        //修改当前行 默认行高  列宽
        //行高
        sheet.setDefaultRowHeightInPoints(20);
        //列款宽
        sheet.setDefaultColumnWidth(10);
        //设置特定单元格的宽度
        sheet.setDefaultColumnWidth(20);
        style.setAlignment(HorizontalAlignment.CENTER);
		style.setFont(font);
        for (int i = 0; i < excelHeader.length; i++){
        	HSSFCell cell = row.createCell(i);    
			cell.setCellStyle(style);
			cell.setCellValue(excelHeader[i]);
			
		}
        Iterator<T> it = data.iterator();
        int index = 0;
		while (it.hasNext()){
			index++;
			row = sheet.createRow(index);
			T t = (T) it.next();
			Field[] fields = t.getClass().getDeclaredFields();
			for (int j = 0; j < cols.length; j++) {
				if(cols[j].toString().indexOf('/')<0) {
					for (int i = 0; i < fields.length; i++) {
						Field field = fields[i];
						String fieldName = field.getName();
						String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
						if (cols[j].equals(fieldName)) {
							HSSFCell cell = row.createCell(j);
							cell.setCellStyle(style);
							Class<? extends Object> tCls = t.getClass();
							Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
							Object value = getMethod.invoke(t, new Object[]{});
							String textValue = null;
							if (null == value) {
								textValue = null;
							} else {
								if (value instanceof Date) {
									Date date = (Date) value;
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
									String strDateTmp = sdf.format(date);
									Date dateTmp = sdf.parse(strDateTmp);
									if (date.getTime() == dateTmp.getTime()) {
										textValue = strDateTmp;
									} else {
										sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										textValue = sdf.format(date);
									}
								} else {
									textValue = value.toString();
								}
							}
							if (textValue != null) {
								cell.setCellValue(textValue);
							}
						}
					}
				}
				else{
					String textValue1=null;
					String textValue2=null;
					String[] arrCol=cols[j].toString().split("/");
					for (int i = 0; i < fields.length; i++) {
						Field field = fields[i];
						String fieldName = field.getName();
						String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
						if (arrCol[0].equals(fieldName)) {
							HSSFCell cell = row.createCell(j);
							cell.setCellStyle(style);
							Class<? extends Object> tCls = t.getClass();
							Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
							Object value = getMethod.invoke(t, new Object[]{});
							if (null == value) {
								textValue1 = null;
							} else {
								if (value instanceof Date) {
									Date date = (Date) value;
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
									String strDateTmp = sdf.format(date);
									Date dateTmp = sdf.parse(strDateTmp);
									if (date.getTime() == dateTmp.getTime()) {
										textValue1 = strDateTmp;
									} else {
										sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										textValue1 = sdf.format(date);
									}
								} else {
									textValue1 = value.toString();
								}
							}
							if (textValue1 != null) {
								cell.setCellValue(textValue1);
							}
						}
						if (arrCol[1].equals(fieldName)) {
							HSSFCell cell = row.createCell(j);
							cell.setCellStyle(style);
							Class<? extends Object> tCls = t.getClass();
							Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
							Object value = getMethod.invoke(t, new Object[]{});
							if (null == value) {
								textValue2 = null;
							} else {
								if (value instanceof Date) {
									Date date = (Date) value;
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
									String strDateTmp = sdf.format(date);
									Date dateTmp = sdf.parse(strDateTmp);
									if (date.getTime() == dateTmp.getTime()) {
										textValue2 = strDateTmp;
									} else {
										sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										textValue2 = sdf.format(date);
									}
								} else {
									textValue2 = value.toString();
								}
							}
							if (textValue1 != null) {
								cell.setCellValue(textValue1+"/"+textValue2);
							}
						}
					}

				}
			}
		}
		return wb;
	}
	
}
