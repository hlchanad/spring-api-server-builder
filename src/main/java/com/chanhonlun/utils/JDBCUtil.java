package com.chanhonlun.utils;

import com.chanhonlun.consts.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

        connection = DriverManager.getConnection(url, username, password);

        logger.info("end");
    }

    public static Connection getConnection() {
        return connection;
    }
}
