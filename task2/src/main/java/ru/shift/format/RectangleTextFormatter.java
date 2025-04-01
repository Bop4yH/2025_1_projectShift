package ru.shift.format;

import ru.shift.figures.Rectangle;

public class RectangleTextFormatter extends TextFormatter<Rectangle> {
    @Override
    public String format(Rectangle rectangle) {
        return super.format(rectangle) +
                TextFormatter.formatLineDataWithLabel("Длина диагонали: ", rectangle.getDiagonal()) +
                TextFormatter.formatLineDataWithLabel("Длина: ", rectangle.getA()) +
                TextFormatter.formatLineDataWithLabel("Ширина: ", rectangle.getB());
    }
}
