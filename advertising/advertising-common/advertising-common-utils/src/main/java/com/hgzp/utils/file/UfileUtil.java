package com.hgzp.utils.file;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hgzp.utils.HttpClientUtils;
import com.hgzp.utils.exception.UfileException;
import com.hgzp.utils.model.ResponseContent;
import com.hgzp.utils.model.UfileResponse;
import com.hgzp.utils.security.SHA1Util;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一文件系统工具类
 *
 * @version jdk1.7
 */
@Component
@RefreshScope
public class UfileUtil {
    private final static Logger logger = LoggerFactory.getLogger(UfileUtil.class);

    @Autowired
    UfileProperties ufileProperties;
    static UfileProperties staticufileProperties;

    public static final String UFILEURL_REPLACE = "@uFileURL@";
    public static final String UFILEDOWN_REPLACE = "@uFileDown@";
    public static final String UWEBURL_REPLACE = "@uWebURL@";
    public static final String UEXTURL_REPLACE = "@uExtURL@";
    public static final String VIDEOURL_REPLACE = "@videoURL@";
    public static final String VIDEOSCREENSHOT_REPLACE = "@videoScreenShot@";

    private static String uFileURL_replace_07 = "@+@";
    private static String uFileDown_replace_07 = "@=@";
    private static String uWebURL_replace_07 = "@-@";

    @PostConstruct
    public void init(){
        UfileUtil.staticufileProperties = ufileProperties;
    }


    public static void checkufileResult(ResponseContent respContent){
        if(!respContent.isHttpScuccess()){
            throw new UfileException("统一文件获取文件信息失败! statusCode:"+respContent.getStatusCode());
        }
        if(StringUtils.isEmpty(respContent.getUTFContent())){
            throw new UfileException("统一文件获取无返回信息! ");
        }
    }

    public static void checkufileResult(UfileResponse response){
        if(!response.isSuccess()){
            String msg = "统一文件请求失败 error_code:%s, error_message:%s";
            msg = String.format(msg, response.getError_code(), response.getError_message());

            throw new UfileException(msg);
        }
    }




    public static MediaInfo getFileInfo(String sha1) throws UfileException {
        MediaInfo mediaInfo = new MediaInfo();

        try {
            ResponseContent urlRespContent = HttpClientUtils.getUrlRespContent(staticufileProperties.getFileInfoUrl() + sha1);

            checkufileResult(urlRespContent);

            String content = urlRespContent.getUTFContent();
            JSONObject data = JSONObject.parseObject(content).getJSONObject("data");
            String mimetype = data.getString("mimetype");
            mediaInfo.setFileSize(data.getString("size"));
            mediaInfo.setHeight(data.getString("height"));
            mediaInfo.setWidth(data.getString("width"));
            mediaInfo.setSuffix(mimetype.substring(mimetype.indexOf("/") + 1));
            return mediaInfo;
        } catch (Exception e) {
            logger.error("ufile error ",e);
            throw new UfileException("统一接口获取文件信息失败:"+e.getMessage());
        }
    }


    /**
     * 方法名称: getVideoTranprogress
     * 方法功能:  获取视频转码进度<br>
     * 接口返回异常时返回-1
     *
     * @param taskId
     * @return java.lang.Integer
     * @author wangwk
     * @date 2020/7/21 10:55
     */
    public static Integer getVideoTranprogress(String taskId) {
        ResponseContent urlRespContent = HttpClientUtils.getUrlRespContent(staticufileProperties.getVideoTranprogress() + taskId);

        checkufileResult(urlRespContent);

        String content = urlRespContent.getUTFContent();
        JSONObject obj = JSONObject.parseObject(content);
        Boolean success = obj.getBoolean("success");
        if (!success) {
            return -1;
        }
        return obj.getJSONObject("result").getInteger("progress");
    }


    /**
     * 根据url以及sha1 码 获取文件内容
     *
     * @param sha1
     * @return Map
     * @throws Exception
     * @author peij
     * @since JDK 1.7
     */
    public static ResponseContent getFileContent(String sha1) throws Exception {
        ResponseContent response = HttpClientUtils.getUrlRespContent(staticufileProperties.getUFileDown() + sha1);
        return response;
    }

