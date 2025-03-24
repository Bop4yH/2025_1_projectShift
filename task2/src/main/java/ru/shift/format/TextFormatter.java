package ru.shift.format;


import ru.shift.figures.Figure;
import ru.shift.figures.ShapeType;

import java.text.DecimalFormat;
import java.util.Map;

public interface TextFormatter<T extends Figure> {

    //можно добавить кастомные юниты при желании
    String UNIT_CM = " см";
    String ANGLE_GRAD = " град";
    int DEFAULT_PRECISION = 2;
    int precision = DEFAULT_PRECISION;


    Map<ShapeType, TextFormatter<? extends Figure>> formatters = Map.of(
            ShapeType.CIRCLE, new CircleTextFormatter(),
            ShapeType.RECTANGLE, new RectangleTextFormatter(),
            ShapeType.TRIANGLE, new TriangleTextFormatter()
    );

    @SuppressWarnings("unchecked") // безопасно пока мапа составлена корректно
    static <F extends Figure> String toText(F figure) {
        return ((TextFormatter<F>) formatters.get(figure.getShapeType())).format(figure);
    }

    default String format(T figure) {
        return formatLineShapeType(figure.getShapeType()) +
                formatLineDataWithLabel("Площадь: ", figure.getArea(), " кв.") +
                formatLineDataWithLabel("Периметр: ", figure.getPerimeter());
    }

    static String formatLineShapeType(ShapeType shapeType) {
        return String.format("Тип фигуры: %s%n", shapeType.getShapeName());
    }


    static String formatLineDataWithLabel(String label, double data) {
        return String.format("%s%s%s%n", label, formatDouble(data), UNIT_CM);
    }


    static String formatLineDataWithLabel(String label, double data, String prefix) {
        return String.format("%s%s%s%s%n", label, formatDouble(data), prefix, UNIT_CM);
    }

    static String formatDouble(double value) {
        return formatDouble(value, precision);
    }

    static String formatDouble(double value, int decimalPlaces) {
        DecimalFormat decimalFormat = new DecimalFormat("#." + "#".repeat(Math.max(0, decimalPlaces)));
        return decimalFormat.format(value);
    }
}
