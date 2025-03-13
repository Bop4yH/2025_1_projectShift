package ru.shift;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int tableSize = parseArg();
        if (tableSize != -1) {
            printTable(tableSize);
        }
    }

    public static void printTable(int size) {
        final int squareLength = String.valueOf(size * size).length();
        final int length = String.valueOf(size).length();
        final String sep = "-".repeat(length) + ("+" + "-".repeat(squareLength)).repeat(size);

        for (int i = 0; i <= size; i++) {
            System.out.printf("%" + length + "s", i == 0 ? "" : i);//%10s
            for (int j = 1; j <= size; j++) {
                System.out.printf("|" + "%" + squareLength + "d", (i == 0 ? 1 : i) * j);
            }
            System.out.println();
            System.out.println(sep);
        }
    }

    public static int parseArg() {
        Scanner in = new Scanner(System.in);
        int num;
        try {
            num = in.nextInt();
            if (num < 1 || 32 < num) {
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println("Only integer numbers between 1 and 32");
            return -1;
        }

        return num;
    }
}