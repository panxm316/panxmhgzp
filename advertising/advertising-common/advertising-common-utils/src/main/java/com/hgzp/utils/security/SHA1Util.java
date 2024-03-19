package com.hgzp.utils.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Formatter;

/**
 * sha1 工具类
 * 
 * @version  jdk1.7
 *
 */
public class SHA1Util {
	
	public static void main(String[] args) throws Exception {
		
		//File f = new File("C:/Users/peij/Desktop/04/文件管理.txt");
//		System.out.println(createSha1(f));
		
		
		
	}
	
	
	/**
	 *  计算文件sha1
	 *   
	 * @author peij 
	 * @param file
	 * @return String sha1串
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	public static String createSha1(File file) throws Exception  {
	    MessageDigest digest = MessageDigest.getInstance("SHA-1");
	    InputStream fis = new FileInputStream(file);
	    int n = 0;
	    byte[] buffer = new byte[8192];
	    while (n != -1) {
	        n = fis.read(buffer);
	        if (n > 0) {
	            digest.update(buffer, 0, n);
	        }
	    }	    
	    fis.close();
	    return byteToHex(digest.digest());
	}
	
	/**
	 * 转16进制
	 * 
	 * @author peij 
	 * @param hash
	 * @return 
	 * @since JDK 1.7
	 */
	private static String byteToHex(final byte[] hash)
	{
	    Formatter formatter = new Formatter();
	    for (byte b : hash)
	    {
	        formatter.format("%02x", b);
	    }
	    String result = formatter.toString();
	    formatter.close();
	    return result;
	}
}
