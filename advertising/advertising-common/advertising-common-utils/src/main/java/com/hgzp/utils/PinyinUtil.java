package com.hgzp.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 拼音工具类
 *
 * @version  jdk1.7
 *
 */
public class PinyinUtil {  
   
    /** 
    * 汉字转换位汉语拼音首字母，英文字符不变 
    * 
    * @param chines 汉字 
    * @return 拼音 
    */  
    public static String converterToFirstSpell(String chines){         
        String pinyinName = "";  
        char[] nameChar = chines.toCharArray();  
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();  
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
        for (int i = 0; i < nameChar.length; i++) {  
            if (Character.toString(nameChar[i]).matches("[\\u4E00-\\u9FA5]+")) {  
                try {  
                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0);  
                } catch (BadHanyuPinyinOutputFormatCombination e) {  
                    e.printStackTrace();  
                }  
            }else{  
                pinyinName += nameChar[i];  
            }  
        }  
        return pinyinName;  
    }  
   
    /** 
    * 汉字转换位汉语拼音，英文字符不变 
    * 
    * @param chines 汉字 
    * @return 拼音 
    */  
    public static String converterToSpell(String chines){          
        String pinyinName = "";  
        char[] nameChar = chines.toCharArray();  
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();  
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
        for (int i = 0; i < nameChar.length; i++) {  
            if (Character.toString(nameChar[i]).matches("[\\u4E00-\\u9FA5]+")) {  
                try {  
                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0];  
                } catch (BadHanyuPinyinOutputFormatCombination e) {  
                    e.printStackTrace();  
                }  
            }else{  
                pinyinName += nameChar[i];  
            }  
        }  
        return pinyinName;  
    }  
    
    /**
     *  
     * 汉字转拼音（例：迟晓清-chixq）
     * 
     * @author chixq
     * @param chines
     * @return 
     * @since JDK 1.7
     */
     public static String converterTofirstall(String chines){          
         String pinyinName = "";  
         char[] nameChar = chines.toCharArray();  
         HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();  
         defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
         defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
         for (int i = 0; i < nameChar.length; i++) {  
             if (Character.toString(nameChar[i]).matches("[\\u4E00-\\u9FA5]+") && i == 0) {  
                 try {  
                     pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0];  
                 } catch (BadHanyuPinyinOutputFormatCombination e) {  
                     e.printStackTrace();  
                 }  
             } 
             if (Character.toString(nameChar[i]).matches("[\\u4E00-\\u9FA5]+") && i != 0) {  
                 try {  
                     pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0);  
                 } catch (BadHanyuPinyinOutputFormatCombination e) {  
                     e.printStackTrace();  
                 }  
             } 
         }  
         return pinyinName;  
     }  
      
    public static void main(String[] args) {  
//        System.out.println(converterToSpell(""));  
    }  
}  