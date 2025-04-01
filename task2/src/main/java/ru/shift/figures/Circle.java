package ru.shift.figures;

public class Circle extends Figure {
    private final double radius;
    private double diameter;

    private Circle(double radius) {
        shapeType = ShapeType.CIRCLE;
        this.radius = radius;
    }

    @Override
    protected double evaluatePerimeter() {
        return radius * 2 * Math.PI;
    }

    @Override
    protected double evaluateArea() {
        return radius * radius * Math.PI;
    }

    public static Circle fromString(String line) {
        return new Circle(check(Double.parseDouble(line)));
    }

    public static Circle ofRadius(double radius) {
        return new Circle(check(radius));
    }

    public double getRadius() {
        return radius;
    }

    public double getDiameter() {
        if (diameter == 0) {
            diameter = evaluateDiameter();
        }
        return diameter;
    }

    private double evaluateDiameter() {
        return 2 * radius;
    }
}