package shift.util;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.shift.figures.Circle;
import ru.shift.figures.Figure;
import ru.shift.figures.Rectangle;
import ru.shift.figures.Triangle;
import ru.shift.util.FigureFactory;

import java.io.BufferedReader;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class FigureFactoryTest {

    @Test
    void testRead_Figure_Circle() throws IOException {
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        when(mockReader.readLine()).thenReturn("CIRCLE", "5.0");

        Figure figure = FigureFactory.readFigure(mockReader);

        assertThat(figure).isInstanceOf(Circle.class);
        assertThat(((Circle) figure).getRadius()).isEqualTo(5.0);
    }

    @Test
    void testRead_Figure_Rectangle() throws IOException {
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        when(mockReader.readLine()).thenReturn("RECTANGLE", "3 4");

        Figure figure = FigureFactory.readFigure(mockReader);

        assertThat(figure).isInstanceOf(Rectangle.class);
        assertThat(((Rectangle) figure).getDiagonal()).isEqualTo(5.0);
    }

    @Test
    void testRead_Figure_Triangle() throws IOException {
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        when(mockReader.readLine()).thenReturn("TRIANGLE", "3 4 5");

        Figure figure = FigureFactory.readFigure(mockReader);

        assertThat(figure).isInstanceOf(Triangle.class);
        assertThat(((Triangle) figure).getAngleAgainstC()).isEqualTo(90);
    }
}