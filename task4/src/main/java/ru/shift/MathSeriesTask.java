package ru.shift;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.RecursiveTask;

import static ru.shift.Main.log;


public class MathSeriesTask extends RecursiveTask<BigDecimal> {
    private static final MathContext mc = new MathContext(50, RoundingMode.HALF_UP);
    private final BigInteger start;
    private final BigInteger end;
    private final BigInteger threshold;

    public MathSeriesTask(BigInteger start, BigInteger end, BigInteger threshold) {
        this.start = start;
        this.end = end;
        this.threshold = threshold;
    }

    @Override
    protected BigDecimal compute() {
        // формула = 1 / (n * (n + 1))
        if (end.subtract(start).compareTo(threshold) <= 0) {

            BigDecimal sum = BigDecimal.ZERO;
            BigInteger i = start;

            while (i.compareTo(end) <= 0) {
                BigInteger nPlus1 = i.add(BigInteger.ONE);
                BigInteger denominator = i.multiply(nPlus1);
                BigDecimal term = BigDecimal.ONE.divide(new BigDecimal(denominator), mc);
                sum = sum.add(term, mc);
                i = i.add(BigInteger.ONE);
            }

            log.info("Вычисление от {} до {} завершено. Результат: {}. ", start, end, sum.toPlainString());
            return sum;

        } else {
            BigInteger mid = start.add(end).divide(BigInteger.TWO);

            MathSeriesTask left = new MathSeriesTask(start, mid, threshold);
            MathSeriesTask right = new MathSeriesTask(mid.add(BigInteger.ONE), end, threshold);

            left.fork();
            BigDecimal rightResult = right.compute();
            BigDecimal leftResult = left.join();

            return leftResult.add(rightResult, mc);
        }
    }
}
