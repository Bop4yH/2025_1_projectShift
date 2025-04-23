package ru.shift.timer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class GameTimer {
    private final List<TimerObserver> observers = new ArrayList<>();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory());
    private volatile int elapsedSeconds = 0;
    private ScheduledFuture<?> task;

    public void addObserver(TimerObserver observer) {
        observers.add(observer);
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void start() {
        long startTime = System.nanoTime();

        task = executor.scheduleAtFixedRate(() -> {
            int nowElapsed = (int) ((System.nanoTime() - startTime) / 1_000_000_000L); // переводим в секунды
            if (nowElapsed > elapsedSeconds) {
                elapsedSeconds = nowElapsed;
                for (TimerObserver observer : observers) {
                    observer.onTimerTick(elapsedSeconds);
                }
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        if (task != null) {
            task.cancel(false);
        }
    }


    public void reset() {
        stop();
        elapsedSeconds = 0;
        for (TimerObserver observer : observers) {
            observer.onTimerTick(elapsedSeconds);
        }
    }

    private static class DaemonThreadFactory implements ThreadFactory {
        private final ThreadFactory defaultFactory = Executors.defaultThreadFactory();

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = defaultFactory.newThread(r);
            thread.setDaemon(true);
            return thread;
        }
    }
}
