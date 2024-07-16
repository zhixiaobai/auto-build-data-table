package com.zxb.auto_build;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Mr.M
 * @date 2024/7/12
 * @Description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({AutoBuildDataTableRegistrar.class})
public @interface EnableAutoBuildDataTable {
    String[] value() default {};

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};

    Class<?>[] defaultConfiguration() default {};
}
