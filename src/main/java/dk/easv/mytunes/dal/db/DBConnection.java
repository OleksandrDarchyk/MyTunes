package dk.easv.mytunes.dal.db;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

public class DBConnection {
    public Connection getConnection() throws SQLServerException {
        SQLServerDataSource ds;
        ds = new SQLServerDataSource();
        ds.setDatabaseName("PlayerMP3"); // make this unique as names are shared on server
        ds.setUser("CSe2024b_e_22"); // Use your own username
        ds.setPassword("CSe2024bE22!24"); // Use your own password
        ds.setServerName("EASV-DB4");
        ds.setPortNumber(1433);
        ds.setTrustServerCertificate(true);
        return ds.getConnection();
        }
    }


    /*private static final String PROP_FILE = "config/config.settings";
    private SQLServerDataSource dataSource;

    public DBConnection() throws IOException {
        Properties databaseProperties = new Properties();


        // Use try-with-resources to safely close the FileInputStream
        try (FileInputStream fis = new FileInputStream(new File(PROP_FILE))) {
            databaseProperties.load(fis);
        } catch (IOException e) {
            throw new IOException("Error loading database properties file: " + e.getMessage(), e);
        }

        dataSource = new SQLServerDataSource();
        dataSource.setServerName(databaseProperties.getProperty("Server"));
        dataSource.setDatabaseName(databaseProperties.getProperty("Database"));
        dataSource.setUser(databaseProperties.getProperty("User"));
        dataSource.setPassword(databaseProperties.getProperty("Password"));
        dataSource.setPortNumber(Integer.parseInt(databaseProperties.getProperty("Port", "1433"))); // Default to 1433
        dataSource.setTrustServerCertificate(true); // Use caution for production environments
    }

    public Connection getConnection() throws SQLServerException {
        return dataSource.getConnection();
    }

    public static void main(String[] args) {
        try {
            DBConnection databaseConnector = new DBConnection();

            try (Connection connection = databaseConnector.getConnection()) {
                System.out.println("Connection is open: " + !connection.isClosed());
            } // Connection gets closed here
        } catch (Exception e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }
}*/



