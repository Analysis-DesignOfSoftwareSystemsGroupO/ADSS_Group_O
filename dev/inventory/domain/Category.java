package inventory.domain;

import inventory.data.CategoryRepository;
import inventory.data.InMemoryCategoryRepository;

import java.util.Objects;
import java.util.ArrayList;
import java.util.UUID;

public class  Category {
    private String id;
    private String name;
    private Category parentCategory;
    ArrayList<Category> subCategories;
    ArrayList<Product> products;

    public Category( String name) {
        Objects.requireNonNull(name, "Category name cannot be null");
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.subCategories = new ArrayList<Category>();
        this.products = new ArrayList<Product>();

        InMemoryCategoryRepository.saveCategory(this);
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public ArrayList<Category> getSubCategories() {
        return subCategories;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }


    public void setName(String name) {
        Objects.requireNonNull(name, "Category name cannot be null");
        this.name = name;
        InMemoryCategoryRepository.updateCategory(this);
    }

    public void setParentCategory(Category parentCategory) {
        if (this.parentCategory != null) {
            throw new IllegalArgumentException("Category's current parent directory is not null, " +
                    "changing parent category is not supported");
        }
        this.parentCategory = parentCategory;
        InMemoryCategoryRepository.updateCategory(this);
    }

    public void addSubCategory(Category newSubCategory) {
        Objects.requireNonNull(newSubCategory, "Category to add cannot be null");
        if(this.subCategories.contains(newSubCategory)) {
            return; // No need to re-add it
        }
        this.subCategories.add(newSubCategory);
        InMemoryCategoryRepository.updateCategory(this);
    }

    public void removeSubCategory(Category subCategory) {
        Objects.requireNonNull(subCategory, "Category to remove cannot be null");
        this.subCategories.remove(subCategory);
        InMemoryCategoryRepository.updateCategory(this);
    }

    public void addProduct(Product newProduct) {
        Objects.requireNonNull(newProduct, "Product to add cannot be null");
        if(this.products.contains(newProduct)) {
            return; // No need to re-add it
        }
        this.products.add(newProduct);
        InMemoryCategoryRepository.updateCategory(this);
    }

    public void removeProduct(Product product) {
        Objects.requireNonNull(product, "Product to remove cannot be null");
        this.products.remove(product);
        InMemoryCategoryRepository.updateCategory(this);
    }

    @Override
    public String toString() {
        String products = "";
        for (Product product : this.products) {
            products += "'" + product.getName() + "', ";
        }
        String subCategoriesString = "";
        for (Category category : this.subCategories) {
            subCategoriesString += "'" + category.getName() + "', ";
        }
        return "Category {" +
                "\n\tid = '" + id + "', " +
                "\n\tname = '" + name + "', " +
                "\n\tproducts = " + products +
                "\n\tsubCategories = " + subCategoriesString +
                "\n\tparentCategory = '" + (parentCategory != null ? parentCategory.getName() : "None") + "'" +
                "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return id.equals(category.id);
    }


}