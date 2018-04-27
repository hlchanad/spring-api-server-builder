package com.chanhonlun.command.models;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Command implements Serializable {

    private List<String> options;

    private String handler;
}
