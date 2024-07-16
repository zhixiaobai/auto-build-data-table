package com.zxb.auto_build;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mr.M
 * @date 2024/7/16
 * @Description
 */
public class SqlFieldBuilder {
    private static final String CREATE_TABLE = "CREATE TABLE";
    private static final String ALTER_TABLE = "ALTER TABLE";
    private static final String DEFAULT_ENGINE = "ENGINE=InnoDB";
    private static final String DEFAULT_CHARSET = "DEFAULT CHARSET=utf8mb4";
    private static final String ADD = "ADD";
    private static final String SPACE = " ";
    private static final String BACKTICK = "`";
    private static final String NOT_NULL = "NOT NULL";
    private static final String DEFAULT_NULL = "DEFAULT NULL";
    private static final String DEFAULT = "DEFAULT '";
    private static final String PRIMARY_KEY = "PRIMARY KEY";
    private static final String COMMENT = "COMMENT '";
    private static final String CLOSE_BRACKET = ")";
    private static final String OPEN_BRACKET = "(";
    private static final String COMMA = ",";
    private static final String SEMICOLON = ";";
    private static final String SINGLE_QUOTE = "' ";
    private static final String ENTER = "\n";

    public static String createTableSql(String tableName, List<TableField> tableFields) {
        return CREATE_TABLE +
                SPACE +
                BACKTICK +
                tableName +
                BACKTICK +
                SPACE +
                OPEN_BRACKET +
                ENTER +
                tableFields.stream()
                        .map(SqlFieldBuilder::buildFieldDefinition)
                        .collect(Collectors.joining(COMMA + ENTER, "", ENTER)) +
                CLOSE_BRACKET +
                SPACE +
                DEFAULT_ENGINE +
                SPACE +
                DEFAULT_CHARSET +
                SEMICOLON;
    }

    public static String createTableFieldSql(TableField tableField) {
        String fieldDefinition = buildFieldDefinition(tableField);
        return ALTER_TABLE + SPACE + BACKTICK + tableField.getTableName() + BACKTICK + SPACE +
                ADD + SPACE + fieldDefinition;
    }

    private static String buildFieldDefinition(TableField tableField) {
        StringBuilder sb = new StringBuilder();
        appendBaseFieldDefinition(sb, tableField);
        appendConstraints(sb, tableField);
        return sb.toString();
    }

    private static void appendBaseFieldDefinition(StringBuilder sb, TableField field) {
        sb.append(BACKTICK)
                .append(field.getFieldName())
                .append(BACKTICK)
                .append(SPACE)
                .append(field.getFieldType().getTypeName());

        if (field.getFieldLength() > 0) {
            sb.append(OPEN_BRACKET).append(field.getFieldLength());
            if (field.getDecimalPoint() > 0) {
                sb.append(COMMA).append(field.getDecimalPoint());
            }
            sb.append(CLOSE_BRACKET);
        }
        sb.append(SPACE);
    }

    private static void appendConstraints(StringBuilder sb, TableField field) {
        if (!field.getNull() || field.getPrimaryKey()) {
            sb.append(NOT_NULL).append(SPACE);
        }

        appendDefaultValue(sb, field);
        appendComment(sb, field);

        if (field.getPrimaryKey()) {
            sb.append(PRIMARY_KEY);
        }
    }

    private static void appendDefaultValue(StringBuilder sb, TableField field) {
        // Primary keys typically cannot have a default value
        if (field.getPrimaryKey()) {
            return;
        }

        String defaultValue = field.getDefaultValue();
        if (defaultValue != null && !defaultValue.trim().isEmpty()) {
            defaultValue = defaultValue.trim();
            if ("null".equalsIgnoreCase(defaultValue)) {
                sb.append(DEFAULT_NULL);
            } else {
                sb.append(DEFAULT).append(defaultValue).append(SINGLE_QUOTE);
            }
        } else {
            sb.append(DEFAULT_NULL);
        }
        sb.append(SPACE);
    }

    private static void appendComment(StringBuilder sb, TableField field) {
        if (field.getComment() != null && !field.getComment().isEmpty()) {
            sb.append(COMMENT).append(field.getComment()).append(SINGLE_QUOTE);
        }
    }
}


