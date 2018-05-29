package com.chanhonlun.builder.commandHandlers;

import com.chanhonlun.builder.consts.Constants;
import com.chanhonlun.builder.consts.OutputPathConstants;
import com.chanhonlun.builder.consts.TemplatePathConstants;
import com.chanhonlun.builder.utils.*;
import com.chanhonlun.command.handlers.Handler;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

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

        writeBuildDotGradle(groupId, artifact, outputDirectory);

        String basePackageName = groupId + "." + artifact;
        // sample basePath = output/src/main/java/com/chanhonlun/server
        String basePath        = outputDirectory + File.separator + OutputPathConstants.OUTPUT_PATH_SRC_MAIN_JAVA + File.separator + basePackageName.replaceAll("\\.", "\\".equals(File.separator) ? "\\" + File.separator : File.separator);

        new File(basePath).mkdirs();

        writeBaseRepositoryInterface(basePath, basePackageName);

        createHelloWroldController(basePath, basePackageName);

        for (String tableName : JDBCUtil.getTableNames()) {
            createPojo(tableName, basePath, basePackageName);
            createRepository(tableName,basePath, basePackageName);
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

    private void writeBuildDotGradle(String groupId, String artifact, String rootDirectory) {

        Map<String, Object> templateData = new HashMap<>();
        templateData.put("groupId", groupId);
        templateData.put("artifact", artifact);

        try (OutputStream os = new FileOutputStream(rootDirectory + File.separator + "build.gradle")) {
            TemplateUtil.renderTemplate(TemplatePathConstants.RESOURCES_BUILD_DOT_GRADLE_PATH, templateData, os);
        } catch (IOException e) {
            logger.info("fail creating build.gradle e={}", e);
        }
    }

    private void writeBaseRepositoryInterface(String basePath, String basePackageName) {

        String repoPackage  = basePackageName + "." + OutputPathConstants.REPO_PACKAGE;
        String repoPath     = basePath + File.separator + OutputPathConstants.REPO_PACKAGE + File.separator;

        String translationTableSuffix = StrUtil.capFirst(PropertiesUtil.getProperty(Constants.PROP_PROJ_TABLE_TRANSLATION_TABLE_SUFFIX));

        String repoFileName = OutputPathConstants.BASE_CLASS_NAME + OutputPathConstants.CLASS_SUFFIX_REPOSITORY + ".java";
        String repoTranslationFileName = OutputPathConstants.BASE_CLASS_NAME + translationTableSuffix + OutputPathConstants.CLASS_SUFFIX_REPOSITORY + ".java";

        new File(repoPath).mkdirs();

        try {
            OutputStream basicRepoOS = new FileOutputStream(repoPath + repoFileName);
            OutputStream basicTranslationRepoOS = new FileOutputStream(repoPath + repoTranslationFileName);

            TemplateRepoUtil.renderBasic(repoPackage, basicRepoOS);
            TemplateRepoUtil.renderBasicTranslation(repoPackage, basicTranslationRepoOS);

        } catch (FileNotFoundException e) {
            logger.info("fail creating basic repos, e={}", e);
        }
    }

    private void createHelloWroldController(String basePath, String basePackageName) {

        String controllerPackage  = basePackageName + "." + OutputPathConstants.CONTROLLER_PACKAGE;
        String controllerPath     = basePath + File.separator + OutputPathConstants.CONTROLLER_PACKAGE + File.separator;
        String controllerFileName = OutputPathConstants.HELLO_WORLD_CONTROLLER_NAME + OutputPathConstants.CLASS_SUFFIX_CONTROLLER + ".java";

        new File(controllerPath).mkdirs();

        Map<String, Object> templateData = new HashMap<>();
        templateData.put("packageName", controllerPackage);
        templateData.put("helloWorldControllerName", OutputPathConstants.HELLO_WORLD_CONTROLLER_NAME + OutputPathConstants.CLASS_SUFFIX_CONTROLLER);

        try (OutputStream os = new FileOutputStream(controllerPath + controllerFileName)){
            TemplateUtil.renderTemplate(TemplatePathConstants.RESOURCES_HELLO_WORLD_PATH, templateData, os);
        } catch (IOException e) {
            logger.info("fail creating HelloWorldController {}, e={}", controllerFileName, e);
        }
    }

    private void createPojo(String tableName, String basePath, String basePackageName) {

        String pojoPackage  = basePackageName + "." + OutputPathConstants.POJO_PACKAGE;
        String pojoPath     = basePath + File.separator + OutputPathConstants.POJO_PACKAGE + File.separator;
        String pojoFileName = StrUtil.javaName(tableName, true) + OutputPathConstants.CLASS_SUFFIX_POJO + ".java";

        new File(pojoPath).mkdirs();

        try (OutputStream os = new FileOutputStream(pojoPath + pojoFileName)){
            TemplatePojoUtil.render(pojoPackage, tableName, os);
        } catch (IOException e) {
            logger.info("fail creating pojo {}, e={}", tableName, e);
        }
    }

    private void createRepository(String tableName, String basePath, String basePackageName) {

        String repoPackage  = basePackageName + "." + OutputPathConstants.REPO_PACKAGE;
        String repoPath     = basePath + File.separator + OutputPathConstants.REPO_PACKAGE + File.separator;
        String repoFileName = StrUtil.javaName(tableName, true) + OutputPathConstants.CLASS_SUFFIX_REPOSITORY + ".java";

        String pojoPackage  = basePackageName + "." + OutputPathConstants.POJO_PACKAGE;

        String translationTableSuffix = PropertiesUtil.getProperty(Constants.PROP_PROJ_TABLE_TRANSLATION_TABLE_SUFFIX);

        new File(repoPath).mkdirs();

        int indexOf = tableName.toLowerCase().indexOf(translationTableSuffix.toLowerCase());
        boolean isTranslationTable = indexOf > 0 && indexOf == tableName.length() - translationTableSuffix.length();

        try (OutputStream os = new FileOutputStream(repoPath + repoFileName)){
            if (isTranslationTable) {
                TemplateRepoUtil.renderDetail(repoPackage, pojoPackage, tableName, os);
            } else {
                TemplateRepoUtil.render(repoPackage, pojoPackage, tableName, os);
            }
        } catch (IOException e) {
            logger.info("fail creating repo {}, e={}", tableName, e);
        }
    }
}
