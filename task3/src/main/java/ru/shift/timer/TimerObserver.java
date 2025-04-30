package ru.shift.timer;

public interface TimerObserver {
    void onTimerTick(int secondsElapsed);
}
