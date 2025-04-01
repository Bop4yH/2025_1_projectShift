package ru.shift.format;

import ru.shift.figures.Circle;

public class CircleTextFormatter extends TextFormatter<Circle> {
    @Override
    public String format(Circle circle) {
        return super.format(circle) +
                TextFormatter.formatLineDataWithLabel("Радиус: ", circle.getRadius()) +
                TextFormatter.formatLineDataWithLabel("Диаметр: ", circle.getDiameter());
    }
}
