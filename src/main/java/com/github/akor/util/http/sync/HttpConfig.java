package com.github.akor.util.http.sync;

import org.apache.http.Header;
import org.apache.http.client.ResponseHandler;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author : AiTao
 * @Create : 2021-12-07 15:52
 * @Description :
 */
public class HttpConfig {
    /**
     * 字符编码
     */
    private String charset = "UTF-8";

    /**
     * 连接超时
     */
    private int connectionTimeout = 3000;
    private int socketTimeout = 5000;
    private int requestTimeout = 3000;
    /**
     * 重试次数
     */
    private int retryCount = 3;
    private boolean isRequestSentRetryEnabled = false;
    private boolean isStaleConnectionCheckEnabled = false;
    /**
     * 内容类型默认值
     */
    private String contentType = "application/x-www-form-urlencoded";
    /**
     * SSL连接套接字工厂
     */
    private SSLConnectionSocketFactory factory;
    /**
     * 响应处理器
     */
    private ResponseHandler responseHandler = HttpUtils.httpResponse();
    /**
     * 请求头信息
     */
    private Map<String, String> headers = new HashMap<>();
    /**
     * 请求头参数
     */
    private Map<String, String> params = new HashMap<>();
    /**
     * 是否启用重定向
     */
    private boolean isRedirectsEnabled = true;

    public static HttpConfig create() {
        return new HttpConfig();
    }

    private HttpConfig() {

    }

    /**
     * 请求头数据转换List<Header>
     */
    private List<Header> convertHeader() {
        List<Header> list = new ArrayList<>();
        if (params.isEmpty()) {
            return list;
        }
        for (Map.Entry<String, String> entry : params.entrySet()) {
            list.add(new BasicHeader(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public <T> T getParams(Object obj) {
        if (obj instanceof List) {
            return (T) convertHeader();
        }
        return (T) getParams();
    }

    public HttpConfig addParams(String key, String value) {
        params.put(key, value);
        return this;
    }

    public HttpConfig addParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public HttpConfig addHerders(Header header) {
        return addHerders(header.getName(), header.getValue());
    }

    public HttpConfig addHerders(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public HttpConfig addHerders(List<Header> headers) {
        return addHerders(toHeadersMap(headers));
    }

    private Map<String, String> toHeadersMap(List<Header> headers) {
        if (headers == null || headers.isEmpty()) {
            return this.headers;
        }
        headers.forEach(this::addHerders);
        return this.headers;
    }

    public HttpConfig addHerders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public ResponseHandler getResponseHandler() {
        return responseHandler;
    }

    public HttpConfig setResponseHandler(ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public HttpConfig setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public HttpConfig setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public HttpConfig setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public HttpConfig setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public HttpConfig setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
        return this;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public HttpConfig setRetryCount(int retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    public boolean isRequestSentRetryEnabled() {
        return isRequestSentRetryEnabled;
    }

    public HttpConfig setRequestSentRetryEnabled(boolean requestSentRetryEnabled) {
        this.isRequestSentRetryEnabled = requestSentRetryEnabled;
        return this;
    }

    public SSLConnectionSocketFactory getSSLConnectionSocketFactory() {
        return factory;
    }

    public HttpConfig setSSLConnectionSocketFactory(SSLConnectionSocketFactory factory) {
        this.factory = factory;
        return this;
    }

    public boolean isStaleConnectionCheckEnabled() {
        return isStaleConnectionCheckEnabled;
    }

    public HttpConfig setStaleConnectionCheckEnabled(boolean isStaleConnectionCheckEnabled) {
        this.isStaleConnectionCheckEnabled = isStaleConnectionCheckEnabled;
        return this;
    }

    public boolean isRedirectsEnabled() {
        return isRedirectsEnabled;
    }

    public HttpConfig setRedirectsEnabled(boolean isRedirectsEnabled) {
        this.isRedirectsEnabled = isRedirectsEnabled;
        return this;
    }
}
