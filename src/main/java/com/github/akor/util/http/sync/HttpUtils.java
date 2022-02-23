package com.github.akor.util.http.sync;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author : AiTao
 * Create : 2021/4/16 3:15
 * Description : HttpClient工具类
 */
public class HttpUtils {
    public static void main(String[] args) throws IOException, InterruptedException {
        Map<String, String> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("page", "1");
        HttpResponse response = HttpUtils.get("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E5%9F%BA%E6%9D%90%E5%9F%BA%E6%9C%AC%E9%9D%A2&fenlei=256&rsv_pq=90ec3a6a000ab141&rsv_t=854eV5bMM0TxVxXw8fXP%2FA7cbk%2BS9QD87GJ1HCdx3V9osjuoxpV0FeCMxVA&rqlang=cn&rsv_enter=0&rsv_dl=tb&rsv_sug3=9&rsv_sug1=6&rsv_sug7=101&rsv_btype=i&inputT=985&rsv_sug4=1519", HttpConfig.create().setResponseHandler(HttpUtils.httpResponse()));
        System.out.println(response.getBody());
//        download("风景", 3, SiteType.QIANKUTU);
//        myAnime("柯南", BSearchType.B_STATION_BANG_UMI);
//        Document document = Jsoup.parse(new URL("https://www.nowcoder.com/ta/review-test?page=1"), 30000);
//        System.out.println(document);
//        nowcoder(21);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    public static final String EMPTY = "";

    public static final String CONTENT_TYPE = "Content-Type";

    // CloseableHttpClient对象是线程安全的，所有的线程都可以使用它一起发送http请求
    private static CloseableHttpClient httpclient = null;

    private static CloseableHttpResponse response = null;

    private static HttpRequestBase requestBase = null;
    // 当前URL地址使用的协议是否为http协议
    private static boolean isHttps;
    // 请求头信息
    private static final Map<String, String> headers = new HashMap<>();

    private HttpUtils() {

    }

    /**
     * http get请求，无参数
     *
     * @param url 请求URL
     * @return 请求体
     */
    public static String get(String url) {
        HttpResponse response = get(url, HttpConfig.create());
        return response != null ? response.getBody() : EMPTY;
    }

    /**
     * http get请求，有参数
     *
     * @param url    请求URL
     * @param config http请求配置
     * @return 请求体
     */
    public static HttpResponse get(String url, HttpConfig config) {
        // 检测URL地址使用的协议
        checkSSL(url);
        try {
            // 构建URI对象，并创建http GET请求
            requestBase = new HttpGet(buildUri(URLDecoder.decode(url, config.getCharset()), config.getParams()));
            // 添加请求头
            addHeaders(config.getHeaders());
            // 执行get请求
            return execute(config.getResponseHandler());
        } catch (IOException | URISyntaxException e) {
            LOGGER.error("执行get请求异常", e);
        } finally {
            release();//释放资源
        }
        return null;
    }


    /**
     * post请求
     *
     * @param url 请求URL
     * @return 请求体
     */
    public String post(String url) {
        HttpResponse response = post(url, HttpConfig.create());
        return response != null ? response.getBody() : EMPTY;
    }

    /**
     * post请求
     * <p>
     * 处理content-type: application/x-www-form-urlencoded情况
     * </p>
     *
     * @param url    请求URL
     * @param config http请求配置
     * @return 请求体
     */
    public static HttpResponse post(String url, HttpConfig config) {
        // 检测URL地址使用的协议
        checkSSL(url);
        try {
            // 创建Http post请求
            requestBase = new HttpPost(URLDecoder.decode(url, config.getCharset()));
            // 添加传递参数
            addParams((HttpPost) requestBase, config.getParams());
            // 添加请求头
            addHeaders(headers);
            return execute(httpResponse());
        } catch (IOException e) {
            LOGGER.error("执行post请求异常", e);
        } finally {
            release();//释放资源
        }
        return null;
    }

//    /**
//     * post请求
//     *
//     * @param url        请求地址
//     * @param jsonString JSON格式串参数体
//     * @return 请求体
//     */
//    public static String post(String url, String jsonString) {
//        checkSSL(url);
//        try {
//            HttpPost httpPost = new HttpPost(url);
//            addParams(httpPost, jsonString);
//            if (JsonUtils.isJson(jsonString)) {
//                httpPost.setHeader(getContentType("application/json"));
//            }
//            return execute(httpPost, stringResponse());
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            release();
//        }
//        return EMPTY;
//    }

    /**
     * 执行http请求并获取响应结果
     * <p>
     * 通过http连接池中获取连接并执行相应请求
     * </p>
     *
     * @return 响应结果
     */
    private static HttpResponse execute(ResponseHandler<HttpResponse> responseHandler) throws IOException {
        // 初始化Httpclient对象
        initHttpClient();
        return httpclient.execute(requestBase, responseHandler);
    }

    /**
     * 统一响应体
     *
     * @return HttpResponse
     * @see HttpResponse
     */
    public static ResponseHandler<HttpResponse> httpResponse() {
        return httpResponse -> {
            response = (CloseableHttpResponse) httpResponse;
            StatusLine statusLine = response.getStatusLine();
            if (statusLine != null) {
                // 请求状态
                int status = statusLine.getStatusCode();
                // 操作成功
                if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MOVED_PERMANENTLY) {
                    HttpEntity entity = response.getEntity();
                    //初始化请求头
                    initHeaders(response.getAllHeaders());
                    if (entity == null) {
                        LOGGER.warn("响应entity数据为空");
                        return HttpResponse.create().setBody("Response entity data is empty").setStatus(status);
                    }
                    byte[] bytes = EntityUtils.toByteArray(entity);
                    if (bytes == null || bytes.length == 0) {
                        LOGGER.warn("响应体数据为空");
                        return HttpResponse.create().setBody("Response body data is empty!").setStatus(status);
                    }
                    return HttpResponse.create()
                            .setInputStream(new ByteArrayInputStream(bytes))
                            .setHttps(isHttps)
                            .setHeaders(headers)
                            .setUrl(requestBase.getURI().toURL().toString())
                            .setBody(new String(bytes, 0, bytes.length))
                            .setBytes(bytes)
                            .setStatus(status);
                } else {
                    return HttpResponse.create().setBody("response status: " + status).setStatus(status);
                }
            }
            return null;
        };
    }

