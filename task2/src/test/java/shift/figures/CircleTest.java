package shift.figures;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.shift.exceptions.NegativeParameterException;
import ru.shift.figures.Circle;
import ru.shift.figures.ShapeType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CircleTest {
    static final double RADIUS = 10;
    static Circle circle;

    @BeforeAll
    static void beforeAll() {
        circle = Circle.ofRadius(RADIUS);
    }

    @Test
    void testGetRadius() {
        double act = circle.getRadius();
        assertThat(act).isEqualTo(RADIUS);
    }

    @Test
    void testEvaluateArea() {
        double exp = RADIUS * RADIUS * Math.PI;
        double act = circle.getArea();
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void testGetShapeType() {
        ShapeType exp = ShapeType.CIRCLE;
        ShapeType act = circle.getShapeType();
        assertThat(act).isEqualTo(exp);
    }


    @Test
    void testGetPerimeter() {
        double exp = 2 * Math.PI * RADIUS;
        double act = circle.getPerimeter();
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void testGetDiameter() {
        double exp = RADIUS * 2;
        double act = circle.getDiameter();
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void whenNegativeRadiusThenException() {
        String radius = "-10.0";
        assertThatThrownBy(() -> Circle.fromString(radius)).isInstanceOf(NegativeParameterException.class);
    }

}