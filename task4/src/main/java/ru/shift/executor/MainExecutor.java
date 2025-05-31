package ru.shift.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.Scanner;

public class MainExecutor {
    public static final Logger log = LoggerFactory.getLogger(MainExecutor.class);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        log.info("Введите значение N: ");
        int numberN;
        try {
            numberN = Integer.parseInt(scanner.nextLine());
            if (numberN < 0) {
                throw new NumberFormatException("Введено отрицательное число: " + numberN);
            }
        } catch (NumberFormatException e) {
            log.error("Введены некорректные данные.", e);
            return;
        }
        int nThreads = Math.min(Runtime.getRuntime().availableProcessors(), numberN);

        log.info("Начинаем вычисления...");
        long startTime = System.nanoTime();
        BigDecimal result = MathSeriesExecutor.execute(numberN, nThreads);
        long endTime = System.nanoTime();

        log.info("Результат: {}", result);
        log.info("Время выполнения: {} мс", (endTime - startTime) / 1_000_000);
    }
}
