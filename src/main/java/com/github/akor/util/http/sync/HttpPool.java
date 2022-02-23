package com.github.akor.util.http.sync;

import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author : AiTao
 * @Create : 2021-12-07 0:09
 * @Description : httpclient连接池
 */
public class HttpPool {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpPool.class);
    // http池化管理器
    private static PoolingHttpClientConnectionManager connectionPool = null;
    public static final int DEFAULT_CONNECT_TIMEOUT = 2000;
    public static final int DEFAULT_REQUEST_TIMEOUT = 1000;
    public static final int DEFAULT_SOCKET_TIMEOUT = 10000;
    public static final int DEFAULT_SOCKET_MAX_TOTAL = 800;
    public static final int DEFAULT_MAX_PER_ROUTE = 400;

    private HttpPool() {
    }

    static {
        setConnectionPool();
    }

    public static void setConnectionPool() {
        SSLContext sslContext = initSSL(false);
        LayeredConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        // 同时支持HTTP和HTPPS
        Registry<ConnectionSocketFactory> registry = RegistryBuilder
                .<ConnectionSocketFactory>create()
                .register("https", sslsf)
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .build();
        // 初始化连接管理器
        connectionPool = new PoolingHttpClientConnectionManager(registry);
        // 最大连接数
        connectionPool.setMaxTotal(DEFAULT_SOCKET_MAX_TOTAL);
        // 最大路由
        connectionPool.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
        // MaxtTotal和DefaultMaxPerRoute的区别：
        // 1、MaxtTotal是整个池子的大小；
        // 2、DefaultMaxPerRoute是根据连接到的主机对MaxTotal的一个细分；比如：
        // MaxtTotal=400 DefaultMaxPerRoute=200
        // 而我只连接到http://www.abc.com时，到这个主机的并发最多只有200；而不是400；
        // 而我连接到http://www.bac.com和http://www.ccd.com时，到每个主机的并发最多只有200；
        // 即加起来是400（但不能超过400）；所以起作用的设置是DefaultMaxPerRoute
        // 初始化httpClient
    }

    public static PoolingHttpClientConnectionManager getConnectionPool() {
        return connectionPool;
    }

    /**
     * 初始化SSL上下文对象
     *
     * @param isDefault 是否默认配置
     * @return SSLContext
     */
    private static SSLContext initSSL(boolean isDefault) {
        SSLContext ssl = null;
        try {
            if (isDefault) {
                ssl = SSLContext.getDefault(); // 默认SSL
            } else {
                ssl = SSLContext.getInstance("TLS"); // 信任所有证书的SSL
                ssl.init(null, new TrustManager[]{new IgnoreTrustManager()}, null);
            }
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            LOGGER.error("初始化SSLContext对象异常", e);
        }
        return ssl;
    }

    /**
     * 加载证书
     *
     * @param keystorePath 路径
     * @param keypwd       密码
     * @return SSLConnectionSocketFactory
     */
    private static SSLConnectionSocketFactory loadCertificate(String keystorePath, String keypwd) {
        File cert = new File(keystorePath);
        if (!cert.exists()) {
            LOGGER.warn("找不到指定的证书, path:{}", cert.getAbsolutePath());
            return null;
        }
        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(cert, keypwd.toCharArray(), new TrustSelfSignedStrategy()).build();
            return new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1"}, null, NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException | CertificateException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取http连接
     *
     * @return CloseableHttpClient
     */
    public static CloseableHttpClient getConnection() {
        return getConnection(HttpConfig.create()
                .setConnectionTimeout(DEFAULT_CONNECT_TIMEOUT)
                .setRequestTimeout(DEFAULT_REQUEST_TIMEOUT)
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
        );
    }

    public static CloseableHttpClient getConnection(HttpConfig config) {
        if (config == null) {
            return getConnection();
        }
        return HttpClients.custom() // 请求配置
                .setConnectionManager(connectionPool) // 设置连接池管理器
                .setDefaultRequestConfig(customConfig(config)) // 设置默认请求配置
                .setDefaultHeaders(convertHeaders(config.getHeaders())) // 设置默认请求头
                .setRetryHandler(new DefaultHttpRequestRetryHandler(config.getRetryCount(), config.isRequestSentRetryEnabled())) // 设置重试次数
//                .setSSLSocketFactory(null)
//                .disableAutomaticRetries() //禁用自动重试，如果此项设置为true，那么将忽略已经设置的RetryHandler
                .build();
    }


    /**
     * 转换请求头
     *
     * @param headers 请求头信息
     * @return List<Header>
     */
    private static List<Header> convertHeaders(Map<String, String> headers) {
        List<Header> list = new ArrayList<>();
        if (headers == null || headers.isEmpty()) {
            return list;
        }
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            list.add(new BasicHeader(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    /**
     * 自定义请求配置参数
     *
     * @param config http配置
     * @return 请求配置对象RequestConfig
     */
    public static RequestConfig customConfig(HttpConfig config) {
        return RequestConfig.custom()
                .setConnectTimeout(config.getConnectionTimeout()) //建立连接的超时时间
                .setConnectionRequestTimeout(config.getRequestTimeout()) //从连接池中获取连接超时时间
                .setSocketTimeout(config.getSocketTimeout()) //socket传输超时时间
                .setStaleConnectionCheckEnabled(config.isStaleConnectionCheckEnabled()) //是否启用检查失效连接
                .setRedirectsEnabled(config.isRedirectsEnabled()) //设置为true 时则仅对 301、302 才返回 true，其余都返回 false
                .build();
    }
}
