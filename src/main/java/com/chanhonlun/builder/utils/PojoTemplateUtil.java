package com.chanhonlun.builder.utils;

import com.chanhonlun.builder.consts.TemplatePathConstants;
import com.chanhonlun.builder.models.TableColumn;
import com.chanhonlun.builder.models.TemplateColumn;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.util.*;

public class PojoTemplateUtil {

    private static final Logger logger = LoggerFactory.getLogger(PojoTemplateUtil.class);

    // regex -> Class
    private static final List<Pair<String, Class>> typings = Arrays.asList(
            new ImmutablePair<>("(?i)\\w+: varchar.*", String.class),
            new ImmutablePair<>("(?i)\\w+: (datetime|timestamp).*", Date.class),
            new ImmutablePair<>("(?i)IS_\\w+: number\\(1, 0\\)", Boolean.class),
            new ImmutablePair<>("(?i)\\w+: number\\([1-3], 0\\)", Short.class),
            new ImmutablePair<>("(?i)\\w+: number\\(([4-9]|10), 0\\)", Integer.class),
            new ImmutablePair<>("(?i)\\w+: number\\(([4-9]|10), \\d\\)", Float.class),
            new ImmutablePair<>("(?i)\\w+: number\\((1[1-9]|20), 0\\)", Long.class),
            new ImmutablePair<>("(?i)\\w+: number\\((1[1-9]|20), \\d\\)", Double.class)
    );

    public static List<TemplateColumn> getColumnsForTemplate(List<TableColumn> tableColumns) {

        List<TemplateColumn> templateColumns = new ArrayList<>();

        logger.debug("typings: {}", typings);

        for (TableColumn tableColumn : tableColumns) {

            TemplateColumn templateColumn = new TemplateColumn();
            templateColumn.setIsPK(tableColumn.getIsPK());
            templateColumn.setColumnName(tableColumn.getName());
            templateColumn.setJavaName(StrUtil.javaName(tableColumn.getName(), false));
            templateColumn.setJavaType(getJavaType(tableColumn.getName(), tableColumn.getDataType(), tableColumn.getColumnSize(), tableColumn.getDecimalDigit()));
            templateColumn.setDefaultValue(tableColumn.getDefaultValue());
            templateColumn.setNullable(tableColumn.getNullable());
            templateColumn.setRemarks(tableColumn.getRemarks());

            templateColumns.add(templateColumn);
        }

        return templateColumns;
    }

    public static String render(String packageName, String tableName) {

        Map<String, Object> templateData = templateData(packageName, tableName);

        JtwigTemplate template = JtwigTemplate.classpathTemplate(TemplatePathConstants.RESOURCES_POJO_PATH);
        JtwigModel model = JtwigModel.newModel(templateData);

        return template.render(model);
    }

    public static void render(String packageName, String tableName, OutputStream os) {

        Map<String, Object> templateData = templateData(packageName, tableName);

        JtwigTemplate template = JtwigTemplate.classpathTemplate(TemplatePathConstants.RESOURCES_POJO_PATH);
        JtwigModel model = JtwigModel.newModel(templateData);

        template.render(model, os);
    }

    private static Map<String, Object> templateData(String packageName, String tableName) {
        List<TableColumn> tableColumns = JDBCUtil.getTableColumns(tableName);

        logger.debug("table: {}, columns: {}", tableName, tableColumns);

        List<TemplateColumn> templateColumns = PojoTemplateUtil.getColumnsForTemplate(tableColumns);

        logger.debug("templateColumns: {}", templateColumns);

        Map<String, Object> data = new HashMap<>();
        data.put("packageName", packageName);
        data.put("tableName", tableName);
        data.put("javaTableName", StrUtil.javaName(tableName, true));
        data.put("columns", templateColumns);

        logger.debug("map for template: {}", data);

        return data;
    }

    private static String getJavaType(String columnName, String dataType, Integer columnSize, Integer decimalDigit) {

        for (Pair<String, Class> typing : typings) {

            // eg: IS_DELETE: NUMBER(1, 0)
            String columnNameAndType = String.format("%s: %s(%d, %d)", columnName, dataType, columnSize, decimalDigit);

            logger.debug("comparing {} with regex {}", columnNameAndType, typing.getLeft());

            if (columnNameAndType.matches(typing.getLeft())) {
                return typing.getRight().getCanonicalName();
            }
        }

        return String.class.getCanonicalName();
    }
}
