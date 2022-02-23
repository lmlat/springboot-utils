package com.aitao.util.http.demo;

import com.aitao.util.http.sync.HttpPool;
import org.apache.http.conn.HttpClientConnectionManager;

public class IdleConnectProcessor implements Runnable {
    private final HttpClientConnectionManager poolManager = HttpPool.getConnectionPool();

    private volatile boolean shutdown;

    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(5000);
                    // 关闭失效的连接
                    poolManager.closeExpiredConnections();
                }
            }
        } catch (InterruptedException ex) {
            // 结束
        }
    }

    /**
     * 关闭清理无效连接的线程
     */
    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
}