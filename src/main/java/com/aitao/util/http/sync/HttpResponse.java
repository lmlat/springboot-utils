package com.aitao.util.http.sync;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

/**
 * @Author : AiTao
 * @Create : 2021-12-09 14:01
 * @Description :
 */
public class HttpResponse {
    private InputStream inputStream;
    private byte[] bytes;
    private String body;
    private int status;
    private String url;
    private Map<String, String> headers;
    private String contentType;
    private boolean isHttps;

    public static HttpResponse create() {
        return new HttpResponse();
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public HttpResponse setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        return this;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public HttpResponse setBytes(byte[] bytes) {
        this.bytes = bytes;
        return this;
    }

    public String getBody() {
        return body;
    }

    public HttpResponse setBody(String body) {
        this.body = body;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public HttpResponse setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public HttpResponse setUrl(String url) {
        this.url = url;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * 获取指定的头信息
     *
     * @param name 头名称
     * @return 头value值
     */
    public String getHeader(String name) {
        if (name == null) {
            throw new NullPointerException("获取头信息name参数为null");
        }
        return headers.get(name);
    }

    public HttpResponse setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    /**
     * 获取默认content-type值
     *
     * @return content-type默认值
     */
    public String getContentTypeOrDefault() {
        return getContentType("application/x-www-form-urlencoded", Consts.UTF_8.displayName());
    }

    public String getContentType(String mimeType, String charset) {
        return ContentType.create(mimeType, charset).toString();
    }

    public Header getContentType(String mimeType) {
        return new BasicHeader("Content-Type", getContentType(mimeType, Consts.UTF_8.displayName()));
    }

    public HttpResponse setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public boolean isHttps() {
        return isHttps;
    }

    public HttpResponse setHttps(boolean isHttps) {
        this.isHttps = isHttps;
        return this;
    }

    public void close() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "inputStream=" + inputStream +
                ", bytes=" + Arrays.toString(bytes) +
                ", body='" + body + '\'' +
                ", status=" + status +
                ", url='" + url + '\'' +
                ", headers=" + headers +
                ", contentType='" + contentType + '\'' +
                ", isHttps=" + isHttps +
                '}';
    }
}
