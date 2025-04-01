package shift.format;

import org.junit.jupiter.api.Test;
import ru.shift.figures.Triangle;
import ru.shift.format.TextFormatter;

import static org.assertj.core.api.Assertions.assertThat;

class TriangleTextFormatterTest {

    @Test
    void getFormattedInfo() {
        String exp = String.format("Тип фигуры: Треугольник%n" +
                "Площадь: 6 кв. см%n" +
                "Периметр: 12 см%n" +
                "Сторона A: 3 см%n" +
                "Угол противолежащий стороне A: 36,87 град%n" +
                "Сторона B: 4 см%n" +
                "Угол противолежащий стороне B: 53,13 град%n" +
                "Сторона C: 5 см%n" +
                "Угол противолежащий стороне C: 90 град%n");
        String act = TextFormatter.toText(Triangle.fromSides(3, 4, 5));
        assertThat(act).isEqualTo(exp);
    }
}