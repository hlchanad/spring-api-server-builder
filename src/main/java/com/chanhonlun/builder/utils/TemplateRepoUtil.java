package com.chanhonlun.builder.utils;

import com.chanhonlun.builder.consts.Constants;
import com.chanhonlun.builder.consts.OutputPathConstants;
import com.chanhonlun.builder.consts.TemplatePathConstants;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class TemplateRepoUtil {

    private static final Logger logger = LoggerFactory.getLogger(TemplateRepoUtil.class);


    public static void render(String packageName, String pojoPackageName, String tableName, OutputStream os) {

        String pojoName       = StrUtil.javaName(tableName, true);
        String repositoryName = pojoName + OutputPathConstants.CLASS_SUFFIX_REPOSITORY;
        String baseRepository = OutputPathConstants.BASE_CLASS_NAME + OutputPathConstants.CLASS_SUFFIX_REPOSITORY;

        Map<String, Object> templateData = new HashMap<>();
        templateData.put("packageName", packageName);
        templateData.put("pojo", pojoName);
        templateData.put("pojoPackage", pojoPackageName);
        templateData.put("repository", repositoryName);
        templateData.put("baseRepository", baseRepository);

        JtwigTemplate template = JtwigTemplate.classpathTemplate(TemplatePathConstants.RESOURCES_REPO_PATH);
        JtwigModel model = JtwigModel.newModel(templateData);

        template.render(model, os);
    }

    public static void renderDetail(String packageName, String pojoPackageName, String tableName, OutputStream os) {

        String pojoName       = StrUtil.javaName(tableName, true);
        String repositoryName = pojoName + OutputPathConstants.CLASS_SUFFIX_REPOSITORY;
        String translationTableSuffix = PropertiesUtil.getProperty(Constants.PROP_PROJ_TABLE_TRANSLATION_TABLE_SUFFIX);
        String baseDetailRepository   = OutputPathConstants.BASE_CLASS_NAME + StrUtil.capFirst(translationTableSuffix) + OutputPathConstants.CLASS_SUFFIX_REPOSITORY;

        Map<String, Object> templateData = new HashMap<>();
        templateData.put("packageName", packageName);
        templateData.put("pojo", pojoName);
        templateData.put("pojoPackage", pojoPackageName);
        templateData.put("repository", repositoryName);
        templateData.put("baseDetailRepository", baseDetailRepository);

        JtwigTemplate template = JtwigTemplate.classpathTemplate(TemplatePathConstants.RESOURCES_DETAIL_REPO_PATH);
        JtwigModel model = JtwigModel.newModel(templateData);

        template.render(model, os);
    }

    public static void renderBasic(String packageName, OutputStream os) {

        String idField = PropertiesUtil.getProperty(Constants.PROP_PROJ_TABLE_ID_FIELD);

        Map<String, Object> templateData = new HashMap<>();
        templateData.put("packageName", packageName);
        templateData.put("capIdField", StrUtil.capFirst(idField));
        templateData.put("idField", idField);

        JtwigTemplate template = JtwigTemplate.classpathTemplate(TemplatePathConstants.RESOURCES_BASE_REPO_PATH);
        JtwigModel model = JtwigModel.newModel(templateData);

        template.render(model, os);
    }

    public static void renderBasicTranslation(String packageName, OutputStream os) {

        String refIdField = PropertiesUtil.getProperty(Constants.PROP_PROJ_TABLE_REF_ID_FIELD);
        String langField  = PropertiesUtil.getProperty(Constants.PROP_PROJ_TABLE_LANG_FIELD);

        Map<String, Object> templateData = new HashMap<>();
        templateData.put("packageName", packageName);
        templateData.put("capRefIdField", StrUtil.capFirst(refIdField));
        templateData.put("idField", refIdField);
        templateData.put("capLangField", StrUtil.capFirst(langField));
        templateData.put("langField", langField);

        JtwigTemplate template = JtwigTemplate.classpathTemplate(TemplatePathConstants.RESOURCES_BASE_DETAIL_REPO_PATH);
        JtwigModel model = JtwigModel.newModel(templateData);

        template.render(model, os);
    }
}
