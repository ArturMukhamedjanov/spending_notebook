package application;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import application.cli.MainCommands;
import application.data_access.CategoryDAOImpl;
import application.data_access.EntityManagerProvider;
import application.data_access.MccDAOImpl;
import application.data_access.TransactionDAOImpl;
import application.data_access.config.DatabaseConfig;
import application.service.*;
import picocli.CommandLine;
import application.service.DAO.*;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
    private CategoryService categoryService;
    private MccService mccService;
    private TransactionService spendService;
    private MccDAO mccDAO;
    private CategoryDAO categoryDAO;
    private TransactionDAO spendDAO;

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public void start(String[] args){
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger logger = loggerContext.getLogger("org.hibernate");
        logger.setLevel(Level.ERROR);
        if(args.length > 0){
            overrideDatabaseConfig(args);
        }
        createDbIfNotExists();
        setEnityManagerConfig();
        initDBConnection();
        loadDatabaseConnection();
        loadServices();
        startConsoleListenning();
    }

    private void overrideDatabaseConfig(String args[]){
        if(args.length != 4){
            logger.error("Wrong number of arguments. Watch Readme.md to see correct input");
            System.exit(0);
        }
        DatabaseConfig.setUrl(args[0]);
        DatabaseConfig.setDatabaseName(args[1]);
        DatabaseConfig.setUsername(args[2]);
        DatabaseConfig.setPassword(args[3]);
    }

    private void createDbIfNotExists() {
        String url = DatabaseConfig.getUrl();
        String dbName = DatabaseConfig.getDatabaseName();
        String username = DatabaseConfig.getUsername();
        String password = DatabaseConfig.getPassword();
        String checkDatabaseQuery = "SELECT datname FROM pg_database WHERE datname = '" + dbName + "'";
        String createDatabaseQuery = "CREATE DATABASE " + dbName;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(checkDatabaseQuery);
            if (!resultSet.next()) {
                statement.executeUpdate(createDatabaseQuery);
                logger.info("Database created successfully!");
            } else {
                logger.info("Database already exists.");
            }
        } catch (SQLException e) {
            logger.error("Error creating or checking database: {}", e.getMessage());
        }
    }

    private void setEnityManagerConfig(){
        try{
            Map<String, String> properties = new HashMap<>();
            properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
            properties.put("javax.persistence.jdbc.url", DatabaseConfig.getUrl() + DatabaseConfig.getDatabaseName());
            properties.put("javax.persistence.jdbc.user", DatabaseConfig.getUsername());
            properties.put("javax.persistence.jdbc.password", DatabaseConfig.getPassword());
            properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            properties.put("hibernate.hbm2ddl.auto", "update");
            EntityManagerProvider.setEnityManagerFactory(properties);
        }catch(Exception e){
            logger.error("Error accessing database:", e.getMessage());
        }
    }

    private void initDBConnection(){
        EntityManagerProvider.ConnectToDB();
    }

    private void loadDatabaseConnection(){
        mccDAO = new MccDAOImpl();
        categoryDAO = new CategoryDAOImpl();
        spendDAO = new TransactionDAOImpl();
    }

    private void loadServices(){
        categoryService = new CategoryService(categoryDAO);
        mccService = new MccService(mccDAO);
        spendService = new TransactionService(spendDAO);
    }

    private void startConsoleListenning(){
        Scanner scanner = new Scanner(System.in, "UTF-8");
        CommandLine commandLine = new CommandLine(new MainCommands(categoryService, mccService, spendService));
        System.out.println("Введите команду (для выхода введите 'exit'):");
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Выход из приложения...");
                break;
            }
            commandLine.execute(input.split(" "));
        }
        scanner.close();
    }

}
