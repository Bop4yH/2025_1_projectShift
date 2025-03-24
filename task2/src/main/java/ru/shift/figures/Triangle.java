package ru.shift.figures;

import ru.shift.exceptions.IllegalNumberOfParametersException;
import ru.shift.exceptions.IllegalTriangleSidesLengthException;

import static ru.shift.Main.log;

public class Triangle extends Figure {
    private final double a;
    private final double b;
    private final double c;
    private double angleAgainstA;
    private double angleAgainstB;
    private double angleAgainstC;

    private Triangle(double a, double b, double c) {
        shapeType = ShapeType.TRIANGLE;
        this.a = a;
        this.b = b;
        this.c = c;
    }


    @Override
    protected double evaluatePerimeter() {
        return a + b + c;
    }

    @Override
    protected double evaluateArea() {
        double p = getPerimeter() / 2;
        return Math.sqrt((p * (p - a) * (p - b) * (p - c)));
    }

    public static Triangle fromString(String line) throws NumberFormatException {
        String[] stringArr = line.split(" ");
        if (stringArr.length != 3) {
            log.error("Invalid number of parameters for Triangle: {}", stringArr.length);
            throw new IllegalNumberOfParametersException("Invalid number of parameters for Triangle");
        }

        double a = check(Double.parseDouble(stringArr[0]));
        double b = check(Double.parseDouble(stringArr[1]));
        double c = check(Double.parseDouble(stringArr[2]));
        checkIfTriangle(a, b, c);

        return new Triangle(a, b, c);
    }

    public static Triangle ofSides(double a, double b, double c) {
        check(a);
        check(b);
        check(c);
        checkIfTriangle(a, b, c);
        return new Triangle(a, b, c);
    }

    private static boolean isTriangle(double a, double b, double c) {
        return a + b > c && a + c > b && b + c > a;
    }

    private static void checkIfTriangle(double a, double b, double c) {
        if (!isTriangle(a, b, c)) {
            log.error("Not a triangle: {} {} {}", a, b, c);
            throw new IllegalTriangleSidesLengthException("Not a triangle");
        }

    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    private static double evaluateAngle(double sideToCalculateAngleAgainst, double sideA, double sideB) {
        double numerator = sideA * sideA + sideB * sideB
                - sideToCalculateAngleAgainst * sideToCalculateAngleAgainst;
        double denominator = 2 * sideA * sideB;
        return Math.toDegrees(Math.acos(numerator / denominator));
    }

    public double getAngleAgainstA() {
        if (angleAgainstA == 0) {
            angleAgainstA = evaluateAngle(a, b, c);
        }
        return angleAgainstA;
    }

    public double getAngleAgainstB() {
        if (angleAgainstB == 0) {
            angleAgainstB = evaluateAngle(b, a, c);
        }
        return angleAgainstB;
    }

    public double getAngleAgainstC() {
        if (angleAgainstC == 0) {
            angleAgainstC = evaluateAngle(c, a, b);
        }
        return angleAgainstC;
    }
}
