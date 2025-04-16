package ru.shift.model;

import ru.shift.dto.CellViewData;

import static ru.shift.model.CellState.*;
import static ru.shift.model.CellType.BOMB;

public class Cell {
    private final CellType cellType;
    private CellState cellState = CLOSED;

    private int neighboringMines;

    public Cell(CellType cellType) {
        this.cellType = cellType;
    }

    public Cell(CellType cellType, int neighboringMines) {
        this.cellType = cellType;
        this.neighboringMines = neighboringMines;

    }

    public CellViewData toViewData() {
        return new CellViewData(cellState, cellState == CellState.OPENED ? getNeighboringMines() : 0);
    }

    public CellType getCellType() {
        return cellType;
    }

    public CellState getCellState() {
        return cellState;
    }

    public int getNeighboringMines() {
        return neighboringMines;
    }

    public void open() {
        if(cellState == CLOSED) {
            if (cellType == BOMB) {
                cellState = EXPLODED;
            } else {
                cellState = OPENED;
            }
        }
    }

    public void openMine(){
        if(cellType == BOMB && cellState == CLOSED){
            cellState = MINE;
        }
    }

    public void explodeMine(){
        if(cellType == BOMB){
            cellState = EXPLODED;
        }
    }

    public void markIncorrectFlag(){
        if(cellState == FLAGGED && cellType != BOMB){
            cellState = INCORRECT_FLAG;
        }
    }

    public void flag() {
        cellState = cellState == FLAGGED ? CLOSED : FLAGGED;
    }

    public void incrementValue() {
        neighboringMines++;
    }

    public boolean isOpened() {
        return cellState == OPENED;
    }

    public boolean isFlagged() {
        return cellState == FLAGGED;
    }

    public boolean isMissFlagged() {
        return cellState == INCORRECT_FLAG;
    }

    public boolean isBomb() {
        return cellType == BOMB;
    }
}
