package connection;

import lombok.Getter;
import java.io.*;
import java.util.Properties;

@Getter
public class PropertiesReader {
    private String DB_URL;
    private String DB_USERNAME;
    private String DB_PASSWORD;
    private String DRIVER;

    public PropertiesReader() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream reader = loader.getResourceAsStream("hibernate.properties");

        Properties properties = new Properties();
        try {
            properties.load(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DB_URL = properties.getProperty("hibernate.connection.url");
        DB_USERNAME = properties.getProperty("hibernate.connection.username");
        DB_PASSWORD = properties.getProperty("hibernate.connection.password");
        DRIVER = properties.getProperty("hibernate.connection.driver_class");
    }

}
