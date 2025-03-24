package ru.shift.exceptions;

public class EmptyParametersException extends InvalidParametersException {
    public EmptyParametersException(String message) {
        super(message);
    }
}
