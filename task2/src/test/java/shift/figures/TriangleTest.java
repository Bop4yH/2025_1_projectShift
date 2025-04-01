package shift.figures;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.shift.exceptions.IllegalTriangleSidesLengthException;
import ru.shift.figures.ShapeType;
import ru.shift.figures.Triangle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TriangleTest {
    static final  double A = 3;
    static final  double B = 4;
    static final  double C = 5;
    static Triangle triangle;

    @BeforeAll
    static void beforeAll() {
        triangle = Triangle.fromSides(A, B, C);
    }

    @Test
    void testGetA() {
        double act = triangle.getA();
        assertThat(act).isEqualTo(A);
    }

    @Test
    void testGetB() {
        double act = triangle.getB();
        assertThat(act).isEqualTo(B);
    }

    @Test
    void testGetC() {
        double act = triangle.getC();
        assertThat(act).isEqualTo(C);
    }

    @Test
    void testEvaluateAngleAgainstA() {
        double exp = Math.toDegrees(Math.acos((B * B + C * C - A * A) / (2 * B * C)));
        double act = triangle.getAngleAgainstA();
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void testEvaluateAngleAgainstB() {
        double exp = Math.toDegrees(Math.acos((A * A + C * C - B * B) / (2 * A * C)));
        double act = triangle.getAngleAgainstB();
        assertThat(act).isEqualTo(exp);

    }

    @Test
    void testEvaluateAngleAgainstC() {
        double exp = Math.toDegrees(Math.acos((A * A + B * B - C * C) / (2 * A * B)));
        double act = triangle.getAngleAgainstC();
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void testEvaluateArea() {
        double p = (A + B + C) / 2;
        double exp = Math.sqrt((p * (p - A) * (p - B) * (p - C)));
        double act = triangle.getArea();
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void testGetShapeType() {
        ShapeType exp = ShapeType.TRIANGLE;
        ShapeType act = triangle.getShapeType();
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void testEvaluatePerimeter() {
        double exp = A + B + C;
        double act = triangle.getPerimeter();
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void whenNotTriangleThenException() {
        String params = "3 4 12";
        assertThatThrownBy(() -> Triangle.fromString(params)).isInstanceOf(IllegalTriangleSidesLengthException.class);
    }
}