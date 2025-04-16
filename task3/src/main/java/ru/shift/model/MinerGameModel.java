package ru.shift.model;

import ru.shift.dto.CellViewData;

import ru.shift.dto.GameType;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MinerGameModel{
    GameType gameType;
    MineField mineField;

    private GameStatus status = GameStatus.IN_PROGRESS;
    private final List<ModelObserver> observers = new ArrayList<>();

    public MinerGameModel() {
        gameType = GameType.NOVICE;
        mineField = new MineField(gameType);
    }

    public MineField getMineField() {
        return mineField;
    }

    public void addObserver(ModelObserver observer) {
        observers.add(observer);
    }

    public GameType getDifficulty() {
        return gameType;
    }

    private void notifyCellChanged(int x, int y) {
        Cell cell = mineField.getCell(x, y);
        CellViewData data = cell.toViewData();

        for (ModelObserver observer : observers) {
            observer.onCellChanged(x, y, data);
        }
    }


    public void notifyGameWon() {
        for (ModelObserver observer : observers) {
            observer.onGameWon();
        }
    }

    public void notifyGameLost() {
        for (ModelObserver observer : observers) {
            observer.onGameLost();
        }
    }

    public void notifyRemainingMinesChanged() {
        int remaining = mineField.getTotalMines() - mineField.getFlaggedCells();
        for (ModelObserver observer : observers) {
            observer.onRemainingMinesChanged(remaining);
        }
    }

    public void notifyGameStarted() {
        for (ModelObserver observer : observers) {
            observer.onGameStarted(gameType);
            observer.onRemainingMinesChanged(gameType.getAmountOfBombs());
        }
    }

    public void resetGame() {
        mineField = new MineField(gameType);
        status = GameStatus.IN_PROGRESS;
        notifyGameStarted();

    }

    public void openCell(int x, int y) {
        if (status != GameStatus.IN_PROGRESS) return;

        revealEmptyArea(new Point(x, y));

        if (mineField.getCell(x, y).isBomb()) {
            status = GameStatus.LOST;
            openAllMines();
            notifyGameLost();
        }
    }


    private void openAndNotify(int x, int y) {
        mineField.openCell(x, y);
        notifyCellChanged(x, y);
        if (mineField.checkFieldCleared()) {
            flagAllBombs();
            status = GameStatus.WON;
            notifyGameWon();
        }
    }

    private void revealEmptyArea(Point start) {
        Queue<Point> queue = new LinkedList<>();
        queue.add(start);

        while (!queue.isEmpty()) {

            Point current = queue.poll();
            int x = current.x;
            int y = current.y;

            if (!mineField.canOpenCell(current)) continue;

            openAndNotify(x, y);

            if (mineField.getCell(x, y).getCellType() != CellType.EMPTY) continue;

            addNeighborsToQueue(current, queue);

        }
    }

    private void addNeighborsToQueue(Point p, Queue<Point> queue) {
        for (Point neighbor : mineField.getNeighbors(p.x, p.y)) {
            if (mineField.canOpenCell(neighbor)) {
                queue.add(neighbor);
            }
        }
    }

    public void flagAndNotify(int x, int y) {
        if (status != GameStatus.IN_PROGRESS) return;
        mineField.flagCell(x, y);
        notifyCellChanged(x, y);
        notifyRemainingMinesChanged();
    }

    public void flagAllBombs() {
        for (int i = 0; i < mineField.getHeight(); i++) {
            for (int j = 0; j < mineField.getWidth(); j++) {
                if (mineField.getCell(i, j).isBomb() && mineField.getCell(i, j).getCellState() != CellState.FLAGGED) {
                    flagAndNotify(i, j);
                }
            }
        }
    }

    public void setDifficulty(GameType difficulty) {
        gameType = difficulty;
        mineField = new MineField(gameType);
    }

    public void attemptOpenAround(int x, int y) {
        if (mineField.flagsAround(x, y) != mineField.getCell(x, y).getNeighboringMines()
                || mineField.getCell(x, y).getCellType() != CellType.NUMBER
                || mineField.getCell(x, y).getCellState() != CellState.OPENED)
            return;

        boolean bombTriggered = false;
        List<Point> safeCells = new ArrayList<>();

        for (Point neighbor : mineField.getNeighbors(x, y)) {
            Cell neighborCell = mineField.getCell(neighbor.x, neighbor.y);
            if (neighborCell.getCellState() != CellState.FLAGGED) {
                if (neighborCell.isBomb()) {
                    neighborCell.explodeMine();
                    notifyCellChanged(neighbor.x, neighbor.y);
                    bombTriggered = true;
                } else {
                    safeCells.add(neighbor); // безопасная ячейка
                }
            }
        }

        if (bombTriggered) {
            status = GameStatus.LOST;
            openAllMines();
            notifyGameLost();
        } else {
            for (Point emptyCell : safeCells) {
                openCell(emptyCell.x, emptyCell.y);
            }
        }
    }

    public void openAllMines() {
        for (int y = 0; y < mineField.getHeight(); y++) {
            for (int x = 0; x < mineField.getWidth(); x++) {
                Cell cell = mineField.getCell(x, y);
                if (cell.getCellType() == CellType.BOMB) {
                    cell.openMine();
                } else {
                    mineField.getCell(x, y).markIncorrectFlag();
                }
                notifyCellChanged(x, y);
            }
        }
    }
}
