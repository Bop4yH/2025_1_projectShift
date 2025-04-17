package ru.shift.timer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameTimer {
    private final List<TimerObserver> observers = new ArrayList<>();
    private ScheduledExecutorService executor;
    private int elapsedSeconds = 0;
    private long startTime;
    public void addObserver(TimerObserver observer) {
        observers.add(observer);
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void start() {
        startTime = System.currentTimeMillis();
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            int nowElapsed = (int)((System.currentTimeMillis() - startTime) / 1000);
            if (nowElapsed > elapsedSeconds) {
                elapsedSeconds = nowElapsed;
                for (TimerObserver observer : observers) {
                    observer.onTimerTick(elapsedSeconds);
                }
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    public void reset() {
        stop();
        elapsedSeconds = 0;
        for (TimerObserver observer : observers) {
            observer.onTimerTick(elapsedSeconds);
        }
    }
}
