package com.chanhonlun.builder.utils;

import com.chanhonlun.builder.consts.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties properties;

    public static void init() throws IOException {

        logger.info("start");

        properties = new Properties();

        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(Constants.PROP_FILE_PATH);

        properties.load(input); // load a properties file

        logger.info("end");
    }

    public static String getProperty(String key) {
        return properties.getProperty(key, "property not found");
    }

}
