package com.chanhonlun.builder.commandHandlers;

import com.chanhonlun.command.handlers.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreatePojoIntoFilePathHandler implements Handler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(String... arguments) {

        String tableName = arguments[0];
        String filePath  = arguments[1];

        logger.info("tableName: {}", tableName);
        logger.info("filePath : {}", filePath);
    }
}
