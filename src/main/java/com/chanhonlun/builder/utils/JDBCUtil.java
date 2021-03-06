package com.chanhonlun.builder.utils;

import com.chanhonlun.builder.consts.Constants;
import com.chanhonlun.builder.models.TableColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JDBCUtil {

    private static final Logger logger = LoggerFactory.getLogger(JDBCUtil.class);

    private static String schema;

    private static Connection connection;

    public static void init() throws SQLException, ClassNotFoundException {

        logger.info("start");

        connection = null;

        String driver   = PropertiesUtil.getProperty(Constants.PROP_DATABASE_DRIVER);
        String url      = PropertiesUtil.getProperty(Constants.PROP_DATABASE_URL);
        String username = PropertiesUtil.getProperty(Constants.PROP_DATABASE_USERNAME);
        String password = PropertiesUtil.getProperty(Constants.PROP_DATABASE_PASSWORD);

        schema = PropertiesUtil.getProperty(Constants.PROP_DATABASE_DEFAULT_SCHEMA);

        Class.forName(driver);

//        connection = DriverManager.getConnection(url, username, password);

        Properties properties = new Properties();
        properties.put("remarksReporting", "true");   // required for Oracle DB for getting remarks
        properties.put("user", username);
        properties.put("password", password);

        connection = DriverManager.getConnection(url, properties);

        logger.info("end");
    }

    public static void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("fail closing connection, e={}", e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static List<String> getTableNames() {

        long start = System.currentTimeMillis();

        List<String> tableNames = new ArrayList<>();

        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, schema, "%", new String[]{ "TABLE" });

            while (resultSet.next()) {
                // the link below indicate columnIndex -> meaning
                // https://docs.oracle.com/javase/8/docs/api/java/sql/DatabaseMetaData.html#getTables-java.lang.String-java.lang.String-java.lang.String-java.lang.String:A-
                tableNames.add(resultSet.getString(3));
            }
        } catch (SQLException e) {
            logger.error("fail getting tables, e={}", e);
        }

        long end = System.currentTimeMillis();

        logger.info("get all table names, duration: {} ms", (end - start));

        return tableNames;
    }

    public static List<String> getPrimaryKeys(String table) {

        long start = System.currentTimeMillis();

        List<String> pks = new ArrayList<>();

        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getPrimaryKeys(null, schema, table);

            while (resultSet.next()) {
                // the link below indicate columnIndex -> meaning
                // https://docs.oracle.com/javase/8/docs/api/java/sql/DatabaseMetaData.html#getPrimaryKeys-java.lang.String-java.lang.String-java.lang.String-
                pks.add(resultSet.getString(4));
            }

        } catch (SQLException e) {
            logger.error("fail getting pks, e={}", e);
        }

        long end = System.currentTimeMillis();

        logger.info("get primary keys for table {}, duration: {} ms", table, (end - start));

        return pks;
    }

    public static List<TableColumn> getTableColumns(String table) {

        long start = System.currentTimeMillis();

        List<String> pkNames = getPrimaryKeys(table);

        List<TableColumn> tableColumns = new ArrayList<>();

        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getColumns(null, schema, table, null);

            while(resultSet.next()) {
                // the link below indicate columnIndex -> meaning
                // https://docs.oracle.com/javase/8/docs/api/java/sql/DatabaseMetaData.html#getColumns-java.lang.String-java.lang.String-java.lang.String-java.lang.String-

                String columnName = resultSet.getString(4);
                Boolean isPK = pkNames.contains(columnName);
                String dataType = resultSet.getString(5);
                String typeName = resultSet.getString(6);
                Integer columnSize = Integer.parseInt(resultSet.getString(7));
                Integer decimalDigit = resultSet.getString(9) != null ? Integer.parseInt(resultSet.getString(9)) : 0;
                Boolean nullable = resultSet.getString(11).equals("1");
                String defaultValue = resultSet.getString(13);
                String remarks = resultSet.getString(12);

                if (typeName.contains("(")) {
                    typeName = typeName.substring(0, typeName.indexOf("("));
                }

                logger.debug("isPK: {}, name: {}, type: {}, typeName: {}, size: {}, decimal: {}, nullable: {}, default: {}, remarks: {}",
                        isPK, columnName, dataType, typeName, columnSize, decimalDigit, nullable, defaultValue, remarks);

                tableColumns.add(new TableColumn(isPK, columnName, typeName, columnSize, decimalDigit, nullable, defaultValue, remarks));
            }

        } catch (SQLException e) {
            logger.error("fail getting tableColumns, e={}", e);
        }

        long end = System.currentTimeMillis();

        logger.info("get metadata for table {}, duration: {} ms", table, (end - start));

        return tableColumns;
    }
}
