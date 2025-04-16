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

    public void addObserver(TimerObserver observer) {
        observers.add(observer);
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void start() {
        elapsedSeconds = 0;
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            elapsedSeconds++;
            for (TimerObserver observer : observers) {
                observer.onTimerTick(elapsedSeconds);
            }
        }, 1, 1, TimeUnit.SECONDS);
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
