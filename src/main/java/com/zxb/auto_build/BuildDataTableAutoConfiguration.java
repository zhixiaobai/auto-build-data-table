package com.zxb.auto_build;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mr.M
 * @date 2024/7/12
 * @Description
 */
//@ConditionalOnClass(Boy.class) // 加载到Boy这个类的话，就自动装配
//@EnableConfigurationProperties(BoyProperties.class)
//@Configuration
//@Data
//@ComponentScan
public class BuildDataTableAutoConfiguration {
}
