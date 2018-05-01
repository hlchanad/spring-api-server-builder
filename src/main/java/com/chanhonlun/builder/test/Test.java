package com.chanhonlun.builder.test;

import com.chanhonlun.command.utils.CommandUtil;
import com.chanhonlun.builder.utils.JDBCUtil;
import com.chanhonlun.builder.utils.PropertiesUtil;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Arrays;

public class Test {

    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    public static void init() {
        try {
            PropertiesUtil.init();
            CommandUtil.init();
            JDBCUtil.init();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void main(String[] args) throws SQLException, ParseException {

        args = new String[]{
                "-p", "SHOP_USER",
//                "-p", "SHOP_USER", "-f", "output"
//                "-P"
//                "-P", "-f", "output"

        };
        logger.info("args: {}", Arrays.toString(args));

        init();

        CommandUtil.parseCommand(args);

//        logger.info("connected");
//
//        logger.info("tableNames: {}", JDBCUtil.getTableNames());
//
//        logger.info("pks for SHOP_USER: {}", JDBCUtil.getPrimaryKeys("SHOP_USER"));
//
//        logger.info("columns for SHOP_USER: {}", JDBCUtil.getTableColumns("SHOP_USER"));
//
//        JDBCUtil.close();
    }
}
