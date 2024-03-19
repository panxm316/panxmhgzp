package com.hgzp.utils.model;

import lombok.Data;

import java.io.File;
import java.util.List;

/**
 * 文件上传返回的信息
 */
@Data
public class FileInfo {

    private String uid;

    private String fileName;

    private String fileSize;

    private File file;




}
