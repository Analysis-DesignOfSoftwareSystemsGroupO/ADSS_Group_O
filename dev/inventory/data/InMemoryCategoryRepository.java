package inventory.data;

import inventory.domain.Category;

import java.util.ArrayList;
import java.util.List;

public class InMemoryCategoryRepository implements CategoryRepository{
    private static final List<Category> categories = new ArrayList<>();

    public static void saveCategory(Category category) {
        categories.add(category);
    }

    public static void updateCategory(Category category) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId().equals(category.getId())) {
                categories.set(i, category);
                return;
            }
        }
    }

    public static void deleteCategory(String id) {
        categories.removeIf(category -> category.getId().equals(id));
    }

    public static Category getCategoryById(String id) {
        for (Category category : categories) {
            if (category.getId().equals(id)) {
                return category;
            }
        }
        return null;
    }

    public static List<Category> getAllCategories() {
        return new ArrayList<>(categories);
    }

    public static void printAllCategories() {
        for (Category category : getAllCategories()) {
            System.out.println(category);
        }
    }
}