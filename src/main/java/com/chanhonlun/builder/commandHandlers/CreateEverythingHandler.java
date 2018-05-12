package com.chanhonlun.builder.commandHandlers;

import com.chanhonlun.builder.consts.Constants;
import com.chanhonlun.builder.consts.OutputPathConstants;
import com.chanhonlun.builder.utils.*;
import com.chanhonlun.command.handlers.Handler;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class CreateEverythingHandler implements Handler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(String... arguments) {

        logger.info("Create Everything Handler");

        String outputDirectory = PropertiesUtil.getProperty(Constants.PROP_OUTPUT_DIRECTORY);
        String basicStructureResourcesPath = Constants.PATH_BASIC_STRUCTURE;

        initOutputDirectory(outputDirectory);

        copyBasicStructure(basicStructureResourcesPath, outputDirectory);

        String groupId  = PropertiesUtil.getProperty(Constants.PROP_PROJ_GROUP_ID);
        String artifact = PropertiesUtil.getProperty(Constants.PROP_PROJ_ARTIFACT);

        String basePackageName = groupId + "." + artifact;
        // sample basePath = output/src/main/java/com/chanhonlun/server
        String basePath        = outputDirectory + File.separator + OutputPathConstants.OUTPUT_PATH_SRC_MAIN_JAVA + File.separator + basePackageName.replaceAll("\\.", File.separator);

        new File(basePath).mkdirs();

        for (String tableName : JDBCUtil.getTableNames()) {
            createPojo(tableName, basePath, basePackageName);
        }
    }

    /**
     * delete the folder if exist, then create empty folder
     *
     * @param outputDirectory output directory path
     */
    private void initOutputDirectory(String outputDirectory) {

        try {
            FileUtils.deleteDirectory(new File(outputDirectory));
            logger.debug("delete directory {} success: {}", outputDirectory, true);
        } catch (IOException e) {
            logger.error("fail deleting directory {}", outputDirectory);
            System.exit(-1);
        }
        boolean success = new File(outputDirectory).mkdirs();
        logger.debug("create directory {} success: {}", outputDirectory, success);
    }

    /**
     * copy files/ folders from {@code fromPath} (inside resources) to {@code toPath}
     *
     * @param fromPath a Resources path copy From
     * @param toPath a Normal path copy To
     */
    private void copyBasicStructure(String fromPath, String toPath) {

        String resourcesFromPath = Thread.currentThread().getContextClassLoader().getResource(fromPath).getFile();
        resourcesFromPath = CryptoUtil.urldecode(resourcesFromPath);
        try {
            FileUtils.copyDirectory(
                    new File(resourcesFromPath),
                    new File(toPath));
        } catch (IOException e) {
            logger.error("fail copying directory, e={}", e);
        }
    }

    private void createPojo(String tableName, String basePath, String basePackageName) {

        String pojoPackage  = basePackageName + "." + OutputPathConstants.POJO_PACKAGE;
        String pojoPath     = basePath + File.separator + OutputPathConstants.POJO_PACKAGE + File.separator;
        String pojoFileName = StrUtil.javaName(tableName, true) + OutputPathConstants.CLASS_SUFFIX_POJO + ".java";

        new File(pojoPath).mkdirs();

        try {
            OutputStream os = new FileOutputStream(pojoPath + pojoFileName);
            PojoTemplateUtil.render(pojoPackage, tableName, os);
        } catch (FileNotFoundException e) {
            logger.info("fail creating pojo {}, e={}", tableName, e);
        }
    }
}
