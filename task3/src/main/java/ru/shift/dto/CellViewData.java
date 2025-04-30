package ru.shift.dto;

import ru.shift.model.CellState;

public record CellViewData(CellState state, int neighboringMines) {
}

