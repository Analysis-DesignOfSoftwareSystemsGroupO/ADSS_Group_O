package inventory.data.DTO;

import inventory.domain.Category;
import inventory.domain.Product;

import java.util.ArrayList;

public class CategoryDTO {
    public String id;
    public String name;
    public Category parentCategory;
    ArrayList<CategoryDTO> subCategories;
    ArrayList<Product> products;

    public CategoryDTO(String id, String name, Category parentCategory, ArrayList<CategoryDTO> subCategories,
                       ArrayList<Product> products) {
        this.id = id;
        this.name = name;
        this.parentCategory = parentCategory;
        this.subCategories = subCategories;
        this.products = products;
    }


}