    /**
     * 字符串响应体
     *
     * @return String
     */
    public static ResponseHandler<String> stringResponse() {
        return httpResponse -> {
            response = (CloseableHttpResponse) httpResponse;
            StatusLine statusLine = response.getStatusLine();
            if (statusLine != null) {
                int status = statusLine.getStatusCode();
                // 操作成功
                if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MOVED_PERMANENTLY) {
                    HttpEntity entity = response.getEntity();
                    // 初始化请求头
                    initHeaders(response.getAllHeaders());
                    if (entity == null) {
                        LOGGER.warn("响应entity数据为空");
                        return "Response entity data is empty";
                    }
                    // 获取请求体信息
                    String body = EntityUtils.toString(entity, Consts.UTF_8);
                    if (body == null || body.isEmpty()) {
                        LOGGER.warn("响应体数据为空");
                        return "Response body data is empty!";
                    }
                    return body;
                } else {
                    return "response status: " + status;
                }
            }
            return null;
        };
    }

    /**
     * 初始化请求头
     *
     * @param headerArr 请求头信息
     */
    private static void initHeaders(Header[] headerArr) {
        if (headerArr == null || headerArr.length == 0) {
            return;
        }
        for (Header header : headerArr) {
            headers.put(header.getName(), header.getValue());
        }
        LOGGER.info("初始化请求头信息成功, headers:{}", headers);
    }

    /**
     * 检测URL地址使用的协议
     *
     * @param url URL地址
     */
    private static void checkSSL(String url) {
        if (url == null || url.isEmpty()) {
            return;
        }
        isHttps = url.startsWith("https");
    }

    /**
     * 初始化Http客户端对象
     */
    private static void initHttpClient() {
        try {
            if (isHttps) {
                //信任所有证书
                SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, (chain, authType) -> true).build();
                SSLConnectionSocketFactory sslcsf = new SSLConnectionSocketFactory(sslContext);
                // 从http连接池中获取客户端连接
                httpclient = HttpPool.getConnection(HttpConfig.create().setSSLConnectionSocketFactory(sslcsf));
            } else {
                httpclient = HttpPool.getConnection();
            }
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            LOGGER.error("初始化http客户端对象异常", e);
        }
    }

    /**
     * 添加请求头
     *
     * @param headers 请求头信息
     */
    private static void addHeaders(Map<String, String> headers) {
        if (headers == null || headers.isEmpty()) { //default
            requestBase.setHeader(HttpResponse.create().getContentType("application/x-www-form-urlencoded"));
        } else {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBase.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 添加参数对象  param1=value1&param2=value2
     *
     * @param httpPost post请求对象
     * @param params   请求参数
     */
    private static void addParams(HttpPost httpPost, Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return;
        }
        // 被UrlEncodedFormEntity实例编码后变为如下内容：param1=value1&param2=value2
        httpPost.setEntity(new UrlEncodedFormEntity(buildNameValuePairs(params), Consts.UTF_8)); //携带参数
    }

    /**
     * 添加参数对象
     *
     * @param httpPost   post请求对象
     * @param jsonString json字符串
     */
    private static void addParams(HttpPost httpPost, String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return;
        }
        httpPost.setEntity(new StringEntity(jsonString, Consts.UTF_8)); //携带参数
    }

    /**
     * 构造键值对对象
     *
     * @param params 请求参数
     * @return 键值对集合
     */
    private static List<NameValuePair> buildNameValuePairs(Map<String, String> params) {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getKey() != null) {
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        return nameValuePairs;
    }

    /**
     * 构建URI对象
     *
     * @param url   请求地址
     * @param param 请求参数
     * @return URI对象
     * @throws URISyntaxException
     */
    private static URI buildUri(String url, Map<String, String> param) throws URISyntaxException {
        // 创建uri
        URIBuilder uriBuilder = new URIBuilder(url);
        // 注入参数
        if (param != null) {
            for (Map.Entry<String, String> entry : param.entrySet()) {
                uriBuilder.addParameter(entry.getKey(), entry.getValue());
            }
        }
        // 获取uri对象
        return uriBuilder.build();
    }

    /**
     * 释放资源到连接池
     */
    public static void release() {
        try {
            if (response != null) {
                EntityUtils.consume(response.getEntity());
            }
        } catch (IOException e) {
            LOGGER.error("释放持有资源异常", e);
        }
    }

    /**
     * 释放资源
     */
    public void close() {
        close(httpclient, response);
    }

    /**
     * 释放资源
     *
     * @param httpclient CloseableHttpClient
     * @param response   CloseableHttpResponse
     */
    private void close(CloseableHttpClient httpclient, CloseableHttpResponse response) {
        try {
            if (response != null) {
                response.close();
            }
            if (httpclient != null) {
                httpclient.close();
            }
        } catch (IOException e) {
            LOGGER.error("关闭连接异常", e);
        }
    }
}