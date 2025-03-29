package shift.format;

import org.junit.jupiter.api.Test;
import ru.shift.figures.Rectangle;
import ru.shift.format.TextFormatter;

import static org.assertj.core.api.Assertions.assertThat;

class RectangleTextFormatterTest {

    @Test
    void getFormattedInfo() {
        String exp = String.format("Тип фигуры: Прямоугольник%n" +
                "Площадь: 20 кв. см%n" +
                "Периметр: 24 см%n" +
                "Длина диагонали: 10,2 см%n" +
                "Длина: 10 см%n" +
                "Ширина: 2 см%n");
        String act = TextFormatter.toText(Rectangle.fromSides(10, 2));
        assertThat(act).isEqualTo(exp);
    }
}