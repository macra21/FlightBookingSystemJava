package com.org.FlightBookingSystem.utils.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Manages the database connections, using a thread-safe connection pool.
 * This class ensures efficient and safe management of database connections, by reusing existing connections if possible.
 */
public class DBConnectionManager {
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    private static final List<Connection> connectionPool = new ArrayList<>();
    private static final int POOL_SIZE = 5;
    // Not quite max, because it can grow indefinitely, but if there are more,
    // but if there are more than MAX_POOL_SIZE the connection will be closed.
    // Probably not the most efficient solution, but this will do until we are
    // allowed to use HikariCP
    private static final int MAX_POOL_SIZE = 10;


    static{
        Properties props = new Properties();
        try (InputStream input = DBConnectionManager.class.getClassLoader().getResourceAsStream("config.properties")) {
            props.load(input);
            URL = props.getProperty("db.mysql.url");
            USER = props.getProperty("db.mysql.user");
            PASSWORD = props.getProperty("db.mysql.pass");

            for (int i = 0; i < POOL_SIZE; i++){
                connectionPool.add(DriverManager.getConnection(URL, USER, PASSWORD));
            }
        } catch (IOException | SQLException e){
            System.err.println("Failed to load config.properties file: " + e.getMessage());
            throw new RuntimeException("Failed to load config.properties file: " + e.getMessage());
        }
    }

    /**
     * Private constructor to prevent this utility class from being instantiated.
     */
    private DBConnectionManager(){}

    /**
     * Creates a new database connection(only when necessary).
     * @return a new database {@link Connection}
     * @throws SQLException if a database error occurs
     */
    private static Connection createConnection() throws SQLException{
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Retrieves a connection from the connection pool.
     * @return {@link Connection}
     * @throws SQLException if a database error occurs
     */
    public static synchronized Connection getConnection() throws SQLException{
        if (connectionPool.isEmpty()){
            return createConnection();
        } else {
            return connectionPool.remove(connectionPool.size() - 1);
        }
    }

    /**
     * Releases a connection back to the pool or deletes it if there are too many.
     * @param connection the connection to release
     * @throws SQLException if a database error occurs
     */
    public static synchronized void releaseConnection(Connection connection) throws SQLException{
        if (connection != null && !connection.isClosed()){
            if(connectionPool.size() < MAX_POOL_SIZE){
                connectionPool.add(connection);
            } else {
                connection.close();
            }
        }
    }
}
