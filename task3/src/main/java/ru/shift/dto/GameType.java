package ru.shift.dto;

public enum GameType {
    NOVICE(9, 9, 2),
    MEDIUM(16, 16, 40),
    EXPERT(16, 30, 99);

    final int rows;
    final int columns;
    final int amountOfBombs;

    GameType(int rows, int columns, int amountOfBombs) {
        this.rows = rows;
        this.columns = columns;
        this.amountOfBombs = amountOfBombs;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getAmountOfBombs() {
        return amountOfBombs;
    }
}
