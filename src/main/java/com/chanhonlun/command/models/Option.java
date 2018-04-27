package com.chanhonlun.command.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class Option implements Serializable {

    private String opt;
    private String longOpt;
    private String desc;
    private boolean hasArg;
    private Argument arg;
}
