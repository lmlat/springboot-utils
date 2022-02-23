package com.github.akor.jdk;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @Company 11bee
 * @Author : tao.ai
 * @Create : 2021/09/22 13:13
 * @Description : druid工具类
 */
public class DruidUtils {
    //数据源
    private static DataSource dataSource;

    //加载配置文件
    static {
        try {
            Properties properties = new Properties();
            properties.load(DruidUtils.class.getClassLoader().getResourceAsStream("druid-config.properties"));
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取DataSource
     *
     * @return DataSource
     */
    public static DataSource getDataSource() {
        if (dataSource != null) {
            return dataSource;
        }
        return null;
    }

    /**
     * 获取连接
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * 释放连接
     */
    public static void close(Statement st, Connection conn) {
        close(null, st, conn);
    }

    /**
     * 释放连接
     */
    public static void close(ResultSet rs, Statement st, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
