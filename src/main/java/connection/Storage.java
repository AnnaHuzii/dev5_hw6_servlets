package connection;

import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Storage {
    private static final Storage INSTANCE = new Storage();

    private Connection connection;

    private Storage() {
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Не удалось загрузить класс драйвера");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Prefs prefs = new Prefs(Prefs.PREFS_FILENAME);

            String url = prefs.getString(Prefs.DB_URL);
            String username = prefs.getString(Prefs.DB_USERNAME);
            String password = prefs.getString(Prefs.DB_PASSWORD);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static Storage getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() {
        return connection;
    }

}
