package ru.shift.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Config {

   private static final Logger log = LoggerFactory.getLogger(Config.class);

   private Config() {
   }

   public static Optional<ServerConfig> loadConfig(String resourceName) {
      Properties props = new Properties();

      try (InputStream is = Config.class.getClassLoader().getResourceAsStream(resourceName)) {
         if (is == null) {
            log.error("Resource '{}' not found", resourceName);
            return Optional.empty();
         }
         props.load(is);

         return Optional.of(new ServerConfig(parseInt(props, "port")));
      } catch (IOException | NumberFormatException | NullPointerException e) {
         log.error("Error loading config from file: {}", resourceName, e);
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
