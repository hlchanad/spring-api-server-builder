package com.chanhonlun.builder.models;

import lombok.Data;

@Data
public class TemplateColumn {

    private Boolean isPK;
    private String  columnName;
    private String  javaName;
    private String  javaType;
    private Boolean nullable;
    private String  defaultValue;
    private String  remarks;

}
