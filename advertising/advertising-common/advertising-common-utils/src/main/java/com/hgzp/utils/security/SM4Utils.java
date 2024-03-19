package com.hgzp.utils.security;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;

/**
 * @description: 国密sm4 加密工具类
 * @author: wangwk
 * @create：2023/4/10 17:13
 */
public class SM4Utils {


    public static final String KEY = "68677a707e4031323334353637383930";

    public static String decrypt(String data){
        SM4 sm4 = SmUtil.sm4(HexUtil.decodeHex(KEY));
        return sm4.decryptStr(data);
    }

    /**
     * 根据传入的KEY解密
     * author:muyn 230414
     * @param data
     * @param skey
     * @return
     */
    public static String decrypt(String data, String skey){
        SM4 sm4 = SmUtil.sm4(HexUtil.decodeHex(skey));
        return sm4.decryptStr(data);
    }
    public static String encrypt(String data){
        SM4 sm4 = SmUtil.sm4(HexUtil.decodeHex(KEY));
        return sm4.encryptHex(data);
    }


    public static void main(String[] args) {
        String key = HexUtil.encodeHexStr(SmUtil.sm4().getSecretKey().getEncoded());
        SM4 sm4 = SmUtil.sm4(HexUtil.decodeHex(KEY));
//        System.out.println(HexUtil.encodeHexStr( sm4.getSecretKey().getEncoded()));

//        sm4.setIv(KEY.getBytes());
//        String encryptHex = sm4.encryptHex("test中文");
//        System.out.println(encryptHex);
        String decrypt = decrypt("8a42bb5042ccda44cd60036f3a424230");
        System.out.println(decrypt);

//        String text = "0e395deb10f6e8a17e17823e1fd9bd98a1bff1df508b5b8a1efb79ec633d1bb129432ac1b74972dbe97bab04f024e89c";
//        String decrypt = decrypt(text);
//        System.out.println(decrypt);
    }

}
