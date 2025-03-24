package shift.figures;

import org.junit.jupiter.api.Test;
import ru.shift.figures.ShapeType;

import static org.assertj.core.api.Assertions.assertThat;

class ShapeTypeTest {
    @Test
    void testGetShapeName() {
        String exp1 = "Круг";
        String exp2 = "Прямоугольник";
        String exp3 = "Треугольник";

        String act1 = ShapeType.CIRCLE.getShapeName();
        String act2 = ShapeType.RECTANGLE.getShapeName();
        String act3 = ShapeType.TRIANGLE.getShapeName();

        assertThat(exp1).isEqualTo(act1);
        assertThat(exp2).isEqualTo(act2);
        assertThat(exp3).isEqualTo(act3);
    }
}