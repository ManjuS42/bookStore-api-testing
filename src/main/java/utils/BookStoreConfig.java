package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BookStoreConfig {

  private static final Properties environmentProperties = new Properties();

  static {
    String envFilePath = "application-UATB.properties";
    try (InputStream input = BookStoreConfig.class.getClassLoader().getResourceAsStream(envFilePath)) {
      if (input == null) {
        throw new IllegalStateException("Unable to locate the environment configuration file: " + envFilePath);
      }
      environmentProperties.load(input);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to load environment configuration file: " + envFilePath, e);
    }
  }

  public static String get(String key) {
    return environmentProperties.getProperty(key);
  }

  public static String getBaseUri() {
    String value = environmentProperties.getProperty("bookstore.service.url");
    System.out.println("URL=" + value);
    if (value == null || value.isEmpty()) {
      throw new RuntimeException("bookstore.service.url not found");
    }
    return value;
  }


}
