package ru.shift.figures;

import ru.shift.exceptions.NegativeParameterException;

public abstract class Figure {
    protected static final String parameterDelimiter = " ";
    protected double perimeter;
    protected double area;
    protected ShapeType shapeType;



    protected abstract double evaluatePerimeter();

    protected abstract double evaluateArea();

    public ShapeType getShapeType() {
        return shapeType;
    }

    public double getPerimeter() {
        if (perimeter == 0) {
            perimeter = evaluatePerimeter();
        }
        return perimeter;
    }

    public double getArea() {
        if (area == 0) {
            area = evaluateArea();
        }
        return area;
    }

    protected static double check(double value) {
        if (value <= 0) {
            throw new NegativeParameterException("Negative parameter" + value);
        }
        return value;
    }

    protected static String[] extractShapeParameters(String line) {
        return line.split(parameterDelimiter);
    }
}




