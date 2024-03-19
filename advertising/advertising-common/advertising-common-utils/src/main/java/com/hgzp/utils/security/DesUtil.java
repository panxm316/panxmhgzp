package com.hgzp.utils.security;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**   
 *    
 * 项目名称：hgcb-utils  
 * 类全名:com.hgcb.utils.DesUtil    
 * 类描述：    
 * 创建人：Peij    
 * 创建时间：2018年3月21日 下午4:06:46
 * 方法列表： 
 * 修改历史：   
 * 	 1、修改人：Peij    
 * 		修改时间：2018年3月21日 下午4:06:46    
 * 		修改备注：  
 *   2、
 *
 * @version  jdk1.7
 * 
 */
public class DesUtil {
	private static final String PRIVATE_KEY = "adminAndRoot1234";

	/**
	 * 加密
	 *
	 * @param plainText
	 *            明文
	 * @param privateKey
	 *            私钥
	 * @return
	 */
	public static String encrypt(String plainText, String privateKey) {
		String rulekey = null;
		if (StringUtils.isEmpty(privateKey)) {
			rulekey = PRIVATE_KEY;
		} else {
			rulekey = privateKey;
		}
		try {
			SecretKeySpec key = new SecretKeySpec(rulekey.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 创建密码器
			byte[] byteContent = plainText.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return Base64.encodeBase64String(result);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param encryptText
	 *            密文
	 * @param privateKey
	 *            私钥
	 * @return
	 */
	public static String decrypt(String encryptText, String privateKey) {
		String rulekey = null;
		if (StringUtils.isEmpty(privateKey)) {
			rulekey = PRIVATE_KEY;
		} else {
			rulekey = privateKey;
		}
		try {
			SecretKeySpec key = new SecretKeySpec(rulekey.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(Base64.decodeBase64(encryptText));// 解密
			return new String(result);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}


	public static void main(String[] args) throws Exception {
		//String content = "b0775589ab2a47bb84f83c99b8d042fd##潍坊测试##wftest##13800000002##http://hgcdcxwf.dzng.com/reslib/main/main";
		// 加密
//		String tt4 = encrypt("reslib,cpsn,public,public_page,public_author,public_folder", null);
//		System.out.println(tt4);
		// 解密
		String tt4 = "oVlCf4nwnCWOx7ED6DpEtq34y1Ws/+d0OclIp9oFEvU=";
		String str = decrypt(tt4, null);
		System.out.println(str);

		// System.out.println(Base64.encodeBase64String("admin".getBytes()));
	}
}
