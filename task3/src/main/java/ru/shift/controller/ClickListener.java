package ru.shift.controller;

public interface ClickListener {
    void onLeftClick(int x, int y);
    void onRightClick(int x, int y);
    void onMiddleClick(int x, int y);
}
