package connection;

import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;

public class Storage {
    private static Storage INSTANCE;
    private Connection connection;

    private Storage() {
        try {
            PropertiesReader propertiesReader = new PropertiesReader();

            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);
            Class.forName(propertiesReader.getDRIVER());

            String url = propertiesReader.getDB_URL();
            String username = propertiesReader.getDB_USERNAME();
            String password = propertiesReader.getDB_PASSWORD();
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Storage getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Storage();
        }
        return INSTANCE;
    }

    public Connection getConnection() {
        return connection;
    }
}

