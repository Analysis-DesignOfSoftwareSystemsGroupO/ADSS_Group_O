package inventory.data;

import inventory.domain.Category;

import java.util.List;

public interface CategoryRepository {
    static void saveCategory(Category category) {}

    static void updateCategory(Category category) {}

    static void deleteCategory(String id) {}

    static Category getCategoryById(String id) { return null; }

    static List<Category> getAllCategories() { return null; }
}
