package com.chanhonlun.command.exceptions;

public class UnrecognizedCommandException extends Exception {

    public UnrecognizedCommandException() {
        super("this command is not known");
    }

    public UnrecognizedCommandException(String command) {
        super("the command, " + command + ", is not known");
    }
}
