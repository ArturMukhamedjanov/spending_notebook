import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import application.models.Category;
import application.service.CategoryService;
import application.service.DAO.CategoryDAO;

public class CategoryServiceTest {

    private CategoryDAO categoryDAO;
    private CategoryService categoryService;

    @Before
    public void setUp() {
        categoryDAO = mock(CategoryDAO.class);
        categoryService = new CategoryService(categoryDAO);
    }

    @Test
    public void testSaveCategory() {
        Category category = new Category();
        categoryService.saveCategory(category);
        verify(categoryDAO).saveCategory(category);
    }

    @Test
    public void testGetCategoryById() {
        Long categoryId = 1L;
        Category expectedCategory = new Category();
        when(categoryDAO.getCategoryById(categoryId)).thenReturn(expectedCategory);

        Category result = categoryService.getCategoryById(categoryId);

        assertEquals(expectedCategory, result);
    }

    @Test
    public void testGetAllCategories() {
        List<Category> expectedCategories = new ArrayList<>();
        when(categoryDAO.getAllCategories()).thenReturn(expectedCategories);

        List<Category> result = categoryService.getAllCategories();

        assertEquals(expectedCategories, result);
    }

    // Добавьте другие тесты для остальных методов...
}
