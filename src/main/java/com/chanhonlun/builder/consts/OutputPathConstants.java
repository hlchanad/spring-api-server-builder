package com.chanhonlun.builder.consts;

import java.io.File;

public class OutputPathConstants {

    public static final String OUTPUT_PATH                  = "output" + File.separator + "project" + File.separator;

    public static final String OUTPUT_PATH_SRC_MAIN_JAVA    = "src" + File.separator + "main" + File.separator + "java";
    public static final String OUTPUT_PATH_SRC_MAIN_RES     = "src" + File.separator + "main" + File.separator + "resources";
    public static final String OUTPUT_PATH_SRC_MAIN_WEB_INF = "src" + File.separator + "main" + File.separator + "webapp" + File.separator + "WEB-INF";

    public static final String CLASS_SUFFIX_CONTROLLER      = "Controller";
    public static final String CLASS_SUFFIX_POJO            = "";
    public static final String CLASS_SUFFIX_REPOSITORY      = "Repository";

    public static final String BASE_CLASS_NAME              = "Base";

    public static final String CONTROLLER_PACKAGE           = "controllers";
    public static final String HELLO_WORLD_CONTROLLER_NAME  = "HelloWorld";

    public static final String POJO_PACKAGE                 = "pojos";
    public static final String REPO_PACKAGE                 = "repositories";
}
