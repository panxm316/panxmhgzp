package com.hgzp.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.digest.SM3;

/**
 * 基于sm3 的密码加密验证工具类
 *
 * @author wangwk
 */
public class SecurityUtils {

    /**
     * 生成sm3密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        SM3 sm3 = SmUtil.sm3();
        String salt = getSalt();
        sm3.setSalt(salt.getBytes());
        String sm3Password =  sm3.digestHex(password);
        return salt + "$" + sm3Password;
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        String[] split = encodedPassword.split("\\$");
        SM3 sm3 = SmUtil.sm3();
        sm3.setSalt(split[0].getBytes());
        String sm3Password =  sm3.digestHex(rawPassword);
        return sm3Password.equals(split[1]);
    }

    private static String getSalt(){
        return RandomUtil.randomString(5);
    }


    public static void main(String[] args) {
//        String encryptPassword = encryptPassword("123");
//        System.out.println(encryptPassword);

        System.out.println( matchesPassword("123", "0mwkj$c6da515a081123cbdd72f2312d657e7e190183c192f32072534a2f52c7e3703b"));

    }
}
