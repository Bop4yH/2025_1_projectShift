package ru.shift.controller;

import ru.shift.dto.GameType;

public interface Controller extends ClickListener {
    void resetGame();

    void setDifficulty(GameType difficulty);
}
