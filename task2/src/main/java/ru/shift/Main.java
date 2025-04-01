package ru.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shift.exceptions.FigureCreationException;
import ru.shift.format.TextFormatter;
import ru.shift.util.FigureFactory;
import ru.shift.util.Options;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;


public class Main {
    public static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Program start");

        var options = Options.parseCommandLine(args);
        if (options == null) {
            System.exit(0);
        }

        var out = new PrintWriter(System.out, true);
        try (var in = Files.newBufferedReader(options.getInputPath())) {
            try {
                if (options.isFileMode()) {
                    out = new PrintWriter(Files.newBufferedWriter(options.getOutputPath()), true);
                }
                var figure = FigureFactory.readFigure(in);
                out.println(TextFormatter.toText(figure));
            } finally {
                if (options.isFileMode()) {
                    out.close();
                }
            }
        } catch (FigureCreationException e) {
            System.exit(0);
        } catch (IOException e) {
            log.error("Error during IO operations", e);
            System.exit(0);
        }

        log.info("Program end");
    }
}