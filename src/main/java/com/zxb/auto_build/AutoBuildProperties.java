package com.zxb.auto_build;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Mr.M
 * @date 2024/7/12
 * @Description
 */
public class AutoBuildProperties {
    public static String username;
    public static String password;
    public static String url;
    public static String driver;
    public static int maxSize;
    public static int initSize;
    public static long timeOut;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        InputStream inputStream = AutoBuildProperties.class.getClassLoader().getResourceAsStream("AutoBuild.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            username = properties.getProperty("auto-build.username");
            password = properties.getProperty("auto-build.password");
            driver = properties.getProperty("auto-build.driver");
            url = properties.getProperty("auto-build.url");
            maxSize = Integer.parseInt(properties.getProperty("auto-build.maxSize"));
            initSize = Integer.parseInt(properties.getProperty("auto-build.initSize"));
            timeOut = Long.parseLong(properties.getProperty("auto-build.timeOut"));
        } catch (IOException exception) {
            throw new RuntimeException("Properties configuration file load failed");
        }
    }
}
