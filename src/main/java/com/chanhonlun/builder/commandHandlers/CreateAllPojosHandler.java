package com.chanhonlun.builder.commandHandlers;

import com.chanhonlun.builder.consts.OutputPathConstants;
import com.chanhonlun.builder.utils.JDBCUtil;
import com.chanhonlun.builder.utils.TemplatePojoUtil;
import com.chanhonlun.builder.utils.StrUtil;
import com.chanhonlun.command.handlers.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class CreateAllPojosHandler implements Handler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(String... arguments) {

        String packageName = arguments[1];
        String filepath    = arguments.length >= 3 ? arguments[2] : OutputPathConstants.OUTPUT_PATH;

        File directory = new File(filepath);
        directory.mkdir();

        List<String> tableNames = JDBCUtil.getTableNames();
        for (String tableName : tableNames) {

            String filename    = StrUtil.javaName(tableName, true) + ".java";
            logger.info("creating pojo '{}' into filepath: '{}'", filename, filepath);

            try {
                OutputStream fos = new FileOutputStream(filepath + filename);
                TemplatePojoUtil.render(packageName, tableName, fos);
            } catch (FileNotFoundException e) {
                logger.error("fail creating pojo {}, e={}", filename, e);
            }
        }


    }
}
