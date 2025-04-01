package ru.shift.util;

import ru.shift.exceptions.*;
import ru.shift.figures.*;

import java.io.BufferedReader;
import java.io.IOException;

import static ru.shift.Main.log;

public final class FigureFactory {
    private FigureFactory() {
    }

    public static Figure readFigure(BufferedReader reader) throws IOException {
        ShapeType shapeType = toShapeType(reader.readLine());

        String params = reader.readLine();
        if (params == null) {
            log.error("No parameters to create {} provided", shapeType);
            throw new EmptyParametersException("No parameters provided");
        }

        try {
            return switch (shapeType) {
                case CIRCLE -> Circle.fromString(params);
                case RECTANGLE -> Rectangle.fromString(params);
                case TRIANGLE -> Triangle.fromString(params);
            };
        } catch (NumberFormatException e) {
            log.error("Wrong input double numbers format {} ", params, e);
            throw new NotDoubleParameterException("Wrong input double numbers format " + e.getMessage());
        } catch (InvalidParametersException e) {
            log.error("Could not create figure {} from:  {}", shapeType, params);
            throw e;
        }
    }

    private static ShapeType toShapeType(String shapeType) {
        try {
            if (shapeType.isEmpty()) {
                log.error("No shape type provided");
                throw new EmptyShapeTypeException("No shape type provided");
            }

            return ShapeType.valueOf(shapeType);
        } catch (IllegalArgumentException e) {
            log.error("Could not recognize {} as a figure type", shapeType);
            throw new UnsupportedShapeTypeException(shapeType);
        }
    }
}