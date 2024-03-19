package com.hgzp.utils;

import com.hgzp.utils.model.ResponseContent;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * HTTP工具类，封装HttpClient
 * 来对外提供简化的HTTP请求
 */
public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);


    /**
     * 使用Get方式 根据URL地址，获取ResponseContent对象
     *
     * @param url 完整的URL地址
     * @return ResponseContent 如果发生异常则返回null，否则返回ResponseContent对象
     * @throws Exception
     */
    public static ResponseContent getUrlRespContent(String url) {
        return getUrlRespContent(url, null);
    }

    /**
     * 使用Get方式 根据URL地址，获取ResponseContent对象
     *
     * @param url    完整的URL地址
     * @param params 参数
     * @return ResponseContent 如果发生异常则返回null，否则返回ResponseContent对象
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static ResponseContent getUrlRespContent(String url, Map<String, String> params) {
        ResponseContent rt = null;
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            url = (null == params ? url : url + "?" + parseParam(params));
            HttpGet get = new HttpGet(url);
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            rt = new ResponseContent();
            rt.setStatusCode(response.getStatusLine().getStatusCode());
            getResponseContent(entity, rt);
        } catch (Exception e) {
            logger.error("httpclient get error !!!", e);
        }
        return rt;
    }

    public static ResponseContent getUrlRespContent(String url, Map<String, String> params, Map<String, String> headers) {
        ResponseContent rt = null;
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            url = (null == params ? url : url + "?" + parseParam(params));
            HttpGet get = new HttpGet(url);

            if (headers != null) {
                headers.forEach(get::addHeader);
            }

            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            rt = new ResponseContent();
            rt.setStatusCode(response.getStatusLine().getStatusCode());
            getResponseContent(entity, rt);
        } catch (Exception e) {
            logger.error("httpclient get error !!!", e);
        }
        return rt;
    }

    /**
     * 文件下载
     *
     * @param url          下载地址
     * @param destFileName 目标位置(需要包含文件名)
     * @return
     * @author wangwk
     * @since JDK 1.7
     */
    public static File downFile(String url, String destFileName) {
        FileOutputStream fout = null;
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet get = new HttpGet(url);
            get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36");
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() != 200) { //20190610  by suny  文件下载失败判断
                return null;
            }
            HttpEntity entity = response.getEntity();
            InputStream in = entity.getContent();
            File file = new File(destFileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fout = new FileOutputStream(file);
            byte[] buff = new byte[4096];
            int hasRead = -1;
            while ((hasRead = in.read(buff)) != -1) {
                fout.write(buff, 0, hasRead);
            }
            fout.flush();
            return file;
        } catch (Exception e) {
            logger.error("httpclient get error !!!", e);
        } finally {
            try {
                if (fout != null) {
                    fout.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 上传文件（包括图片）
     *
     * @param url       请求URL
     * @param paramsMap 参数和值
     * @return
     */
    public static ResponseContent postEntity(String url, Map<String, Object> paramsMap) {
        CloseableHttpClient client = HttpClients.createDefault();
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        ResponseContent rt = null;
        try {
            HttpPost httppost = new HttpPost(url);
            //构造参数
            if (paramsMap != null) {
                buildPostParams(paramsMap, builder);
            }
            HttpEntity reqEntity = builder.build();
            httppost.setEntity(reqEntity);
            HttpResponse response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            //构造返回值
            rt = new ResponseContent();
            rt.setStatusCode(response.getStatusLine().getStatusCode());
            getResponseContent(entity, rt);
        } catch (Exception e) {
            logger.error("httpclient post error !!!", e);
        }
        return rt;
    }

    public static ResponseContent postUrlForm(String url, Map<String, Object> params) {
        return postUrlForm(url, params, null);
    }

    public static ResponseContent postUrlForm(String url, Map<String, Object> params, Map<String, String> headers) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        ResponseContent rt = new ResponseContent();

        try {

            // 设置请求的参数
            List<NameValuePair> list = new ArrayList<>();
            for (String key : params.keySet()) {
                list.add(new BasicNameValuePair(key, String.valueOf(params.get(key))));
            }
            if (headers != null) {
                headers.forEach(httpPost::addHeader);
            }
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "utf-8");


            httpPost.setEntity(urlEncodedFormEntity);
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            rt.setStatusCode(response.getStatusLine().getStatusCode());
            getResponseContent(entity, rt);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rt;
    }


    private static void getResponseContent(HttpEntity entity, ResponseContent ret) throws IOException {
        Header enHeader = entity.getContentEncoding();
        if (enHeader != null) {
            String charset = enHeader.getValue().toLowerCase();
            ret.setEncoding(charset);
        }
        Header contenttype = entity.getContentType();
        if (contenttype != null) {
            try {
                HeaderElement[] hes = contenttype.getElements();
                if (hes != null && hes.length > 0) {
                    ret.setContentType(hes[0].getName());
                }
            } catch (Exception e) {
            }
        }

        Header contenttypestr = entity.getContentType();
        if (contenttypestr != null) {
            ret.setContentTypeString(contenttypestr.getValue());
        }
        ret.setContentBytes(EntityUtils.toByteArray(entity));
    }


    /**
     * 转换参数列表
     *
     * @param params
     * @return
     */
    private static String parseParam(Map<String, String> params) {
        if (null == params || params.isEmpty()) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (String key : params.keySet()) {
            sb.append(key + "=" + params.get(key) + "&");
        }
        return sb.substring(0, sb.length() - 1);
    }


    private static void buildPostParams(Map<String, Object> paramsMap, MultipartEntityBuilder builder) {
        //构造参数
        Iterator<String> iterator = paramsMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = paramsMap.get(key);
            if (value instanceof File) {
                FileBody fileBody = new FileBody((File) value);
                builder.addPart(key, fileBody);
            } else if (value instanceof byte[]) {
                byte[] byteVlue = (byte[]) value;
                ByteArrayBody byteArrayBody = new ByteArrayBody(byteVlue, key);
                builder.addPart(key, byteArrayBody);
            } else {
                if (value != null && !"".equals(value)) {
                    StringBody stringBody = new StringBody(String.valueOf(value), ContentType.create("text/plain", "UTF-8"));
                    builder.addPart(key, stringBody);
                } else {
                    StringBody stringBody = new StringBody(String.valueOf(value), ContentType.create("text/plain", "UTF-8"));
                    builder.addPart(key, stringBody);
                }
            }
        }
    }

    public static ResponseContent postJson(String url, String json) {
        return postJson(url, json, null);
    }

    public static ResponseContent postJson(String url, String json, Map<String, String> headers) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        ResponseContent rt = null;
        try {
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5 * 1000).build();
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader("Accept", "application/json");
            if (headers != null) {
                headers.forEach(httpPost::addHeader);
            }
            httpPost.setConfig(requestConfig);
            StringEntity entity = new StringEntity(json, Consts.UTF_8);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity hentity = response.getEntity();
            //构造返回值
            rt = new ResponseContent();
            rt.setStatusCode(response.getStatusLine().getStatusCode());
            getResponseContent(hentity, rt);


        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
            }
            httpPost.releaseConnection();
        }
        return rt;
    }


}