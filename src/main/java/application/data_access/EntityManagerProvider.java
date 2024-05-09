package application.data_access;

import java.util.Map;

import javax.persistence.*;

public class EntityManagerProvider {

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;

    public static void ConnectToDB(){
        try{
            entityManager = entityManagerFactory.createEntityManager();
        }catch(Exception e){
            System.out.println("Unable to connect to database. Check login and password in persistence.xml.");
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
            System.out.println("Unable to connect to db. Check url, login and password.");
            System.exit(0);
        }
    }

    /*public static void closeEntityManager() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }*/
}