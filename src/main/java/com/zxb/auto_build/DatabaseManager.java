package com.zxb.auto_build;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

/**
 * @author Mr.M
 * @date 2024/7/16
 * @Description
 */
public class DatabaseManager {
    private Connection connection;
    private final Map<String, List<String>> TABLES_FIELDS_MAP = new HashMap<>();

    public DatabaseManager() {
        // Ensure connection is managed centrally
        this.connection = getConnection();
    }

    public void initialize() throws SQLException {
        loadExistingTablesFields();
    }

    public void destroyConnection() {
        ConnectionPoolImpl.getInstance().destroyConnectionPool();
    }

    private Connection getConnection() {
        return ConnectionPoolImpl.getInstance().getConnection();
    }

    public void processEntityClass(Class<?> clazz, Map<String, Object> attributes) throws SQLException {
        Field[] entityFields = clazz.getDeclaredFields();
        if (attributes == null || attributes.get("TableName") == null) {
            throw new IllegalArgumentException("@AutoBuild TableName cannot be empty");
        }

        String tableName = buildDataTableName(attributes, clazz);
        List<String> tableFields = TABLES_FIELDS_MAP.getOrDefault(tableName, new ArrayList<>());
        autoBuildDataTableInfo(tableName, entityFields, tableFields);
    }

    private void loadExistingTablesFields() throws SQLException {
        String query = ExecSqlConstant.EXEC_DATABASE_TABLE_FIELD;
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                String columnName = resultSet.getString("COLUMN_NAME");
                TABLES_FIELDS_MAP.computeIfAbsent(tableName, k -> new ArrayList<>()).add(columnName);
            }
        }
    }

    private String buildDataTableName(Map<String, Object> attributes, Class<?> clazz) {
        String tableNameKey = "TableName";
        String tableName = attributes.get(tableNameKey).toString();
        if (tableName.isEmpty()) {
            tableName = convertToDatabaseNameFormat(clazz.getSimpleName());
            attributes.put(tableNameKey, tableName);
        }
        return tableName;
    }

    private String buildTableFieldName(String fieldName) {
        return convertToDatabaseNameFormat(fieldName);
    }

    private AutoBuildTableFieldType buildTableFieldType(Field entityField) {
        String fieldTypeName = entityField.getType().getName();
        for (AutoBuildFieldType fieldType : AutoBuildFieldType.values()) {
            if (fieldType.getPacketType().equals(fieldTypeName) || fieldType.getBaseType().equals(fieldTypeName)) {
                return fieldType.getTableFieldType();
            }
        }
        throw new RuntimeException("未找到当前字段：" + entityField.getName() + " 所对应的MySQL值类型");
    }

    private String convertToDatabaseNameFormat(String name) {
        StringBuilder tableName = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            if (Character.isUpperCase(ch)) {
                if (i != 0) {tableName.append("_");}
                tableName.append(Character.toLowerCase(ch));
            } else {
                tableName.append(ch);
            }
        }
        return tableName.toString();
    }

    private void autoBuildDataTableInfo(String tableName, Field[] entityFields, List<String> tableFields) throws SQLException {
        if (!tableFields.isEmpty()) {
            executeUpdateTable(tableName, entityFields, tableFields);
        } else {
            executeCreateTable(tableName, entityFields, tableFields);
        }
    }

    private void executeUpdateTable(String tableName, Field[] entityFields, List<String> tableFields) throws SQLException {
        if (this.connection == null) {
            this.connection = getConnection();
        }
        List<TableField> tableFieldEntities = buildTableField(tableName, entityFields, tableFields);
        Statement statement = this.connection.createStatement();
        for (TableField tableField : tableFieldEntities) {
            statement.addBatch(SqlFieldBuilder.createTableFieldSql(tableField));
        }
        statement.executeBatch();
    }

    private void executeCreateTable(String tableName, Field[] entityFields, List<String> tableFields) throws SQLException {
        if (connection == null) {
            connection = this.getConnection();
        }
        List<TableField> tableFieldEntities = buildTableField(tableName, entityFields, tableFields);
        String tableSql = SqlFieldBuilder.createTableSql(tableName,tableFieldEntities );
        Statement statement = connection.createStatement();
        statement.execute(tableSql);
    }

    private List<TableField> buildTableField(String tableName, Field[] entityFields, List<String> tableFields) {
        List<TableField> tableFieldEntities = new ArrayList<>();
        for (Field entityField : entityFields) {
            String fieldName = "";
            boolean fieldExist = true;
            AutoBuildField autoBuildField = null;
            // 判断是否有AutoBuildField注解
            if (entityField.isAnnotationPresent(AutoBuildField.class)) {
                autoBuildField = entityField.getAnnotation(AutoBuildField.class);
                fieldName = autoBuildField.fieldName();
                fieldExist = autoBuildField.exist();
            }
            fieldName = fieldName.isEmpty() ? buildTableFieldName(entityField.getName()) : fieldName;
            // 表存在 字段不存在情况 新增表字段
            if (!tableFields.contains(fieldName) && fieldExist) {
                TableField tableField = new TableField(tableName);
                if (CommonUtil.notNull(autoBuildField)) {
                    tableField.setPrimaryKey(autoBuildField.primaryKey());
                    tableField.setFieldName(autoBuildField.fieldName().isEmpty() ?
                            fieldName : autoBuildField.fieldName());
                    tableField.setFieldType(autoBuildField.fieldType() == AutoBuildTableFieldType.DEFAULT ?
                            buildTableFieldType(entityField) : autoBuildField.fieldType());
                    tableField.setFieldLength(autoBuildField.fieldLength() == -1 ?
                            tableField.getFieldType().getFieldLength() : autoBuildField.fieldLength());
                    tableField.setDecimalPoint(autoBuildField.decimalPoint() == -1 ?
                            tableField.getFieldType().getDecimalPoint() : autoBuildField.decimalPoint());
                    tableField.setNull(autoBuildField.isNull());
                    tableField.setComment(autoBuildField.comment());
                    tableField.setDefaultValue(autoBuildField.defaultValue());
                } else {
                    tableField.setPrimaryKey(false);
                    tableField.setFieldName(this.buildTableFieldName(fieldName));
                    tableField.setFieldType(this.buildTableFieldType(entityField));
                    tableField.setFieldLength(tableField.getFieldType().getFieldLength());
                    tableField.setDecimalPoint(tableField.getFieldType().getDecimalPoint());
                    tableField.setNull(true);
                    tableField.setComment(fieldName);
                    tableField.setDefaultValue("");
                }
                tableFieldEntities.add(tableField);
            }
        }
        return tableFieldEntities;
    }
}

