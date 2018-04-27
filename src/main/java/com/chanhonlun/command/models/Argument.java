package com.chanhonlun.command.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class Argument implements Serializable {

    private String name;
    private boolean required;
}
