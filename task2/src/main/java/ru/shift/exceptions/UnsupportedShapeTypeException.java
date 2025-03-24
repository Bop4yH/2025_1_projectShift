package ru.shift.exceptions;

public class UnsupportedShapeTypeException extends FigureCreationException {
    public UnsupportedShapeTypeException(String message) {
        super(message);
    }
}
