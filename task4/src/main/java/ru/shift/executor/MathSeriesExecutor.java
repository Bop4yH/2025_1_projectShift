package ru.shift.executor;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static ru.shift.fork_join.MainForkJoin.log;

public class MathSeriesExecutor {
    private static final MathContext mc = new MathContext(50, RoundingMode.HALF_UP);

    private MathSeriesExecutor() {}
    public static BigDecimal execute(int numberN, int nThreads) {
        // формула = 1 / (n * (n + 1))
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        List<Callable<BigDecimal>> tasks = getCallables(numberN, nThreads);
        List<Future<BigDecimal>> results;

        try {
            results = executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Поток был прерван во время invokeAll()", e);
            return BigDecimal.ZERO;
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (Future<BigDecimal> future : results) {
            try {
                sum = sum.add(future.get());
            }
            catch (ExecutionException e) {
                Throwable cause = e.getCause();
                log.error("Ошибка при выполнении потока: {}", cause.getMessage(), cause);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Поток был прерван", e);
                return sum;
            }
        }

        executor.shutdown();
        return sum;
    }

    private static List<Callable<BigDecimal>> getCallables(int numberN, int nThreads) {
        List<Callable<BigDecimal>> tasks = new ArrayList<>();
        int oneInterval = numberN / nThreads;
        for (int t = 0; t < nThreads; t++) {
            final int start = t * oneInterval + 1;
            final int end = (t == nThreads - 1) ? numberN : (start + oneInterval - 1);
            tasks.add(() -> {
                BigDecimal sum = BigDecimal.ZERO;
                int i = start;

                while (i <= end) {
                    int denominator = i * (i + 1);
                    BigDecimal term = BigDecimal.ONE.divide(new BigDecimal(denominator), mc);
                    sum = sum.add(term, mc);
                    i++;
                }

                log.info("Вычисление от {} до {} завершено. Результат: {}. ", start, end, sum.toPlainString());
                return sum;
            });
        }
        return tasks;
    }

}
