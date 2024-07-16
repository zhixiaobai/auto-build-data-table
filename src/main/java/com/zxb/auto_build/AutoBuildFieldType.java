package com.zxb.auto_build;

/**
 * @author Mr.M
 * @date 2024/7/15
 * @Description
 */
public enum AutoBuildFieldType {
    /**
     * 字段类型
     */
    STRING("java.lang.String", "", AutoBuildTableFieldType.VARCHAR),
    INTEGER("java.lang.Integer", "int", AutoBuildTableFieldType.INT),
    BYTE("java.lang.Byte", "byte", AutoBuildTableFieldType.TINYINT),
    LONG("java.lang.Long", "long", AutoBuildTableFieldType.BIGINT),
    SHORT("java.lang.Short", "short", AutoBuildTableFieldType.SMALLINT),
    FLOAT("java.lang.Float", "float", AutoBuildTableFieldType.FLOAT),
    DOUBLE("java.lang.Double", "double", AutoBuildTableFieldType.DOUBLE),
    CHARACTER("java.lang.Character", "char", AutoBuildTableFieldType.CHAR),
    BOOLEAN("java.lang.Boolean", "boolean", AutoBuildTableFieldType.TINYINT),
    DATE("java.sql.Date", "", AutoBuildTableFieldType.DATE),
    DECIMAL("java.math.BigDecimal", "", AutoBuildTableFieldType.DECIMAL),
    TIMESTAMP("java.sql.Timestamp", "", AutoBuildTableFieldType.TIMESTAMP),
    TIME("java.sql.Time", "", AutoBuildTableFieldType.TIME);

    private final String packetType;
    private final String baseType;
    private final AutoBuildTableFieldType tableFieldType;

    AutoBuildFieldType(String packetType, String baseType, AutoBuildTableFieldType tableFieldType) {
        this.packetType = packetType;
        this.baseType = baseType;
        this.tableFieldType = tableFieldType;
    }

    public String getPacketType() {
        return packetType;
    }

    public String getBaseType() {
        return baseType;
    }

    public AutoBuildTableFieldType getTableFieldType() {
        return tableFieldType;
    }
}
