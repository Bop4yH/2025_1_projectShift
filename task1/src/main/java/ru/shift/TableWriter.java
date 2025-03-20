package ru.shift;

import java.io.PrintWriter;

public class TableWriter {
    private static final String DASH = "-";
    private static final String DOT = "+";
    private static final String DELIMITER = "|";

    private TableWriter() {
    }

    public static void printTable(int size, PrintWriter writer) {
        final int squareLength = String.valueOf(size * size).length();
        final int length = String.valueOf(size).length();
        final String emptySpace = " ".repeat(length);
        final String separator = DASH.repeat(length) + (DOT + DASH.repeat(squareLength)).repeat(size);

        final String formatFirstColumn = "%" + length + "d";
        final String formatTableColumn = DELIMITER + "%" + squareLength + "d";

        StringBuilder numLine = new StringBuilder((squareLength + 1) * length);

        numLine.append(emptySpace);
        for (int j = 1; j <= size; j++) {
            numLine.append(String.format(formatTableColumn, j));
        }
        writer.println(numLine);
        writer.println(separator);

        for (int i = 1; i <= size; i++) {
            numLine = new StringBuilder((squareLength + 1) * length);
            numLine.append(String.format(formatFirstColumn, i));
            for (int j = 1; j <= size; j++) {
                numLine.append(String.format(formatTableColumn, i * j));
            }
            writer.println(numLine);
            writer.println(separator);
        }
    }
}
