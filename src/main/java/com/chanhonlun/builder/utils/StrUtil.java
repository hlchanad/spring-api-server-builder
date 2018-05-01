package com.chanhonlun.builder.utils;

import com.google.common.base.CaseFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrUtil {

    private static final Logger logger = LoggerFactory.getLogger(StrUtil.class);

    public static String javaName(String underScoredName, boolean capitalizeFirst) {
        CaseFormat caseFormat = capitalizeFirst ? CaseFormat.UPPER_CAMEL : CaseFormat.LOWER_CAMEL;
        return CaseFormat.UPPER_UNDERSCORE.to(caseFormat, underScoredName.toUpperCase());
    }
}
