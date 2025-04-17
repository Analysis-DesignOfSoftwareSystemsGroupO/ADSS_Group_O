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

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", parentCategory=" + (parentCategory != null ? parentCategory.getName() : "None") +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return id.equals(category.id);
    }

}


