package com.hgzp.utils.file;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Base64;


/**
 * 文件操作工具类 实现文件的创建、删除、复制、压缩、解压以及目录的创建、删除、复制、压缩解压等功能
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

	private static Logger log = LoggerFactory.getLogger(FileUtils.class);

	/**
	 * 根据文件获取文件后缀
	 * 
	 * @param fileName
	 */
	public static String getFileType(File file) {
		return getFileType(file.getName());
	}

	/**
	 * 根据文件名称获取文件后缀
	 * 
	 * @param fileName
	 */
	public static String getFileType(String fileName) {
		return StrUtil.subAfter(fileName, ".", true);
	}

	/**
	 * 根据文件名随机生成uuid文件名称
	 */
	public static String uuidFileName(String fileName) {
		return IdUtil.simpleUUID() + "." + getFileType(fileName);
	}

	/**
	 * 判断文件夹是否存在，如不存在则创建
	 */
	public static void isFolderExitAndCreate(String folderName) {
		File file = new File(folderName);
		if (!file.exists()) {
			file.mkdir();
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static void deleteFile(File file) {
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
		}
	}

	/**
	 * 复制单个文件，如果目标文件存在，则不覆盖
	 * 
	 * @param srcFileName
	 *            待复制的文件名
	 * @param descFileName
	 *            目标文件名
	 * @return 如果复制成功，则返回true，否则返回false
	 */
	public static boolean copyFile(String srcFileName, String descFileName) {
		return FileUtils.copyFileCover(srcFileName, descFileName, false);
	}

	/**
	 * 复制单个文件
	 * 
	 * @param srcFileName
	 *            待复制的文件名
	 * @param descFileName
	 *            目标文件名
	 * @param coverlay
	 *            如果目标文件已存在，是否覆盖
	 * @return 如果复制成功，则返回true，否则返回false
	 */
	public static boolean copyFileCover(String srcFileName, String descFileName, boolean coverlay) {
		File srcFile = new File(srcFileName);
		// 判断源文件是否存在
		if (!srcFile.exists()) {
			log.debug("复制文件失败，源文件 " + srcFileName + " 不存在!");
			return false;
		}
		// 判断源文件是否是合法的文件
		else if (!srcFile.isFile()) {
			log.debug("复制文件失败，" + srcFileName + " 不是一个文件!");
			return false;
		}
		File descFile = new File(descFileName);
		// 判断目标文件是否存在
		if (descFile.exists()) {
			// 如果目标文件存在，并且允许覆盖
			if (coverlay) {
				log.debug("目标文件已存在，准备删除!");
				if (!FileUtils.delFile(descFileName)) {
					log.debug("删除目标文件 " + descFileName + " 失败!");
					return false;
				}
			} else {
				log.debug("复制文件失败，目标文件 " + descFileName + " 已存在!");
				return false;
			}
		} else {
			if (!descFile.getParentFile().exists()) {
				// 如果目标文件所在的目录不存在，则创建目录
				log.debug("目标文件所在的目录不存在，创建目录!");
				// 创建目标文件所在的目录
				if (!descFile.getParentFile().mkdirs()) {
					log.debug("创建目标文件所在的目录失败!");
					return false;
				}
			}
		}

		// 准备复制文件
		// 读取的位数
		int readByte = 0;
		InputStream ins = null;
		OutputStream outs = null;
		try {
			// 打开源文件
			ins = new FileInputStream(srcFile);
			// 打开目标文件的输出流
			outs = new FileOutputStream(descFile);
			byte[] buf = new byte[1024];
			// 一次读取1024个字节，当readByte为-1时表示文件已经读取完毕
			while ((readByte = ins.read(buf)) != -1) {
				// 将读取的字节流写入到输出流
				outs.write(buf, 0, readByte);
			}
			log.debug("复制单个文件 " + srcFileName + " 到" + descFileName + "成功!");
			return true;
		} catch (Exception e) {
			log.debug("复制文件失败：" + e.getMessage());
			return false;
		} finally {
			// 关闭输入输出流，首先关闭输出流，然后再关闭输入流
			if (outs != null) {
				try {
					outs.close();
				} catch (IOException oute) {
					oute.printStackTrace();
				}
			}
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException ine) {
					ine.printStackTrace();
				}
			}
		}
	}

	/**
	 * 复制整个目录的内容，如果目标目录存在，则不覆盖
	 * 
	 * @param srcDirName
	 *            源目录名
	 * @param descDirName
	 *            目标目录名
	 * @return 如果复制成功返回true，否则返回false
	 */
	public static boolean copyDirectory(String srcDirName, String descDirName) {
		return FileUtils.copyDirectoryCover(srcDirName, descDirName, false);
	}

	/**
	 * 复制整个目录的内容
	 * 
	 * @param srcDirName
	 *            源目录名
	 * @param descDirName
	 *            目标目录名
	 * @param coverlay
	 *            如果目标目录存在，是否覆盖
	 * @return 如果复制成功返回true，否则返回false
	 */
	public static boolean copyDirectoryCover(String srcDirName, String descDirName, boolean coverlay) {
		File srcDir = new File(srcDirName);
		// 判断源目录是否存在
		if (!srcDir.exists()) {
			log.debug("复制目录失败，源目录 " + srcDirName + " 不存在!");
			return false;
		}
		// 判断源目录是否是目录
		else if (!srcDir.isDirectory()) {
			log.debug("复制目录失败，" + srcDirName + " 不是一个目录!");
			return false;
		}
		// 如果目标文件夹名不以文件分隔符结尾，自动添加文件分隔符
		String descDirNames = descDirName;
		if (!descDirNames.endsWith(File.separator)) {
			descDirNames = descDirNames + File.separator;
		}
		File descDir = new File(descDirNames);
		// 如果目标文件夹存在
		if (descDir.exists()) {
			if (coverlay) {
				// 允许覆盖目标目录
				log.debug("目标目录已存在，准备删除!");
				if (!FileUtils.delFile(descDirNames)) {
					log.debug("删除目录 " + descDirNames + " 失败!");
					return false;
				}
			} else {
				log.debug("目标目录复制失败，目标目录 " + descDirNames + " 已存在!");
				return false;
			}
		} else {
			// 创建目标目录
			log.debug("目标目录不存在，准备创建!");
			if (!descDir.mkdirs()) {
				log.debug("创建目标目录失败!");
				return false;
			}

		}

		boolean flag = true;
		// 列出源目录下的所有文件名和子目录名
		File[] files = srcDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 如果是一个单个文件，则直接复制
			if (files[i].isFile()) {
				flag = FileUtils.copyFile(files[i].getAbsolutePath(), descDirName + files[i].getName());
				// 如果拷贝文件失败，则退出循环
				if (!flag) {
					break;
				}
			}
			// 如果是子目录，则继续复制目录
			if (files[i].isDirectory()) {
				flag = FileUtils.copyDirectory(files[i].getAbsolutePath(), descDirName + files[i].getName());
				// 如果拷贝目录失败，则退出循环
				if (!flag) {
					break;
				}
			}
		}

		if (!flag) {
			log.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 失败!");
			return false;
		}
		log.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 成功!");
		return true;

	}

	/**
	 * 删除文件，可以删除单个文件或文件夹
	 * 
	 * @param fileName
	 *            被删除的文件名
	 * @return 如果删除成功，则返回true，否是返回false
	 */
	public static boolean delFile(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			log.debug(fileName + " 文件不存在!");
			return true;
		} else {
			if (file.isFile()) {
				return FileUtils.deleteFile(fileName);
			} else {
				return FileUtils.deleteDirectory(fileName);
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param fileName
	 *            被删除的文件名
	 * @return 如果删除成功，则返回true，否则返回false
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				log.debug("删除单个文件 " + fileName + " 成功!");
				return true;
			} else {
				log.debug("删除单个文件 " + fileName + " 失败!");
				return false;
			}
		} else {
			log.debug(fileName + " 文件不存在!");
			return true;
		}
	}

	/**
	 * 删除目录及目录下的文件
	 * 
	 * @param dirName
	 *            被删除的目录所在的文件路径
	 * @return 如果目录删除成功，则返回true，否则返回false
	 */
	public static boolean deleteDirectory(String dirName) {
		String dirNames = dirName;
		if (!dirNames.endsWith(File.separator)) {
			dirNames = dirNames + File.separator;
		}
		File dirFile = new File(dirNames);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			log.debug(dirNames + " 目录不存在!");
			return true;
		}
		boolean flag = true;
		// 列出全部文件及子目录
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = FileUtils.deleteFile(files[i].getAbsolutePath());
				// 如果删除文件失败，则退出循环
				if (!flag) {
					break;
				}
			}
			// 删除子目录
			else if (files[i].isDirectory()) {
				flag = FileUtils.deleteDirectory(files[i].getAbsolutePath());
				// 如果删除子目录失败，则退出循环
				if (!flag) {
					break;
				}
			}
		}

		if (!flag) {
			log.debug("删除目录失败!");
			return false;
		}
		// 删除当前目录
		if (dirFile.delete()) {
			log.debug("删除目录 " + dirName + " 成功!");
			return true;
		} else {
			log.debug("删除目录 " + dirName + " 失败!");
			return false;
		}

	}

	/**
	 * 创建单个文件
	 * 
	 * @param descFileName
	 *            文件名，包含路径
	 * @return 如果创建成功，则返回true，否则返回false
	 */
	public static boolean createFile(String descFileName) {
		File file = new File(descFileName);
		if (file.exists()) {
			log.debug("文件 " + descFileName + " 已存在!");
			return false;
		}
		if (descFileName.endsWith(File.separator)) {
			log.debug(descFileName + " 为目录，不能创建目录!");
			return false;
		}
		if (!file.getParentFile().exists()) {
			// 如果文件所在的目录不存在，则创建目录
			if (!file.getParentFile().mkdirs()) {
				log.debug("创建文件所在的目录失败!");
				return false;
			}
		}

		// 创建文件
		try {
			if (file.createNewFile()) {
				log.debug(descFileName + " 文件创建成功!");
				return true;
			} else {
				log.debug(descFileName + " 文件创建失败!");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(descFileName + " 文件创建失败!");
			return false;
		}

	}

	/**
	 * 创建目录
	 * 
	 * @param descDirName
	 *            目录名,包含路径
	 * @return 如果创建成功，则返回true，否则返回false
	 */
	public static boolean createDirectory(String descDirName) {
		String descDirNames = descDirName;
		if (!descDirNames.endsWith(File.separator)) {
			descDirNames = descDirNames + File.separator;
		}
		File descDir = new File(descDirNames);
		if (descDir.exists()) {
			log.debug("目录 " + descDirNames + " 已存在!");
			return false;
		}
		// 创建目录
		if (descDir.mkdirs()) {
			log.debug("目录 " + descDirNames + " 创建成功!");
			return true;
		} else {
			log.debug("目录 " + descDirNames + " 创建失败!");
			return false;
		}

	}

	

	

	

	

	

	/**
	 * 将内容写入文件
	 * 
	 * @param content
	 * @param filePath
	 */
	public static void writeFile(String content, String filePath) {
		try {
			if (FileUtils.createFile(filePath)) {
				// FileWriter fileWriter = new FileWriter(filePath, true);
				// BufferedWriter bufferedWriter = new
				// BufferedWriter(fileWriter);
				// 避免中文乱码
				OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8");
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				bufferedWriter.write(content);
				bufferedWriter.close();
				fileWriter.close();
			} else {
				log.info("生成失败，文件已存在！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 追加文件：使用RandomAccessFile
	 * 
	 * @param fileName
	 *            文件名
	 * @param content
	 *            追加的内容
	 */
	public static void appendFile(String filePath, String content) {
		RandomAccessFile randomFile = null;
		try {
			// 打开一个随机访问文件流，按读写方式
			randomFile = new RandomAccessFile(filePath, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			randomFile.writeUTF(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (randomFile != null) {
				try {
					randomFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static File base64ToImage(String imgStr,String imgFilePath) {

		if (StrUtil.isEmpty(imgStr)){ // 图像数据为空

			return null;
		}
		try {
			// Base64解码
			byte[] b = Base64.getDecoder().decode(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}

			File f = new File(imgFilePath);
			OutputStream out = new FileOutputStream(f);
			out.write(b);
			out.flush();
			out.close();

			return f;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static void main(String[] args) {
		String b64="/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAYEBQYFBAYGBQYHBwYIChAKCgkJChQODwwQFxQYGBcUFhYaHSUfGhsjHBYWICwgIyYnKSopGR8tMC0oMCUoKSj/2wBDAQcHBwoIChMKChMoGhYaKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCj/wAARCAA4AC0DASIAAhEBAxEB/8QAHAAAAgIDAQEAAAAAAAAAAAAABgcABQIDBAEI/8QANBAAAQMCBQIEBAMJAAAAAAAAAQIDBAURAAYSITETQQcUIlFhcYGhFZGxIyUyRFKzwdHw/8QAGAEAAwEBAAAAAAAAAAAAAAAABAUGAwD/xAArEQABAwIFAgQHAAAAAAAAAAABAgMRAAQFEiFBURMxI2GRoSKBksHh8PH/2gAMAwEAAhEDEQA/AAuoZMaUwxKQXGUPawlDZuPTte3zPHwxvynkhMypP0x+Zo8+2iOlQaOpJ6jarlJPHoPfDQyzTo9XdosN8dRlxchJsSL2JAIt8RguPhtCjIc8oZTDqjfqJWQsKHBCh7fC2KFy+Ww2hspB01kmZk86bCl4BUSZj9FJKteBmaYri/wtdPnNJOlRS7pWDY39Kh8MVk6iVal1tuA5UZ0TRFYLjLSlNHVoF79+dR+uH81CzPSXfK0+sJmMlJ2no6hA9jsSeTuSLA8Yp8yUqpv5geqEyFEejFhDq3ukFaVgC6dXNr329jjrDEC5cJ6ydBtHl6Vo4k5DlOtJ7O8lT8DqvtRzLUtKuqlhCSog8mw3vvfGFN8REQYjbc2mtrcIvrjgNhW5G6eL7DB/Ljom+JMBkxWYyFLc/YoCShIDKz227Y5a7lrKciUlySy2ytSbgNktgi53sPjffDjEn7M5GrhomRmkbSTptxzQ1tnKM00SeFsgv1jLhVuoxlOqPuStdz+Zw9FJC3QeLffHzx4OzGGKjRnZjqGmmqX61rUEhJKzbc/MYd6cx0dps/vSALcXkJ/3hJjDJL/hgkR9zWzCgAQrmsOmheaHmnAkpDIUL8XuLfpjlzxHRGyzLW23dw2SCBcm5A/XFQM20RWZ33G6vB06EpOiSi55uOflgS8UfEuCKhSstxmHZTNSeaCpbatmvWkWCbHWeftzgBlCkuJWrsCJrVRGUgd6HZS3G/EGO60HApJd19U3VYsrFyfe5++CzLNIaq0V9TzbSyy70xqSSQNKVdiP6jha1KqNUPNbylNvKiJ1x0yVJ9KVE2Tq4Ivb22vg+y5W4sKE4zJmPxXC5rIZSTq9KRcn6YcYo6jqBI1hKR7k0IwlfTSO00iM816XTpbDlHlOsNutNk6TupBSFAG9++CnLOdkPxmYsypy4vVWVdSQsLTb2ukJttxtbC0zi51PIpv/ACjH9pONTjZahRFdRCwq5uhV7bJNvpe3zBw1Rh7N3cZHSQClEfT/ACtAcqZHJp1yEuuMTBTXoD0VxwPIlpmltaU6gbaUpNhtbng4tMiNLYdT+KhuZJac1NvOqLl06ipJGodtVrj/ABhIUbNkjK6pDkRlDj8hotpUs+lG4N7d/ljknZrrri0zJEh0CUk6SnUgWBIIB+nbjjE7ido1ht2ppBzRr6j80Xb+IPjMA/On/Jap71ZrPVjmQiWpQX1U+nSd7p2G9ydx8N74WviBXHGpcRuFLslLZ1hDnB+hwPu5mrkCQ1AlvlTS22goSGuo4xsP4Qo3JFgQDtsLWGKisJ8/5ZaBpeSgpdCWg2kqubKFlG5Itc2HHfCtpt4OrfuVAZoiTsBA9oooW6l+GwkkDgVXmsuLU2txptbjaEtpJQFCyU2HIPYY9TXp3XaWohxLYslte6be1sTExUru3QiQdQOBOnnE0AlCSYrRUpAmusrSwWikeoDue5/73x0QnB5Ztp9lWplWpl1KrKG97dxa+/HJOJiYk728cdcU4ruaf2Vg0swqYFWDlRly1ATHFOkEqurcgkAH7JT+WMQspAF7G1tsTEwmWoqMmq9gBCYG1f/Z";

		base64ToImage(b64, "d:\\64.jpg");
	}


}
