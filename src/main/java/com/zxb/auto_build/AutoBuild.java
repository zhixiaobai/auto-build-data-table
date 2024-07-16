package com.zxb.auto_build;

import java.lang.annotation.*;

/**
 * @author Mr.M
 * @date 2024/7/12
 * @Description
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoBuild {
    String TableName() default "";
}
