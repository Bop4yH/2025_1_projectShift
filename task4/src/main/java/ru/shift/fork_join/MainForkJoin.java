package ru.shift.fork_join;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;


public class MainForkJoin {
    public static final Logger log = LoggerFactory.getLogger(MainForkJoin.class);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        log.info("Введите значение N: ");
        BigInteger numberN;
        try {
            numberN = new BigInteger(scanner.nextLine());
            if (numberN.compareTo(BigInteger.ZERO) < 0) {
                throw new NumberFormatException("Введено отрицательное число: " + numberN);
            }
        } catch (NumberFormatException e) {
            log.error("Введены некорректные данные.", e);
            return;
        }

        ForkJoinPool pool = new ForkJoinPool();

        log.info("Начинаем вычисления...");
        long startTime = System.nanoTime();

        int numThreads = Runtime.getRuntime().availableProcessors();
        BigInteger threshold = numberN.divide(BigInteger.valueOf(numThreads * 8L));
        MathSeriesForkJoin task = new MathSeriesForkJoin(BigInteger.ONE, numberN, threshold);
        BigDecimal result = pool.invoke(task);

        long endTime = System.nanoTime();
        log.info("Результат: {}", result);
        log.info("Время выполнения: {} мс", (endTime - startTime));

        pool.shutdown();
    }
}