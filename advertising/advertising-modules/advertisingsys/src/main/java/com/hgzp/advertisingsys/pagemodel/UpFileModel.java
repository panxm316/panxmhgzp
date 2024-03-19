package com.hgzp.advertisingsys.pagemodel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FileModel
 * 创建人：suny
 * 类描述：上传文件返回对象
 * 创建日期：2023/10/28 16:57
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpFileModel {
    /**
     * 文件统一文件地址
     */
    private String url;
    /**
     * 文件下载url  视频转码播放地址
     */
    private String durl;
    /**
     * 文件格式
     */
    private String sfileformat;

    /**
     * 统一文件ID
     */
    private String sfileid;

    /**
     * 文件大小
     */
    private String sfilesize;

    /**
     * 源文件名
     */
    private String soriginalfile;
    /**
     * 文件格式类型(Photo、Video、Audio、Office、Application)
     */
    private String sfiletype;
}