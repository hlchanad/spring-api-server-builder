package com.chanhonlun.command.models;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CommandConfig implements Serializable {

    List<Option> options;

    List<Command> commands;
}
