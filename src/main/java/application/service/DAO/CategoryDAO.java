package application.service.DAO;

import java.util.List;

import application.models.Category;

public interface CategoryDAO {
    void saveCategory(Category category);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    void updateCategory(Category category);
    void deleteCategory(Long id);
    Category getCategoryByName(String name);
}