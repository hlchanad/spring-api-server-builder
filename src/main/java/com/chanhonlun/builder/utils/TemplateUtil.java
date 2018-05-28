package com.chanhonlun.builder.utils;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.util.Map;

public class TemplateUtil {

    private static final Logger logger = LoggerFactory.getLogger(TemplateUtil.class);

    public static void renderTemplate(String templateResourcesPath, Map<String, Object> templateData, OutputStream os) {

        JtwigTemplate template = JtwigTemplate.classpathTemplate(templateResourcesPath);
        JtwigModel    model    = JtwigModel.newModel(templateData);
        template.render(model, os);
    }
}
