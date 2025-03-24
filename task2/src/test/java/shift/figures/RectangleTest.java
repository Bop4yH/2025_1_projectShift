package shift.figures;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.shift.exceptions.IllegalNumberOfParametersException;
import ru.shift.figures.Rectangle;
import ru.shift.figures.ShapeType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RectangleTest {
    final static double a = 3;
    final static double b = 12;
    static Rectangle rectangle;

    @BeforeAll
    static void beforeAll() {
        rectangle = Rectangle.ofSides(a, b);
    }

    @Test
    void testEvaluateArea() {
        double exp = a * b;
        double act = rectangle.getArea();
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void testGetShapeType() {
        ShapeType exp = ShapeType.RECTANGLE;
        ShapeType act = rectangle.getShapeType();
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void testEvaluatePerimeter() {
        double exp = 2 * (a + b);
        double act = rectangle.getPerimeter();
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void testGetA() {
        double exp = Math.max(a, b);
        double act = rectangle.getA();
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void testGetB() {
        double exp = Math.min(a, b);
        double act = rectangle.getB();
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void testGetDiagonal() {
        double exp = Math.sqrt(a * a + b * b);
        double act = rectangle.getDiagonal();
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void whenNotTwoArgumentsThenException() {
        String params = "10 8 12";
        assertThatThrownBy(() -> Rectangle.fromString(params)).isInstanceOf(IllegalNumberOfParametersException.class);
    }

}