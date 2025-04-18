package ru.shift;

import java.util.Scanner;
import java.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigInteger;
import java.math.BigDecimal;


public class Main{
    public static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        log.info("Введите значение N: ");
        BigInteger N = new BigInteger(scanner.nextLine());

        ForkJoinPool pool = new ForkJoinPool();

        log.info("Начинаем вычисления...");
        long startTime = System.currentTimeMillis();

        int numThreads = Runtime.getRuntime().availableProcessors();
        BigInteger threshold = N.divide(BigInteger.valueOf(numThreads * 8L));
        MathSeriesTask task = new MathSeriesTask(BigInteger.ONE, N,threshold);
        BigDecimal result = pool.invoke(task);

        long endTime = System.currentTimeMillis();
        log.info("Результат: {}", result);
        log.info("Время выполнения: {} мс" , (endTime - startTime));

        pool.shutdown();
    }
}