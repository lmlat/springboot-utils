package com.aitao.util.http.demo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpClient_Utils {
    // 常规get请求
    public static String Getmethod(String url) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse respons1 = null;
        try {
            respons1 = client.execute(get);
        } catch (ClientProtocolException e1) {
            System.out.println("客户端get请求异常");
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        // 切割字符串
        String result = respons1.getStatusLine().toString().split(" ")[1];

        try {
            client.close();// 释放资源
        } catch (IOException e) {
            System.out.println("请求连接无法关闭，关注get方法！");
            e.printStackTrace();
        }
        return result;

    }

    // 常规P0ST请求
    public static String HttpPostWithJson(String url, String json) {

        String returnValue = "这是默认返回值，接口调用失败";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
            // 创建HttpClient对象
            httpClient = HttpClients.createDefault();
            // 创建httpPost对象
            HttpPost httpPost = new HttpPost(url);
            // 给httpPost设置JSON格式的参数
            StringEntity requestEntity = new StringEntity(json, "utf-8");
            requestEntity.setContentEncoding("UTF-8");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(requestEntity);
            // 发送HttpPost请求，获取返回值
            returnValue = httpClient.execute(httpPost, responseHandler); // 调接口获取返回值，用此方法
        } catch (Exception e) {
            System.out.println("请求返回值为空！");
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                System.out.println("请求连接无法关闭，关注post方法！");
                e.printStackTrace();
            }
        }
        // 第五步：处理返回值
        return returnValue;
    }

    // 忽略证书的HTTPS请求 - get
    public static String HttpsGetIgnoreCertification(String url)
            throws NoSuchAlgorithmException, KeyManagementException, ClientProtocolException, IOException {
        // First create a trust manager that won't care.
        X509TrustManager trustManager = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // Don't do anything.
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // Don't do anything.
            }

            public X509Certificate[] getAcceptedIssuers() {
                // Don't do anything.
                return null;
            }

        };
        // 现在将信任管理器放到SSLContext中。
        SSLContext sslcontext = SSLContext.getInstance("SSL");
        sslcontext.init(null, new TrustManager[]{trustManager}, null);
        SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", sf, 443));

        HttpGet httpget = new HttpGet(url);
//        String result = "";
        httpget.setHeader("Content-type", "application/json");
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        EntityUtils.consume(entity);
//        String result1 = response.getStatusLine().toString();
//        String result2 = response.getStatusLine().toString().split(" ")[2];
        String result3 = response.getStatusLine().toString().split(" ")[1];
        return result3;

    }

    // 忽略证书的HTTPS请求 - post
    public static String HttpsPostIgnoreCertification(String url, String requestData)
            throws NoSuchAlgorithmException, KeyManagementException, ClientProtocolException, IOException {
        // First create a trust manager that won't care.
        X509TrustManager trustManager = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // Don't do anything.
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // Don't do anything.
            }

            public X509Certificate[] getAcceptedIssuers() {
                // Don't do anything.
                return null;
            }

        };
        // 现在将信任管理器放到SSLContext中。
        SSLContext sslcontext = SSLContext.getInstance("SSL");
        sslcontext.init(null, new TrustManager[]{trustManager}, null);
        SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", sf, 443));

        HttpPost httpPost = new HttpPost(url);
        StringBuilder result = new StringBuilder();
//            httpPost.setHeader("Authorization", "basic " + "dGNsb3VkYWRtaW46dGNsb3VkMTIz");
        httpPost.setHeader("Content-type", "application/json");
        StringEntity reqEntity;
        // 将请求参数封装成HttpEntity
        reqEntity = new StringEntity(requestData);
        BufferedHttpEntity bhe = new BufferedHttpEntity(reqEntity);
        httpPost.setEntity(bhe);

        HttpResponse response = httpclient.execute(httpPost);
        HttpEntity resEntity = response.getEntity();
        InputStreamReader reader = new InputStreamReader(resEntity.getContent(), StandardCharsets.UTF_8);
        // 取内存资源
        char[] buff = new char[1024];
        int length = 0;
        while ((length = reader.read(buff)) != -1) {
            result.append(new String(buff, 0, length));
        }
        httpclient.close();
        return result.toString();

//            System.out.println(result);
    }

    // 启用HTTPS携带证书GET请求
    public static String HttpsforGet(String url, String keystore_PathFile, String keypwd) throws IOException,
            KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpClient httpClient = null;
        if (url.startsWith("https")) {
//            "E:\\White_testNG\\mock\\mock_https\\isa\\isa.keystor"
            File cert = new File(keystore_PathFile);
            String keystore = keypwd;
            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(cert, keystore.toCharArray(), new TrustSelfSignedStrategy()).build();
            // Allow TLSv1 protocol only
            // Supports RFC 2246: TLS version 1.0 ; may support other versions
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, NoopHostnameVerifier.INSTANCE);
            httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } else {
            httpClient = HttpClients.createDefault();
        }
        try (CloseableHttpClient _httpClient = httpClient; CloseableHttpResponse res = _httpClient.execute(httpGet);) {

            StatusLine sl = res.getStatusLine();
//                System.out.println(sl.toString().split(" ")[1]);
            String result = sl.toString().split(" ")[1];
            /*
             * if (sl != null) { System.out.println(sl.getStatusCode()); StringBuilder
             * builder = new StringBuilder(); try (InputStream is =
             * res.getEntity().getContent(); InputStreamReader isr = new
             * InputStreamReader(is); BufferedReader br = new BufferedReader(isr);) { String
             * line = br.readLine(); while(line != null) { builder.append(line); line =
             * br.readLine(); } System.out.println("响应结果：" + builder.toString());
             */
            return result;
        }
    }

    // 启用HTTPS携带证书post请求
    public static String HttpsforPost(String url, String keystore_PathFile, String keypwd, String json)
            throws ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException,
            KeyStoreException, CertificateException {

        String returnValue = "这是默认返回值，接口调用失败";
        HttpPost httppost = new HttpPost(url);
        CloseableHttpClient httpClient = null;
        if (url.startsWith("https")) {
            File cert = new File(keystore_PathFile);
            String keystore = keypwd;
            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(cert, keystore.toCharArray(), new TrustSelfSignedStrategy()).build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, NoopHostnameVerifier.INSTANCE);
            httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } else {
            httpClient = HttpClients.createDefault();
        }
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try (CloseableHttpClient _httpClient = httpClient; CloseableHttpResponse res = _httpClient.execute(httppost);) {
            StringEntity requestEntity = new StringEntity(json, "utf-8");
            requestEntity.setContentEncoding("UTF-8");
            httppost.setHeader("Content-type", "application/json");
            httppost.setEntity(requestEntity);
            // 发送HttpPost请求，获取返回值
            returnValue = httpClient.execute(httppost, responseHandler); // 调接口获取返回值，用此方法

//                System.out.println(returnValue);

        }
        return returnValue;

    }

}