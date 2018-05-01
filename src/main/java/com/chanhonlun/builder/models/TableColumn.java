package com.chanhonlun.builder.models;

import lombok.Data;

@Data
public class TableColumn {

    private Boolean isPK;
    private String  name;
    private String  dataType;
    private Integer columnSize;
    private Integer decimalDigit;
    private Boolean nullable;
    private String  defaultValue;
    private String  remarks;

    public TableColumn() {}

    public TableColumn(Boolean isPK, String name, String dataType, Integer columnSize, Integer decimalDigit, Boolean nullable, String defaultValue, String remarks) {
        this.isPK         = isPK;
        this.name         = name;
        this.dataType     = dataType;
        this.columnSize   = columnSize;
        this.decimalDigit = decimalDigit;
        this.nullable     = nullable;
        this.defaultValue = defaultValue;
        this.remarks      = remarks;
    }
}
