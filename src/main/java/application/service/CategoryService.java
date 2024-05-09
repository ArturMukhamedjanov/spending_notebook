package application.service;

import java.util.ArrayList;
import java.util.List;

import application.models.Category;
import application.service.DAO.CategoryDAO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CategoryService {

    private CategoryDAO categoryDAO;

    public void saveCategory(Category category) {
        categoryDAO.saveCategory(category);
    }

    public Category getCategoryById(Long id) {
        return categoryDAO.getCategoryById(id);
    }

    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    public void updateCategory(Category category) {
        categoryDAO.updateCategory(category);
    }

    public void deleteCategory(Long id) {
        categoryDAO.deleteCategory(id);
    }

    public Category getCategoryByName(String name){
        return categoryDAO.getCategoryByName(name);
    }

    public boolean isCategoryNameExists(String name){
        Category category = categoryDAO.getCategoryByName(name);
        if(category == null){
            return false;
        }
        return true;

    }

    public boolean isSubChildren(Category mainCategory, Category needCategory){
        for(Category childCategory : mainCategory.getChildren()){
            if(childCategory.getId() == needCategory.getId()){
                return true;
            }
            return isSubChildren(childCategory, needCategory);
        }
        return false;
    }

    public List<Category> getRootCategories() {
        List<Category> categories = new ArrayList<>();
        for(Category category : categoryDAO.getAllCategories()){
            if(category.getParents().isEmpty()){
                categories.add(category);
            }
        }
        return categories;
    }
}
