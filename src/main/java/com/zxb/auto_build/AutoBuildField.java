package com.zxb.auto_build;

import java.lang.annotation.*;

/**
 * @author Mr.M
 * @date 2024/7/12
 * @Description
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoBuildField {

    boolean primaryKey() default false;

    String fieldName() default "";

    AutoBuildTableFieldType fieldType() default AutoBuildTableFieldType.DEFAULT;

    int fieldLength() default -1;

    int decimalPoint() default -1;

    boolean isNull() default true;

    String comment() default "";

    String defaultValue() default "";

    boolean exist() default true;
}
