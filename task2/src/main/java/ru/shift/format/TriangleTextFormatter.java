package ru.shift.format;

import ru.shift.figures.Triangle;

public class TriangleTextFormatter implements TextFormatter<Triangle> {

    @Override
    public String format(Triangle triangle) {
        String sideA = formatLineTriangleSideAndAngle(triangle.getA(), triangle.getAngleAgainstA(), Side.A);
        String sideB = formatLineTriangleSideAndAngle(triangle.getB(), triangle.getAngleAgainstB(), Side.B);
        String sideC = formatLineTriangleSideAndAngle(triangle.getC(), triangle.getAngleAgainstC(), Side.C);
        return TextFormatter.super.format(triangle) + sideA + sideB + sideC;
    }

    private static String formatLineTriangleSideAndAngle(double side, double angle, Side sideName) {
        return String.format("Сторона %s: %s%s%n", sideName, TextFormatter.formatDouble(side), UNIT_CM) +
                String.format("Угол противолежащий стороне %s: %s%s%n", sideName, TextFormatter.formatDouble(angle), ANGLE_GRAD);
    }

    private enum Side {
        A,
        B,
        C
    }
}
