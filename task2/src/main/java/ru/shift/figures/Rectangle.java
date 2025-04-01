package ru.shift.figures;

import ru.shift.exceptions.IllegalNumberOfParametersException;


public class Rectangle extends Figure {
    private static final int ARGS_COUNT = 2;
    private final double a;
    private final double b;
    private double diagonal;

    private Rectangle(double a, double b) {
        shapeType = ShapeType.RECTANGLE;
        this.a = Math.max(a, b);
        this.b = Math.min(a, b);
    }

    @Override
    protected double evaluatePerimeter() {
        return 2 * (a + b);
    }

    @Override
    protected double evaluateArea() {
        return a * b;
    }

    public static Rectangle fromString(String line) {
        String[] params = extractShapeParameters(line);
        if (params.length != ARGS_COUNT) {
            throw new IllegalNumberOfParametersException("Invalid number of parameters for Rectangle");
        }

        double a = check(Double.parseDouble(params[0]));
        double b = check(Double.parseDouble(params[1]));
        return new Rectangle(a, b);
    }

    public static Rectangle fromSides(double a, double b) {
        check(a);
        check(b);
        return new Rectangle(a, b);
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getDiagonal() {
        if (diagonal == 0) {
            diagonal = evaluateDiagonal();
        }
        return diagonal;
    }

    private double evaluateDiagonal() {
        return Math.sqrt(a * a + b * b);
    }
}