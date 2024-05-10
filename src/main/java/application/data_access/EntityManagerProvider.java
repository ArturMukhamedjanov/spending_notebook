package application.data_access;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.*;

public class EntityManagerProvider {

    private static final Logger logger = Logger.getLogger(EntityManagerProvider.class.getName());
    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;

    public static void ConnectToDB(){
        try{
            entityManager = entityManagerFactory.createEntityManager();
        }catch(Exception e){
            logger.log(Level.SEVERE, "Unable to connect to database. Check login and password in database.properties.", e.getMessage());
            System.exit(0);
        }
    }

    public static EntityManager getEntityManager() {
        return entityManager;
    }

    public static void setEnityManagerFactory(Map<String, String> properties){
        try{
            entityManagerFactory = Persistence.createEntityManagerFactory("SpendingNotebookPU", properties);
        }catch(Exception e){
            logger.log(Level.SEVERE, "Unable to connect to db. Check url, login and password.", e.getMessage());
            System.exit(0);
        }
    }

    /*public static void closeEntityManager() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }*/
}
