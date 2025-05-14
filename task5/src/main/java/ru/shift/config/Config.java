package ru.shift.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

import static ru.shift.Main.log;

public class Config {
    private Config() {
    }

    public static Optional<ProducerConsumerConfig> loadConfig(Path filePath) {
        Properties props = new Properties();

        try (InputStream is = Files.newInputStream(filePath)) {
            props.load(is);

            return Optional.of(new ProducerConsumerConfig(
                    parseInt(props, "producerCount"),
                    parseInt(props, "consumerCount"),
                    parseInt(props, "producerTime"),
                    parseInt(props, "consumerTime"),
                    parseInt(props, "storageSize")
            ));

        } catch (IOException | NumberFormatException | NullPointerException e) {
            log.error("Error loading config from file: {}", filePath, e);
            return Optional.empty();
        }
    }

    private static int parseInt(Properties props, String key) {
        String value = props.getProperty(key);
        if (value == null) {
            throw new NullPointerException("Missing config key: " + key);
        }
        return Integer.parseInt(value);
    }
}
