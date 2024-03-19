package com.hgzp.utils.security;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;

/**
 * 项目名：hgcb-parent
 * 类全名：com.hgcb.utils.RsaUtils
 * 创建人：suny
 * 类描述：SM2 非对称加密
 * 创建日期：2023/4/3 9:28
 */
public class SM2Utils {


    public static final String PRIVATEKEY = "4bc632d8c0816c82568f3fb06c5d55862e3e9a394a430bfb3db621b029805515";
    public static final String PUBLICKEY = "04bc9b314bc317dfe0d7672126815b2e6d9cf150efbb9a933fe1d8b4ef70e9873e6c68dfe57e1d2f1870dcba4413119ad0851fee24da487762189211bc7e7632d0";

    static SM2 sm2 = SmUtil.sm2(PRIVATEKEY, PUBLICKEY);

    /**
     * 私钥解密
     *
     * @param text             待解密的文本
     * @return 解密后的文本
     */
    public static String decryptByPrivateKey( String text)  {
        return sm2.decryptStr(text, KeyType.PrivateKey);
    }


    /**
     * 公钥加密
     *
     * @param text            待加密的文本
     * @return 加密后的文本
     */
    public static String encryptByPublicKey(String text)  {
        return sm2.encryptBase64(text, KeyType.PublicKey);
    }


    public static void main(String[] args) {

        String text = "test中文";
//        String encryptByPrivateKey = encryptByPrivateKey(text);
//        System.out.println("私钥加密后的文本：" + encryptByPrivateKey);
//        String decryptByPublicKey = decryptByPublicKey(encryptByPrivateKey);
//        System.out.println("公钥解密后的文本：" + decryptByPublicKey);
//
        String encryptByPublicKey = "04cd520c0a5bce134f46b6437f0d2d5795c489a2e2bf4f0f2a5daf40744abee19abcd9fef46055b02554b8f96d6134e69f6209bb28e840d24c2c3956cb62556352d3d81155426db5dbbccc0b1c0069a48a58d1809585afc7f5a7f95a89c00cafae6df9";//encryptByPublicKey(text);
        System.out.println("公钥加密后的文本：" + encryptByPublicKey);
        String decryptByPrivateKey = decryptByPrivateKey(encryptByPublicKey);
        System.out.println("私钥解密后的文本：" + decryptByPrivateKey);

//        KeyPair pair = SecureUtil.generateKeyPair("SM2");
//        byte[] privateKey = pair.getPrivate().getEncoded();
//        byte[] publicKey = pair.getPublic().getEncoded();
//        System.out.println(HexUtil.encodeHexStr(privateKey));
//        System.out.println(HexUtil.encodeHexStr(publicKey));

//        SM2 sm2 = SmUtil.sm2(privateKey, publicKey);
//        // 公钥加密，私钥解密
//        String encryptStr = sm2.encryptBase64(text, KeyType.PrivateKey);
//        String decryptStr = StrUtil.utf8Str(sm2.decrypt(encryptStr, KeyType.PublicKey));
//        System.out.println(decryptStr);

    }



}
