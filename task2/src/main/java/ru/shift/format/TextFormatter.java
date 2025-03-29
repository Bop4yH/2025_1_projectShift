package ru.shift.format;


import ru.shift.figures.Figure;
import ru.shift.figures.ShapeType;

import java.text.DecimalFormat;
import java.util.Map;

public abstract class TextFormatter<T extends Figure> {
    protected static final String UNIT_CM = " см";
    protected static final String ANGLE_GRAD = " град";
    protected static final String SQUARE_LABEL = " кв";
    protected static int precision = 2;


    private static Map<ShapeType, TextFormatter<? extends Figure>> formatters;

    @SuppressWarnings("unchecked") // безопасно пока мапа составлена корректно
    public static <F extends Figure> String toText(F figure) {
        //починка class loader deadlock
        if (formatters == null){
            formatters = Map.of(
                    ShapeType.CIRCLE, new CircleTextFormatter(),
                    ShapeType.RECTANGLE, new RectangleTextFormatter(),
                    ShapeType.TRIANGLE, new TriangleTextFormatter()
            );
        }

        return ((TextFormatter<F>) formatters.get(figure.getShapeType())).format(figure);
    }

    protected String format(T figure) {
        return formatLineShapeType(figure.getShapeType()) +
                formatLineDataWithLabel("Площадь: ", figure.getArea(), SQUARE_LABEL) +
                formatLineDataWithLabel("Периметр: ", figure.getPerimeter());
    }

    protected static String formatLineShapeType(ShapeType shapeType) {
        return String.format("Тип фигуры: %s%n", shapeType.getShapeName());
    }


    protected static String formatLineDataWithLabel(String label, double data) {
        return String.format("%s%s%s%n", label, formatDouble(data), UNIT_CM);
    }


    protected static String formatLineDataWithLabel(String label, double data, String prefix) {
        return String.format("%s%s%s%s%n", label, formatDouble(data), prefix, UNIT_CM);
    }

    protected static String formatDouble(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#." + "#".repeat(Math.max(0, precision)));
        return decimalFormat.format(value);
    }
}
