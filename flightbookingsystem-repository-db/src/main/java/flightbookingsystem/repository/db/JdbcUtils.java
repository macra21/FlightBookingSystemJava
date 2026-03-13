package flightbookingsystem.repository.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private static String url;
    static {
        Properties props = new Properties();
        try (InputStream is = JdbcUtils.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is == null) {
                throw new RuntimeException("File db.properties not found!");
            }
            props.load(is);
            url = props.getProperty("jdbc.url");
        } catch (IOException e) {
            throw new RuntimeException("Error reading from db: ", e);
        }
    }

    /**
     * Returns a new connection to the db.
     * <p>
     *     <strong>Must be closed by the caller!</strong>
     * </p>
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }
}