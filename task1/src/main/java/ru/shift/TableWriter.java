package ru.shift;

import java.io.PrintWriter;

public class TableWriter {
    private final static String DASH = "-";
    private final static String DOT = "+";
    private final static String DELIMITER = "|";

    private TableWriter() {
    }

    public static void printTable(int size, PrintWriter writer) {
        final int squareLength = String.valueOf(size * size).length();
        final int length = String.valueOf(size).length();
        final String separator = DASH.repeat(length) + (DOT + DASH.repeat(squareLength)).repeat(size);
        StringBuilder numLine = new StringBuilder((squareLength + 1) * length);

        numLine.append(String.format("%2$" + length + "s", length, ""));
        for (int j = 1; j <= size; j++) {
            numLine.append(String.format(DELIMITER + "%2$" + squareLength + "d", squareLength, j));
        }
        writer.println(numLine);
        writer.println(separator);

        for (int i = 1; i <= size; i++) {
            numLine = new StringBuilder((squareLength + 1) * length);
            numLine.append(String.format("%2$" + length + "d", length, i));
            for (int j = 1; j <= size; j++) {
                numLine.append(String.format(DELIMITER + "%2$" + squareLength + "d", squareLength, i * j));
            }
            writer.println(numLine);
            writer.println(separator);
        }
    }
}
