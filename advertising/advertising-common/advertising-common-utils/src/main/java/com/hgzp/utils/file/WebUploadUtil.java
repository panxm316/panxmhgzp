package com.hgzp.utils.file;

import com.alibaba.nacos.common.utils.NumberUtils;
import com.hgzp.utils.ServletUtils;
import com.hgzp.utils.exception.FileUploadUnFinshException;
import com.hgzp.utils.model.FileInfo;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * WebUploadUtil
 * 创建人：wangwk
 * 类描述：webupload 工具类
 * 创建日期：2023/9/2 15:16
 */
public class WebUploadUtil {


    public static FileInfo upload(HttpServletRequest request) throws IOException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        File destTempFile = null;
        if (isMultipart) {
            //获取表单
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile multipartFile = multipartRequest.getFile("multiFile");

            // 得到所有的表单域，
            String id = multipartRequest.getParameter("uid");
            String fileName = multipartRequest.getParameter("name");
            String fieSize = multipartRequest.getParameter("size");
            String filetype = multipartRequest.getParameter("mtype");

            int chunks = multipartRequest.getParameter("chunks") == null ? 1 : NumberUtils.toInt(multipartRequest.getParameter("chunks"));
            int chunk = NumberUtils.toInt(multipartRequest.getParameter("chunk"));


            String sfileformat = fileName.substring(fileName.lastIndexOf("."));
            // 文件上传路径
            String filePath = ServletUtils.generateTempFilePath(request);

            // 临时目录用来存放所有分片文件
            String tempFileDir = filePath + id;
            File parentFileDir = new File(tempFileDir);
            if (!parentFileDir.exists()) {
                parentFileDir.mkdirs();
            }
            // 分片处理时，前台会多次调用上传接口，每次都会上传文件的一部分到后台
            File tempPartFile = new File(parentFileDir, fileName + "_" + chunk + ".part");
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), tempPartFile);

            // 所有分片都存在才说明整个文件上传完成
            boolean uploadDone = true;
            for (int i = 0; i < chunks; i++) {
                File partFile = new File(parentFileDir, fileName + "_" + i + ".part");
                if (!partFile.exists()) {
                    uploadDone = false;
                }
            }
            // 将所有分片文件合并到一个文件中
            if (uploadDone) {
                // 得到 destTempFile 就是最终的文件
                destTempFile = new File(filePath, id + sfileformat);
                for (int i = 0; i < chunks; i++) {
                    File partFile = new File(parentFileDir, fileName + "_" + i + ".part");
                    FileOutputStream destTempfos = new FileOutputStream(destTempFile, true);
                    // 遍历"所有分片文件"到"最终文件"中
                    FileUtils.copyFile(partFile, destTempfos);
                    destTempfos.close();
                }
                // 删除临时目录中的分片文件
                FileUtils.deleteDirectory(parentFileDir);

                FileInfo fileInfo = new FileInfo();
                fileInfo.setFile(destTempFile);
                fileInfo.setUid(id);
                fileInfo.setFileSize(fieSize);
                fileInfo.setFileName(fileName);
                return fileInfo;
            }else{
                // 临时文件创建失败
                if (chunk == chunks - 1) {
                    FileUtils.deleteDirectory(parentFileDir);
                    throw new FileUploadException("临时文件创建失败！");
                }
                throw new FileUploadUnFinshException("上传中...");
            }

        }else{
            //不符合格式
            throw new FileUploadException("上传参数不正确！");
        }
    }




}
