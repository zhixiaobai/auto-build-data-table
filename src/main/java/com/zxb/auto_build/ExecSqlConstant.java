package com.zxb.auto_build;

/**
 * @author Mr.M
 * @date 2024/7/12
 * @Description
 */
public class ExecSqlConstant {
    public static final String EXEC_DATABASE_TABLE_FIELD = "SELECT TABLE_NAME, COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()";
    public static final String EXEC_JUDGMENT_TABLE_EXIST = "SELECT COUNT(*) AS count FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = ?";
    public static final String EXEC_GET_TABLE_FIELD = "SELECT column_name FROM information_schema.columns WHERE table_name = ?";
}
