package com.zxb.auto_build;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mr.M
 * @date 2024/7/12
 * @Description
 */
public class ConnectionPoolImpl implements IConnectionPool {
    private String username;
    private String password;
    private String url;
    private String driver;

    private Integer maxSize;
    private Integer initSize;
    private Long timeOut;

    private final AtomicInteger totalSize = new AtomicInteger(0);
    private List<Connection> freeConnections = new Vector<>();
    private List<Connection> activeConnections = new Vector<>();
    private final ThreadLocal<Connection> localConnections = new ThreadLocal<Connection>(){
        @Override
        protected Connection initialValue() {
            try {
                return connect();
            } catch (SQLException exception) {
                throw new RuntimeException("ThreadLocal connection pool init failed");
            }
        }

        @Override
        public void remove() {
            // 移除 将连接重新放回空闲集合
            Connection connection = get();
            activeConnections.remove(connection);
            freeConnections.add(connection);
            super.remove();
        }
    };

    /**
     * volatile 防止jvm指令重排
     */
    private static volatile ConnectionPoolImpl instance;

    /**
     * 双重加锁
     * @return ConnectionPoolImpl实例
     */
    public static ConnectionPoolImpl getInstance() {
        if (instance == null) {
            synchronized (ConnectionPoolImpl.class) {
                if (instance == null) {
                    instance = new ConnectionPoolImpl();
                }
            }
        }
        return instance;
    }

    /**
     * 构造方法
     */
    public ConnectionPoolImpl() {
        loadConfig();
        init();
    }

    private void loadConfig() {
        username = AutoBuildProperties.username;
        password = AutoBuildProperties.password;
        driver = AutoBuildProperties.driver;
        url = AutoBuildProperties.url;
        maxSize = AutoBuildProperties.maxSize;
        initSize = AutoBuildProperties.initSize;
        timeOut = AutoBuildProperties.timeOut;
    }

    /**
     * 初始化
     */
    private void init() {
        try {
            for (int i = 0; i < initSize; i++) {
                freeConnections.add(newConnection());
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new RuntimeException("Connection pool init failed");
        }
    }



    @Override
    public synchronized Connection getConnection() {
        // 获取一个连接
        return localConnections.get();
    }

    @Override
    public void freeLocalConnection() {
        // 移除一个连接
        localConnections.remove();
    }

    @Override
    public synchronized void destroyConnectionPool() {
        // 销毁连接池
        try {
            for (Connection connection: freeConnections) {
                connection.close();
            }
            freeConnections = null;
            for (Connection connection: activeConnections) {
                connection.close();
            }
            activeConnections = null;
        } catch (SQLException throwables) {
            throw new RuntimeException("Connection pool destroy failed");
        }
    }

    private synchronized Connection connect() throws SQLException {
        // 判断是否存在空闲连接
        if (!freeConnections.isEmpty()) {
            Connection connection = freeConnections.get(0);
            freeConnections.remove(0);
            if (connection != null && !connection.isClosed()) {
                activeConnections.add(connection);
                return connection;
            } else {
                return connect();
            }
        } else {
            // 判断当前线程池是否饱和
            if (totalSize.get() == maxSize) {
                // 饱和后 等待 释放 再次获取
                try {
                    wait(timeOut);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return connect();
            } else {
                // 如果没有饱和
                Connection connection = newConnection();
                if (connection != null && !connection.isClosed()) {
                    activeConnections.add(connection);
                    return connection;
                } else {
                    throw new SQLException();
                }
            }
        }
    }

    private synchronized Connection newConnection() throws SQLException {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection connection = DriverManager.getConnection(url, username, password);
        totalSize.incrementAndGet();
        return connection;
    }
}
