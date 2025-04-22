package ru.shift.model;

import ru.shift.dto.GameType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MineField {
    private Cell[][] field;
    private final int width;
    private final int height;
    private final int totalMines;
    private int clearedCells;
    private int flaggedCells;

    public MineField(GameType gameType) {
        width = gameType.getColumns();
        height = gameType.getRows();
        totalMines = gameType.getAmountOfBombs();
        initEmptyField();
    }

    public int getFlaggedCells() {
        return flaggedCells;
    }

    public int getTotalMines() {
        return totalMines;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private boolean isValidIndex(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    private void initEmptyField() {
        field = new Cell[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                field[i][j] = new Cell(CellType.EMPTY);
            }
        }
        clearedCells = 0;
        flaggedCells = 0;
    }

    public void placeBombsExcluding(int excludedColumn, int excludedRow) {
        int excludedIndex = excludedRow * width + excludedColumn;
        var bombIndexes = RandomGenerator.generateOrderedUniqueRandomNumbersExcluding(
                totalMines, height * width, excludedIndex);

        while (bombIndexes.hasNext()) {
            int currentBombIndex = bombIndexes.next();
            int row = currentBombIndex / width;
            int column = currentBombIndex % width;
            field[row][column] = new Cell(CellType.BOMB);
            adjustSurroundingNumbers(column, row);
        }
    }

    public Cell getCell(int x, int y) {
        return field[y][x];
    }

    public void openCell(int x, int y) {
        if (!canOpenCell(x, y)) return;
        field[y][x].open();
        if (field[y][x].getCellType() != CellType.BOMB) {
            clearedCells++;
        }
    }

    public boolean checkFieldCleared() {
        return clearedCells == height * width - totalMines;
    }

    private void adjustSurroundingNumbers(int x, int y) {
        for (Point neighbor : getNeighbors(x, y)) {
            int neighborX = neighbor.x;
            int neighborY = neighbor.y;

            if (!isValidIndex(neighborX, neighborY)) {
                continue;
            }

            Cell cell = field[neighborY][neighborX];
            switch (cell.getCellType()) {
                case EMPTY -> field[neighborY][neighborX] = new Cell(CellType.NUMBER, 1);
                case NUMBER -> cell.incrementValue();
                default -> {
                }
            }
        }
    }

    private boolean canOpenCell(int x, int y) {
        return isValidIndex(x, y) && !field[y][x].isOpened() && !field[y][x].isFlagged()
                && !field[y][x].isMissFlagged();
    }

    public boolean canOpenCell(Point p) {
        return canOpenCell(p.x, p.y);
    }


    public List<Point> getNeighbors(int x, int y) {
        List<Point> neighbors = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                if (!isValidIndex(x + dx, y + dy)) continue;

                neighbors.add(new Point(x + dx, y + dy));
            }
        }
        return neighbors;
    }

    public void flagCell(int x, int y) {
        if (field[y][x].isOpened()) return;
        field[y][x].flag();
        if (field[y][x].getCellState() == CellState.FLAGGED) {
            flaggedCells++;
        } else {
            flaggedCells--;
        }
    }

    public int flagsAround(int x, int y) {
        int amountOfFlags = 0;
        for (Point index : getNeighbors(x, y)) {
            if (field[index.y][index.x].isFlagged()) {
                amountOfFlags++;
            }
        }
        return amountOfFlags;
    }
}
