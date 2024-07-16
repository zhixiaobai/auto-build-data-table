package com.zxb.auto_build;

/**
 * @author Mr.M
 * @date 2024/7/12
 * @Description
 */
public enum AutoBuildTableFieldType {
    /**
     * 数据库字段枚举类
     */
    DEFAULT("default", 0, 0),
    CHAR("char", 255, 0),
    VARCHAR("varchar", 255, 0),
    BINARY("binary", 255, 0),
    VARBINARY("varbinary", 255, 0),
    TINYBLOB("tinyblob", 255, 0),
    TINYTEXT("tinytext", 255, 0),
    BLOB("blob", 65535, 0),
    TEXT("text", 65535, 0),
    MEDIUMBLOB("mediumblob", 16777215, 0),
    MEDIUMTEXT("mediumtext", 16777215, 0),
//    LONGBLOB("longblob", 4294967295L, 0),
//    LONGTEXT("longtext", 4294967295L, 0),
    ENUM("enum", 65535, 0),
    SET("set", 64, 0),
    BOOL("bool", 0, 0),
    BOOLEAN("boolean", 0, 0),
    TINYINT("tinyint", 4, 0),
    SMALLINT("smallint", 6, 0),
    MEDIUMINT("mediumint", 9, 0),
    INT("int", 11, 0),
    INTEGER("integer", 11, 0),
    BIGINT("bigint", 20, 0),
    FLOAT("float", 10, 1),
    DOUBLE("double", 10, 2),
    DECIMAL("decimal", 10, 2),
    DATE("date", 0, 0),
    DATETIME("datetime", 0, 0),
    TIMESTAMP("timestamp", 0, 0),
    TIME("time", 0, 0),
    YEAR("year", 0, 0);

    private final String typeName;
    private final Integer fieldLength;
    private final Integer decimalPoint;

    AutoBuildTableFieldType(String typeName, Integer fieldLength, Integer decimalPoint) {
        this.typeName = typeName;
        this.fieldLength = fieldLength;
        this.decimalPoint = decimalPoint;
    }

    public String getTypeName() {
        return typeName;
    }

    public Integer getFieldLength() {
        return fieldLength;
    }

    public Integer getDecimalPoint() {
        return decimalPoint;
    }
}
