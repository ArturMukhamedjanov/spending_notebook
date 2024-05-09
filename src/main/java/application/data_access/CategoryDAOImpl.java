package application.data_access;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import application.models.Category;
import application.service.DAO.CategoryDAO;

public class CategoryDAOImpl implements CategoryDAO {

    @Override
    public void saveCategory(Category category) {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(category);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public Category getCategoryById(Long id) {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        return entityManager.find(Category.class, id);

    }

    @Override
    public List<Category> getAllCategories() {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        return entityManager.createQuery("SELECT c FROM Category c").getResultList();

    }

    @Override
    public void updateCategory(Category category) {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(category);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCategory(Long id) {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Category category = entityManager.find(Category.class, id);
            category.removeAllChildren();
            if (category != null) {
                entityManager.remove(category);
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
    public Category getCategoryByName(String name) {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        try {
            return (Category) entityManager.createQuery("SELECT c FROM Category c WHERE c.name = :name")
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


}
