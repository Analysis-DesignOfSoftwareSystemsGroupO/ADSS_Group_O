package inventory.domain;

import java.util.Objects;

public class Category {
    private String id;
    private String name;
    private Category parentCategory;

    public Category(String id, String name) {
        Objects.requireNonNull(id, "Category id cannot be null");
        Objects.requireNonNull(name, "Category name cannot be null");
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Objects.requireNonNull(name, "Category name cannot be null");
        this.name = name;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

}


