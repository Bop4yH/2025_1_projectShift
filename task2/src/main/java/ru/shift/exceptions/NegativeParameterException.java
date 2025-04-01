package ru.shift.exceptions;

public class NegativeParameterException extends InvalidParametersException {
    public NegativeParameterException(String message) {
        super(message);
    }
}
