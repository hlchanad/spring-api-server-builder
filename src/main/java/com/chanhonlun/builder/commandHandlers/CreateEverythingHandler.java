package com.chanhonlun.builder.commandHandlers;

import com.chanhonlun.builder.consts.Constants;
import com.chanhonlun.builder.utils.CryptoUtil;
import com.chanhonlun.builder.utils.PropertiesUtil;
import com.chanhonlun.command.handlers.Handler;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class CreateEverythingHandler implements Handler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(String... arguments) {

        logger.info("Create Everything Handler");

        String outputDirectory = PropertiesUtil.getProperty(Constants.PROP_OUTPUT_DIRECTORY);
        String basicStructureResourcesPath = Constants.PATH_BASIC_STRUCTURE;

        initOutputDirectory(outputDirectory);

        copyBasicStructure(basicStructureResourcesPath, outputDirectory);


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

}
