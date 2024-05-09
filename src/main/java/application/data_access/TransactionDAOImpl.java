package application.data_access;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import application.models.Mcc;
import application.models.Month;
import application.models.Transaction;
import application.service.DAO.TransactionDAO;

public class TransactionDAOImpl implements TransactionDAO {
    @Override
    public void saveTransaction(Transaction transactionModel) { // Изменено имя метода saveSpend на saveTransaction
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(transactionModel);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public Transaction getTransactionById(Long id) { // Изменено имя метода getSpendById на getTransactionById
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        return entityManager.find(Transaction.class, id);
    }

    @Override
    public List<Transaction> getAllTransactions() { // Изменено имя метода getAllSpends на getAllTransactions
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        return entityManager.createQuery("SELECT t FROM Transaction t").getResultList();
    }

    @Override
    public void updateTransaction(Transaction transactionModel) { // Изменено имя метода updateSpend на updateTransaction
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(transactionModel);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTransaction(Long id) { // Изменено имя метода deleteSpend на deleteTransaction
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Transaction transactionModel = entityManager.find(Transaction.class, id);
            if (transactionModel != null) {
                entityManager.remove(transactionModel);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<Transaction> getTransactionsByNameAndValueAndMonth(String name, double value, Month month) {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        TypedQuery<Transaction> query = entityManager.createQuery(
                "SELECT t FROM Transaction t WHERE t.name = :name AND t.value = :value AND t.month = :month",
                Transaction.class
        );
        query.setParameter("name", name);
        query.setParameter("value", value);
        query.setParameter("month", month);
        return query.getResultList();
    }

    @Override
    public List<Transaction> getTransactionsByMccAndMonth(Mcc mcc, Month month) {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        return entityManager.createQuery("SELECT t FROM Transaction t WHERE t.mcc = :mcc AND t.month = :month")
                .setParameter("mcc", mcc)
                .setParameter("month", month)
                .getResultList();
    }

}