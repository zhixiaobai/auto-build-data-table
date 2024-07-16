package com.zxb.auto_build;

import java.sql.Connection;

/**
 * @author Mr.M
 * @date 2024/7/12
 * @Description
 */
public interface IConnectionPool {
    /**
     * 获取连接
     * @return Connection
     */
    Connection getConnection();

    /**
     * 释放连接
     */
    void freeLocalConnection();

    /**
     * 销毁连接池
     */
    void destroyConnectionPool();
}
