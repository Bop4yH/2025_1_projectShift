package ru.shift.util;

import org.apache.commons.cli.*;

import java.nio.file.Path;

import static ru.shift.Main.log;

public class Options {
    public static final String OPTION_O = "o";
    public static final Option OUTPUT_PATH_OPTION = new Option(OPTION_O, true, "results path");

    private Path inputFilePath;
    private Path outputFilePath;
    private boolean isValid = false;

    enum OutputMode {CONSOLE, FILE}

    OutputMode outputMode = OutputMode.CONSOLE;

    private Options() {
    }

    public static Options parseCommandLine(String[] args) {
        Options options = new Options();
        org.apache.commons.cli.Options cliOptions = new org.apache.commons.cli.Options().addOption(OUTPUT_PATH_OPTION);
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(cliOptions, args);

            if (cmd.hasOption(OPTION_O)) {
                options.outputFilePath = Path.of(cmd.getOptionValue(OPTION_O));

                options.outputMode = OutputMode.FILE;
            }
            String[] defaultArgs = cmd.getArgs();

            if (defaultArgs.length != 1) {
                throw new ParseException("Wrong number of arguments");
            }
            options.inputFilePath = Path.of(defaultArgs[0]);
            options.isValid = true;
            return options;
        } catch (ParseException e) {
            options.isValid = false;
            log.error("Invalid command line: ", e);

            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("-o out1.txt in1.txt", cliOptions);
            return null;
        }
    }

    public boolean isFileMode() {
        return outputMode == OutputMode.FILE;
    }

    public Path getInputPath() {
        return inputFilePath;
    }

    public Path getOutputPath() {
        if (outputMode == OutputMode.FILE) {
            return outputFilePath;
        }
        return null;
    }

    public boolean isValid() {
        return isValid;
    }
}