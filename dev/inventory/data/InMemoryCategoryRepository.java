package inventory.data;

import inventory.domain.Category;

import java.util.ArrayList;
import java.util.List;

public class InMemoryCategoryRepository implements CategoryRepository{
    private final List<Category> categories = new ArrayList<>();

    public void saveCategory(Category category) {
        categories.add(category);
    }
    public void updateCategory(Category category) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId().equals(category.getId())) {
                categories.set(i, category);
                return;
            }
        }
    }
    public void deleteCategory(String id) {
        categories.removeIf(category -> category.getId().equals(id));
    }
    public Category getCategoryById(String id) {
        for (Category category : categories) {
            if (category.getId().equals(id)) {
                return category;
            }
        }
        return null;
    }
    public List<Category> getAllCategories() {
        return new ArrayList<>(categories);
    }
    public void printAllCategories() {
        for (Category category : categories) {
            System.out.println(category);
        }
    }


}
