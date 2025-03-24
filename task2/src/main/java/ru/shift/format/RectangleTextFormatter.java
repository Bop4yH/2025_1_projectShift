package ru.shift.format;

import ru.shift.figures.Rectangle;

public class RectangleTextFormatter implements TextFormatter<Rectangle> {
    @Override
    public String format(Rectangle rectangle) {
        return TextFormatter.super.format(rectangle) +
                TextFormatter.formatLineDataWithLabel("Длина диагонали: ", rectangle.getDiagonal()) +
                TextFormatter.formatLineDataWithLabel("Длина: ", rectangle.getA()) +
                TextFormatter.formatLineDataWithLabel("Ширина: ", rectangle.getB());
    }
}
