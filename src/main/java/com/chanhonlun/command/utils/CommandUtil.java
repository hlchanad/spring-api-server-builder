package com.chanhonlun.command.utils;

import com.chanhonlun.builder.consts.Constants;
import com.chanhonlun.builder.utils.PropertiesUtil;
import com.chanhonlun.command.exceptions.UnrecognizedCommandException;
import com.chanhonlun.command.handlers.Handler;
import com.chanhonlun.command.models.Command;
import com.chanhonlun.command.models.Option;
import com.chanhonlun.command.models.CommandConfig;
import com.google.gson.Gson;
import org.apache.commons.cli.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandUtil {

    private static Logger logger = LoggerFactory.getLogger(CommandUtil.class);

    private static org.apache.commons.cli.Options cliOptions;

    private static CommandConfig commandConfig;

    public static void init() throws IOException {

        String commandsFilePath = PropertiesUtil.getProperty(Constants.PROP_COMMANDS_FILE_PATH);

        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(commandsFilePath);

        commandConfig = new Gson().fromJson(IOUtils.toString(input), CommandConfig.class);

        logger.info("commandConfig: {}", commandConfig);

        parseOptions(commandConfig);
    }

    private static void parseOptions(CommandConfig commandConfig) {

        cliOptions = new org.apache.commons.cli.Options();

        for (Option option : commandConfig.getOptions()) {
            org.apache.commons.cli.Option.Builder builder = org.apache.commons.cli.Option.builder(option.getOpt());
            builder.longOpt(option.getLongOpt());
            builder.desc(option.getDesc());
            if (option.isHasArg()) {
                builder.hasArg();
                builder.argName(option.getArg().getName());
            }
            cliOptions.addOption(builder.build());
        }
    }

    private static void printUsage() {
        new HelpFormatter().printHelp("api-server-builder.jar [OPTION]...", cliOptions);
    }

    public static void parseCommand(String[] args){
        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine commandLine = parser.parse(cliOptions, args);
            handleCommand(commandLine);
        } catch (UnrecognizedOptionException | UnrecognizedCommandException e) {
            printUsage();
//            logger.error("UnrecognizedCommandException, e={}", e);
        } catch (ParseException e) {
            logger.error("ParseException, e={}", e);
        } catch (ClassNotFoundException e) {
            logger.error("ClassNotFoundException, e={}", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleCommand(CommandLine commandLine) throws ClassNotFoundException, UnrecognizedCommandException, IllegalAccessException, InstantiationException {

        Command theCommand = null;

        if (commandLine.getOptions().length <= 0) {
            printUsage();
        }
        else {
            for (Command command : commandConfig.getCommands()) {

                logger.info("command: {}", command.getOptions());

                List<String> enteredOptions = Stream.of(commandLine.getOptions())
                        .map(org.apache.commons.cli.Option::getOpt).sorted().collect(Collectors.toList());

                List<String> requiredOptions = command.getOptions().stream()
                        .sorted().collect(Collectors.toList());

                if (requiredOptions.equals(enteredOptions)) {
                    theCommand = command;
                    break;
                }
            }

            logger.info("theCommand: {}", theCommand);

            if (theCommand == null) {
                String enteredCommandString = Stream.of(commandLine.getOptions())
                        .map(org.apache.commons.cli.Option::getOpt).collect(Collectors.joining(" "));
                throw new UnrecognizedCommandException(enteredCommandString);
            }

            Class<?> clazz = Class.forName(theCommand.getHandler());

            List<String> arguments = new ArrayList<>();
            for (String option : theCommand.getOptions()) {
                arguments.add(commandLine.getOptionValue(option));
            }

            if (Handler.class.isAssignableFrom(clazz)) {
                ((Handler) clazz.newInstance()).handle(arguments.toArray(new String[0]));
            }
        }

    }
}
