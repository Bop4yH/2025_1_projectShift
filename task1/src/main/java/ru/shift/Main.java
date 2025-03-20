package ru.shift;

import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static final int FLOOR_LIMIT = 1;
    private static final int CEIL_LIMIT = 32;

    public static void main(String[] args) {
        try {
            int tableSize = readTableSize();
            TableWriter.printTable(tableSize, new PrintWriter(System.out, true));
        } catch (IllegalArgumentException | InputMismatchException e) {
            System.out.printf("Only integer numbers between %d and %d%n", FLOOR_LIMIT, CEIL_LIMIT);
            System.out.println ("Exception: " + e.getMessage());
            System.exit(0);
        }
    }

    private static int readTableSize() {
        Scanner in = new Scanner(System.in);
        int num;

        System.out.printf("Write table size between %d and %d%n", FLOOR_LIMIT, CEIL_LIMIT);
        num = in.nextInt();
        if (num < FLOOR_LIMIT || CEIL_LIMIT < num) {
            throw new IllegalArgumentException(String.format("Table size not between %d and %d", FLOOR_LIMIT, CEIL_LIMIT));
        }

        return num;
    }
}