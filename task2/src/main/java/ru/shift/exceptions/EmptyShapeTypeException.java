package ru.shift.exceptions;

public class EmptyShapeTypeException extends FigureCreationException {
    public EmptyShapeTypeException(String message) {
        super(message);
    }
}
