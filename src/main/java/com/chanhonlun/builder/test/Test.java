package com.chanhonlun.builder.test;

import com.chanhonlun.builder.utils.JDBCUtil;
import com.chanhonlun.builder.utils.PropertiesUtil;
import com.chanhonlun.command.utils.CommandUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static void main(String[] args) {

        args = new String[]{
//                "-p", "SHOP_USER", "-n", "com.chanhonlun.server.pojos"
//                "-p", "SHOP_USER", "-n", "com.chanhonlun.server.pojos", "-f", "output/"
//                "-P", "-n", "com.chanhonlun.server.pojos"
//                "-P", "-n", "com.chanhonlun.server.pojos", "-f", "output/"
                "-r"
        };
        logger.info("args: {}", Arrays.toString(args));

        init();

        CommandUtil.parseCommand(args);
    }
}
