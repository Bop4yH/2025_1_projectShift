package shift.format;

import org.junit.jupiter.api.Test;
import ru.shift.figures.Circle;
import ru.shift.format.TextFormatter;

import static org.assertj.core.api.Assertions.assertThat;

class CircleTextFormatterTest {

    @Test
    void testFormat() {
        String exp = String.format("Тип фигуры: Круг%n" +
                "Площадь: 452,39 кв. см%n" +
                "Периметр: 75,4 см%n" +
                "Радиус: 12 см%n" +
                "Диаметр: 24 см%n");
        String act = TextFormatter.toText(Circle.ofRadius(12));
        assertThat(act).isEqualTo(exp);
    }


}