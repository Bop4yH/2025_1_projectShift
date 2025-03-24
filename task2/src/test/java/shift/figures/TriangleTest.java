package shift.figures;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.shift.exceptions.IllegalTriangleSidesLengthException;
import ru.shift.figures.ShapeType;
import ru.shift.figures.Triangle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TriangleTest {
    final static double a = 3;
    final static double b = 4;
    final static double c = 5;
    static Triangle triangle;

    @BeforeAll
    static void beforeAll() {
        triangle = Triangle.ofSides(a, b, c);
    }

    @Test
    void testGetA() {
        double act = triangle.getA();
        assertThat(act).isEqualTo(a);
    }

    @Test
    void testGetB() {
        double act = triangle.getB();
        assertThat(act).isEqualTo(b);
    }

    @Test
    void testGetC() {
        double act = triangle.getC();
        assertThat(act).isEqualTo(c);
    }

    @Test
    void testEvaluateAngleAgainstA() {
        double exp = Math.toDegrees(Math.acos((b * b + c * c - a * a) / (2 * b * c)));
        double act = triangle.getAngleAgainstA();
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void testEvaluateAngleAgainstB() {
        double exp = Math.toDegrees(Math.acos((a * a + c * c - b * b) / (2 * a * c)));
        double act = triangle.getAngleAgainstB();
        assertThat(act).isEqualTo(exp);

    }

    @Test
    void testEvaluateAngleAgainstC() {
        double exp = Math.toDegrees(Math.acos((a * a + b * b - c * c) / (2 * a * b)));
        double act = triangle.getAngleAgainstC();
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void testEvaluateArea() {
        double p = (a + b + c) / 2;
        double exp = Math.sqrt((p * (p - a) * (p - b) * (p - c)));
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
        double exp = a + b + c;
        double act = triangle.getPerimeter();
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void whenNotTriangleThenException() {
        String params = "3 4 12";
        assertThatThrownBy(() -> Triangle.fromString(params)).isInstanceOf(IllegalTriangleSidesLengthException.class);
    }
}