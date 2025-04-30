package ru.shift.model;

import ru.shift.dto.CellViewData;
import ru.shift.dto.GameType;

public interface ModelObserver {
    void onCellChanged(int x, int y, CellViewData data);
    void onGameWon();
    void onGameLost();

    void onRemainingMinesChanged(int remaining);
    void onGameStarted(GameType gameType);
}
