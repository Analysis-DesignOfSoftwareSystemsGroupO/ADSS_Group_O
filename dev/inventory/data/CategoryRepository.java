package inventory.data;

import inventory.domain.Category;

import java.util.List;

public interface CategoryRepository {
    void saveCategory(Category category);
    void updateCategory(Category category);
    void deleteCategory(String id);
    Category getCategoryById(String id);
    List<Category> getAllCategories();
    void printAllCategories();
}
