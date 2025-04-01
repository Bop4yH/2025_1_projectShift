package shift.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.shift.util.Options;

import java.nio.file.Path;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class OptionsTest {
    static final String[] ARGS = {"in.txt", "-o", "out.txt"};
    static Options options;

    @BeforeAll
    static void beforeAll() {
        options = Options.parseCommandLine(ARGS);
    }

    @Test
    void testGetInputPath() {
        String exp = ARGS[0];
        String act = options.getInputPath().toString();
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void testGetOutputPath() {
        String exp = ARGS[2];
        String act = options.getOutputPath().toString();
        assertThat(act).isEqualTo(exp);

        String[] argsNull = {"in.txt"};
        Path act2 = Objects.requireNonNull(Options.parseCommandLine(argsNull)).getOutputPath();
        assertThat(act2).isNull();
    }

    @Test
    void testIsValid() {
        boolean exp = true;
        boolean act = options.isValid();
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void whenNoArgumentsThenNull() {
        String[] args = {"-o", "out.txt"};
        Options act = Options.parseCommandLine(args);

        assertThat(act).isNull();
    }

    @Test
    void whenMoreThanOneArgumentThenNull() {
        String[] args = {"-o", "out.txt", "in1.txt", "in2.txt"};
        Options act = Options.parseCommandLine(args);
        assertThat(act).isNull();
    }

    @Test
    void testIsFileMode() {
        boolean exp = true;
        boolean act = options.isFileMode();
        assertThat(act).isEqualTo(exp);

        String[] args = {"in.txt"};
        boolean exp2 = false;
        boolean act2 = Objects.requireNonNull(Options.parseCommandLine(args)).isFileMode();
        assertThat(act2).isEqualTo(exp2);
    }
}