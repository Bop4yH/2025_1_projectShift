package shift.figures;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.shift.exceptions.IllegalNumberOfParametersException;
import ru.shift.figures.Rectangle;
import ru.shift.figures.ShapeType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RectangleTest {
    static final double A = 3;
    static final double B = 12;

    static Rectangle rectangle;

    @BeforeAll
    static void beforeAll() {
        rectangle = Rectangle.fromSides(A, B);
    }

    @Test
    void testEvaluateArea() {
        double exp = A * B;
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
        double exp = 2 * (A + B);
        double act = rectangle.getPerimeter();
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void testGetA() {
        double exp = Math.max(A, B);
        double act = rectangle.getA();
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void testGetB() {
        double exp = Math.min(A, B);
        double act = rectangle.getB();
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void testGetDiagonal() {
        double exp = Math.sqrt(A * A + B * B);
        double act = rectangle.getDiagonal();
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void whenNotTwoArgumentsThenException() {
        String params = "10 8 12";
        assertThatThrownBy(() -> Rectangle.fromString(params)).isInstanceOf(IllegalNumberOfParametersException.class);
    }

}