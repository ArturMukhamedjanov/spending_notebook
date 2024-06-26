package application.data_access;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

import application.models.Mcc;
import application.service.DAO.MccDAO;

public class MccDAOImpl implements MccDAO {

    private static final Logger logger = Logger.getLogger(MccDAOImpl.class.getName());

    @Override
    public void saveMcc(Mcc mcc) {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(mcc);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, "Error occurred while saving MCC.", e.getMessage());
        }
    }

    @Override
    public Mcc getMccById(Long id) {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        return entityManager.find(Mcc.class, id);
    }

    @Override
    public List<Mcc> getAllMccs() {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        return entityManager.createQuery("SELECT m FROM Mcc m").getResultList();
    }

    @Override
    public void updateMcc(Mcc mcc) {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(mcc);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, "Error occurred while updating MCC.", e.getMessage());
        }
    }

    @Override
    public void deleteMcc(Long id) {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Mcc mcc = entityManager.find(Mcc.class, id);
            if (mcc != null) {
                entityManager.remove(mcc);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, "Error occurred while deleting MCC.", e.getMessage());
        }
    }

    @Override
    public Mcc getMccByCode(String code) {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        try {
            return (Mcc) entityManager.createQuery("SELECT m FROM Mcc m WHERE m.code = :code")
                    .setParameter("code", code)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
