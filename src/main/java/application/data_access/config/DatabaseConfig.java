package application.data_access.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static final Properties properties = new Properties();
    private static String databaseUrl; 
    private static String databaseName;
    private static String user;
    private static String password;

    static {
        try (InputStream inputStream = DatabaseConfig.class.getClassLoader().getResourceAsStream("database.properties")) {
            properties.load(inputStream);
            databaseUrl = properties.getProperty("database.url");
            databaseName = properties.getProperty("database.name");
            user = properties.getProperty("database.username");
            password = properties.getProperty("database.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getUrl() {
        return databaseUrl;
    }

    public static void setUrl(String url) {
        databaseUrl = url;
    }

    public static String getDatabaseName() {
        return databaseName;
    }

    public static void setDatabaseName(String dbName) {
        databaseName = dbName;
    }

    public static String getUsername() {
        return user;
    }

    public static void setUsername(String username) {
        user = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String pass) {
        password = pass;
    }
}
