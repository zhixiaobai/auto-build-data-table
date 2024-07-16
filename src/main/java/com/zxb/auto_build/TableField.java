package com.zxb.auto_build;

/**
 * @author Mr.M
 * @date 2024/7/15
 * @Description
 */
public class TableField {
    private String tableName;
    private Boolean primaryKey;
    private String fieldName;
    private AutoBuildTableFieldType fieldType;
    private Integer fieldLength;
    private Integer decimalPoint;
    private Boolean isNull;
    private String comment;
    private String defaultValue;

    public TableField(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Boolean getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public AutoBuildTableFieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(AutoBuildTableFieldType fieldType) {
        this.fieldType = fieldType;
    }

    public Integer getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(Integer fieldLength) {
        this.fieldLength = fieldLength;
    }

    public Integer getDecimalPoint() {
        return decimalPoint;
    }

    public void setDecimalPoint(Integer decimalPoint) {
        this.decimalPoint = decimalPoint;
    }

    public Boolean getNull() {
        return isNull;
    }

    public void setNull(Boolean aNull) {
        isNull = aNull;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