    /**
     * 方法名称:getMediaInfo.<br/>
     * 方法功能: 通过sha1 获取统一文件上媒体文件的信息（返回信息较多，需要可以扩充返回值）.<br/>
     *
     * @param sha1
     * @return
     * @throws IOException
     * @author wangwk
     * @since JDK 1.8
     */
    public static MediaInfo getMediaInfo(String sha1) throws IOException {

        MediaInfo mediaInfo = new MediaInfo();

        JSONObject params = new JSONObject();
        params.put("sha1", sha1);
        ResponseContent response = HttpClientUtils.postJson(staticufileProperties.getMediaInfoUrl(), params.toJSONString());

        checkufileResult(response);

        String content = response.getUTFContent();

        JSONObject obj = JSONObject.parseObject(content);
        Boolean success = obj.getBoolean("success");
        if(success != null && success) {
            JSONArray jsonArray = obj.getJSONObject("mediainfo").getJSONArray("streams");
            if (jsonArray!= null && jsonArray.size() > 0) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject o = jsonArray.getJSONObject(i);
                    if (o != null && o.getString("duration") != null) {//20190409  suny 添加因为 flv视频文件找不到该项参数
                        mediaInfo.setDuration(Double.parseDouble(o.getString("duration")) * 1000);
                    }
                }
            } else {
                JSONObject format = obj.getJSONObject("mediainfo").getJSONObject("format");
                if (null != format && null != format.getString("duration")) {
                    mediaInfo.setDuration(Double.parseDouble(format.getString("duration") == "" ? "0" : format.getString("duration")) * 1000);
                }
            }
        }
        return mediaInfo;
    }

    /**
     * 方法名称:videoTransWaterMarkApi.<br/>
     * 方法功能:  统一文件视频加水印，加台标 转码接口.<br/>
     *
     * @param sha1      视频文件sha1
     * @param ticon     台标文件sha1
     * @param tposition 台标位置，从左上角开始，顺时针 0,1,2,3
     * @param wicon     水印文件sha1
     * @param wposition 水印位置，从左上角开始，顺时针 0,1,2,3
     * @param iscode    是否转码 1需要转码 0不需要转码 2强制转码
     * @param mode      0，1，2分别对应流畅，标清，高清
     * @param ispush    是否消息队列推送
     * @return
     * @throws UnsupportedEncodingException
     * @author wangwk
     * @since JDK 1.8
     */
    public static TransResponse videoTransWaterMarkApi(String sha1, String ticon, String tposition, String wicon, String wposition, int iscode, String mode, boolean ispush) throws UnsupportedEncodingException {
        StringBuilder params = new StringBuilder();
        params.append(sha1).append("?");
        // String params =
        // "{0}?ticon={1}&tposition={2}&wicon={3}&wposition={4}&iscode={5}&mode={6}";
        params.append("ex=").append(staticufileProperties.getExchange()).append("&");
        String p1 = "ticon={0}&tposition={1}";
        if (StringUtils.hasText(ticon)) {
            p1 = MessageFormat.format(p1, ticon, tposition);
        } else {
            p1 = MessageFormat.format(p1, "", "");
        }
        params.append(p1).append("&");

        String p2 = "wicon={0}&wposition={1}";
        if (StringUtils.hasText(wicon)) {
            p2 = MessageFormat.format(p2, wicon, wposition);
        } else {
            p2 = MessageFormat.format(p2, "", "");
        }
        params.append(p2).append("&");

        if (iscode == 1) {
            params.append("iscode=1");
        } else if (iscode == 2) {
            params.append("iscode=2");
        } else {
            params.append("iscode=0");
        }

        params.append("&mode=").append(mode);
        params.append("&ispush=").append(ispush);
        params.append("&defaultmode0=").append(staticufileProperties.isDefaultmode0());

        String url = staticufileProperties.getVideoTransWaterMark() + params.toString();

        ResponseContent response = HttpClientUtils.getUrlRespContent(url);

        checkufileResult(response);

        TransResponse result = JSONObject.parseObject(response.getContent(), TransResponse.class);

        return result;
    }

    /**
     * 向文件服务器推送数据
     *
     * @param file
     * @return
     * @throws Exception
     * @author peij
     * @since JDK 1.7
     */
    public static UfileResponse postFile(File file) throws Exception {

        Map<String, Object> paramsMap = new HashMap<>(16);
        String sha1 = SHA1Util.createSha1(file);
        paramsMap.put("ufile_upload", file);
        ResponseContent response = HttpClientUtils.postEntity(staticufileProperties.getUFileURL() + sha1, paramsMap);

        checkufileResult(response);

        UfileResponse fr = JSONObject.parseObject(response.getContent(), UfileResponse.class);

        checkufileResult(fr);

        return fr;
    }

    /**
     * 下载统一文件上的文件
     *
     * @param sha1         文件sha1
     * @param destFileName 文件完整路径(包含文件名)
     * @return
     * @author wangwk
     * @since JDK 1.7
     */
    public static File getFile(String sha1, String destFileName) {
        String url = staticufileProperties.getUFileDown() + sha1;
        File f = HttpClientUtils.downFile(url, destFileName);
        return f;
    }


    /**
     * 方法名称: getStaticUrl
     * 方法功能: 获取带扩展名的文件的统一文件跳转后外网地址
     * @author wangwk
     * @date 2021/11/5 9:02
     * @param sha1  统一文件sha1
     * @param ext  文件扩展名
     * @return  跳转后的url
     */
    public static String getStaticUrl(String sha1, String ext){
       try {
           if (!".".equals(ext.substring(0, 1))) {
               ext = "." + ext;
           }
           String url = staticufileProperties.getUExtURL() + sha1 + ext + "?wanmode=true&apimode=true";
           ResponseContent urlRespContent = HttpClientUtils.getUrlRespContent(url);
           if (urlRespContent == null) {
               return "";
           }
           checkufileResult(urlRespContent);

           JSONObject parseObject = JSONObject.parseObject(urlRespContent.getUTFContent());
           return parseObject.getString("url");
       }catch (Exception e0){
           return  "";
       }
    }




    /**
     * 将读到的文件转成字符串
     *
     * @param txtFile 文件路径
     * @return 文件内容
     * @throws IOException
     * @author peij
     * @since JDK 1.7
     */
    public static String readTxtFileToStr(String txtFile) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(txtFile));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    /**
     * 适用于存储数据库时将 内容中的统一文件地址替换为特殊占位符
     *
     * @param str
     * @return
     * @author wangwk
     * @since JDK 1.7
     */
    public static String replaceUfileUrl(String str) {
        if(str == null){
            return "";
        }

        String r_str = str.replace(staticufileProperties.getUFileURL(), UFILEURL_REPLACE)
                .replace(staticufileProperties.getUFileDown(), UFILEDOWN_REPLACE)
                .replace(staticufileProperties.getUWebURL(), UWEBURL_REPLACE)
                .replace(staticufileProperties.getUExtURL(), UEXTURL_REPLACE)
                .replace(staticufileProperties.getVideoURL(), VIDEOURL_REPLACE)
                .replace(staticufileProperties.getVideoScreenShot(), VIDEOSCREENSHOT_REPLACE);
        return r_str;
    }

    /**
     * 将数据库中的占位符替换回原来的统一文件地址
     *
     * @param str
     * @return
     * @author wangwk
     * @since JDK 1.7
     */
    public static String unreplaceUfileUrl(String str) {
        if(str == null){
            return "";
        }
        String r_str = str.replace(UFILEURL_REPLACE, staticufileProperties.getUFileURL())
                .replace(UFILEDOWN_REPLACE, staticufileProperties.getUFileDown())
                .replace(UWEBURL_REPLACE, staticufileProperties.getUWebURL())
                .replace(UEXTURL_REPLACE, staticufileProperties.getUExtURL())
                .replace(VIDEOURL_REPLACE, staticufileProperties.getVideoURL())
                .replace(uFileURL_replace_07, staticufileProperties.getVideoURL())
                .replace(uFileDown_replace_07, staticufileProperties.getUFileDown())
                .replace(uWebURL_replace_07, staticufileProperties.getUWebURL())
                .replace(VIDEOSCREENSHOT_REPLACE, staticufileProperties.getVideoScreenShot());
        return r_str;
    }

    public static String readTxtFileToStr(String txtFile, String charset) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(txtFile), charset));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    public static Map<String, String> getFileByReadChar(String sha1) throws Exception {

        Map<String, String> map = new HashMap<String, String>();
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(staticufileProperties.getUFileDown() + sha1);
        HttpResponse response = client.execute(request);

        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(resEntity.getContent(), "UTF-8"));

            String result = "";
            char[] cbuf = new char[1024];
            int hasRead = 0;
            while ((hasRead = rd.read(cbuf)) != -1) {
                result += new String(cbuf, 0, hasRead);
            }

            map.put("stautsCode", response.getStatusLine().getStatusCode() + "");
            map.put("mainContent", result);
            return map;
        } else {
            return null;
        }
    }

    /**
     * 从服务器获得一个image输入流
     *
     * @param url2
     * @return
     * @throws IOException
     * @author chixq
     * @since JDK 1.7
     */
    public static InputStream getInputStream(String url2) throws IOException {
        InputStream inputStream = null;
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet get = new HttpGet(url2);
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() != 200) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
        }catch (Exception e){
            logger.error("httpclient get error !!!", e);
        }
        return inputStream;
    }

    /**
     * 从统一文件获取复合稿内容
     *
     * @param url
     * @return
     * @throws IOException
     * @author chixq
     * @since JDK 1.7
     */
    public static String getStoryCommentFromufile(String url)  {
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        String comment = "";
        try {
            is = UfileUtil.getInputStream(url);
            if (is != null) {
                baos = new ByteArrayOutputStream();
                int length = 0;
                byte[] buffer = new byte[1024];
                while ((length = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }
                comment = new String(baos.toByteArray(), "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                    baos.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        return unreplaceUfileUrl(comment);
    }

    /**
     * 方法名称:formatTime.<br/>
     * 方法功能: 将毫秒数转化成时分秒.<br/>
     *
     * @param seconds
     * @return
     * @author wangwk
     * @since JDK 1.8
     */
    public static String formatTime(double seconds) {
        String str = "";
        seconds = seconds / 1000;
        int hours = (int) (seconds / (60 * 60));
        int mins = (int) ((seconds / (60 * 60) - hours) * 60);
        int second = (int) (seconds - hours * 60 * 60 - mins * 60);
        if (hours < 10) {
            str = "0" + hours + ":";
        } else {
            str = hours + ":";
        }

        if (mins < 10) {
            str += "0" + mins + ":";
        } else {
            str += mins + ":";
        }

        if (second < 10) {
            str += "0" + second;
        } else {
            str += second;
        }
        return str;
    }

    /**
     * 方法名称:formatTime.<br/>
     * 方法功能: 将毫秒数转化成时分秒.<br/>
     *
     * @param seconds
     * @return
     * @author chixq
     * @since JDK 1.8
     */
    public static String formatTime(double seconds, String logo) {
        String str = "", mh = ":";
        seconds = seconds / 1000;
        if (StringUtils.hasText(logo)) {
            mh = "'";
        }
        int hours = (int) (seconds / (60 * 60));
        int mins = (int) ((seconds / (60 * 60) - hours) * 60);
        int second = (int) (seconds - hours * 60 * 60 - mins * 60);
        if (hours < 10) {
            str = "0" + hours + mh;
        } else {
            str = hours + mh;
        }

        if (mins < 10) {
            str += "0" + mins + mh;
        } else {
            str += mins + mh;
        }

        if (second < 10) {
            str += "0" + second;
        } else {
            str += second;
        }
        return str;
    }

    public static class TransResponse {
        private boolean success;
        private String msg;
        private String screenshot;
        private String taskid;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getScreenshot() {
            return screenshot;
        }

        public void setScreenshot(String screenshot) {
            this.screenshot = screenshot;
        }

        public String getTaskid() {
            return taskid;
        }

        public void setTaskid(String taskid) {
            this.taskid = taskid;
        }

        @Override
        public String toString() {
            return "TransResponse [success=" + success + ", msg=" + msg + ", screenshot=" + screenshot + "]";
        }

    }

    public static class MediaInfo {
        private double duration; // 时长
        private String width;
        private String height;
        private String fileSize;
        private String suffix;

        public double getDuration() {
            return duration;
        }

        public void setDuration(double duration) {
            this.duration = duration;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getFileSize() {
            return fileSize;
        }

        public void setFileSize(String fileSize) {
            this.fileSize = fileSize;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }

        @Override
        public String toString() {
            return "MediaInfo [duration=" + duration + "]";
        }

    }



    /**
     *方法名称: UpdateFileTemp
     *方法功能: 统一文件转正式文件
     * @author suny
     * @date 2021/4/23 17:12
     * @param url
     * @param param
     * @return com.hgcb.core.page.Json
     */
    public static Map<String, Object> updateTempFile(String url, String param) {
        Map<String, Object> map = new HashMap<>();
        try {
            String urlNameString = url + param;
            ResponseContent urlRespContent = HttpClientUtils.getUrlRespContent(urlNameString);
            String content = urlRespContent.getUTFContent();
            JSONObject jsf = JSONObject.parseObject(content);
            if(jsf == null){
                map.put("msg","文件转正式时失败");
                map.put("success",false);
            }
            if("200".equals(jsf.getString("status_code"))){
                map.put("success",true);
            }else{
                map.put("msg","文件转正式报错:" + jsf.getString("error_message"));
                map.put("success",false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg","发送GET请求出现异常！" + e);
            map.put("success",false);
        }

        return map;
    }

    /**
     * 方法名称: getscreenshotsha1
     * 方法功能: 获取视频的封面图
     * @author wangwk
     * @date 2023/2/1 13:38
     * @param videoSha1 视频sha1
     * @return 封面图sha1
     */
    public static String getscreenshotsha1(String videoSha1) throws Exception {

        String url = staticufileProperties.getGetscreenshotsha1() + videoSha1;
        ResponseContent response = HttpClientUtils.getUrlRespContent(url);
        checkufileResult(response);

        TransResponse result = JSONObject.parseObject(response.getContent(), TransResponse.class);
        return result.getScreenshot();
    }


}
