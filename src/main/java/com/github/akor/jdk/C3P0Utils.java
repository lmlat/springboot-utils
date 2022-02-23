package com.github.akor.jdk;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Company 11bee
 * @Author : tao.ai
 * @Create : 2021/09/22 13:07
 * @Description : c3p0工具类
 * @see com.mchange.v2.c3p0.ComboPooledDataSource
 */
public class C3P0Utils {
    private DataSource dataSource;

    //空参，调用默认配置 <default-config>
    public C3P0Utils() {
        dataSource = new ComboPooledDataSource();
    }

    //有参，指定的参数为 <named-config name="otherc3p0"> 中的name属性名
    public C3P0Utils(String name) {
        dataSource = new ComboPooledDataSource(name);
    }

    /**
     * 获取数据源
     *
     * @return 返回DataSource
     */
    public DataSource getDataSource() {
        if (dataSource != null) {
            return dataSource;
        }
        return null;
    }

    /**
     * 获取连接对象
     *
     * @return 返回Connection
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * 释放连接
     */
    public void close(Statement st, Connection conn) {
        close(null, st, conn);
    }

    public void close(ResultSet rs, Statement st, Connection conn) {
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
