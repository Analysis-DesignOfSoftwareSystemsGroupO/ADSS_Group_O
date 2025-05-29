package inventory.data.DAO;

import inventory.data.ProductRepository;
import inventory.data.connection.DataBaseConnector;
import inventory.domain.Category;
import inventory.domain.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements ProductRepository {
    // This class will handle database operations related to products,

    @Override
    public void saveProduct(Product product) {
        String sql = """
                INSERT INTO "Products" (
                product_id, product_name, product_manufacturer, 
                cost_price, min_stock_level, group_id, location
                ) VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, product.getId());
            statement.setString(2, product.getName());
            statement.setString(3, product.getManufacturer());
            statement.setDouble(4, product.getCostPrice());
            statement.setInt(5, product.getMinimumStockLevel());
            statement.setString(6, product.getCategory().getId());
            statement.setString(7, product.getLocation());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error saving product: " + e.getMessage());
        }
    }

    @Override
    public void updateProduct(Product product) {
        String sql = """
                UPDATE "Products"
                SET product_name = ?, product_manufacturer = ?, 
                    cost_price = ?, min_stock_level = ?, group_id = ?, 
                    location = ?
                WHERE product_id = ?
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, product.getName());
            statement.setString(2, product.getManufacturer());
            statement.setDouble(3, product.getCostPrice());
            statement.setInt(4, product.getMinimumStockLevel());
            statement.setString(5, product.getCategory().getId());
            statement.setString(6, product.getLocation());
            statement.setString(7, product.getId());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error updating product: " + e.getMessage());
        }
    }

    @Override
    public void deleteProduct(String id) {
        String sql = "DELETE FROM \"Products\" WHERE product_id = ?";

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error deleting product: " + e.getMessage());
        }
    }

    @Override
    public Product getProductById(String id) {
        String sql = """
                SELECT 
                    p.*,
                    cg.parent_category_id, cg.sub_category_id,    cg.sub_sub_category_id,
                    c1.category_name AS parent_name,
                    c2.category_name AS sub_name,
                    c3.category_name AS sub_sub_name
                FROM "inventory"."Products" p
                JOIN "inventory"."CategoryGroups" cg ON p.group_id = cg.group_id
                JOIN "inventory"."Categories" c1 ON cg.parent_category_id = c1.category_id
                JOIN "inventory"."Categories" c2 ON cg.sub_category_id = c2.category_id
                JOIN "inventory"."Categories" c3 ON cg.sub_category_id2 = c3.category_id
                WHERE p.product_id = ?
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);
            ResultSet res = statement.executeQuery();

            if (res.next()) {
                return mapResultSetToProduct(res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Placeholder return statement
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = """
                SELECT 
                    p.*,
                    cg.parent_category_id, cg.sub_category_id,    cg.sub_sub_category_id,
                    c1.category_name AS parent_name,
                    c2.category_name AS sub_name,
                    c3.category_name AS sub_sub_name
                FROM "inventory"."Products" p
                JOIN "inventory"."CategoryGroups" cg ON p.group_id = cg.group_id
                JOIN "inventory"."Categories" c1 ON cg.parent_category_id = c1.category_id
                JOIN "inventory"."Categories" c2 ON cg.sub_category_id = c2.category_id
                JOIN "inventory"."Categories" c3 ON cg.sub_category_id2 = c3.category_id
                WHERE p.product_id = ?
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet res = statement.executeQuery()) {

            while (res.next()) {
                products.add(mapResultSetToProduct(res));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    private Product mapResultSetToProduct(ResultSet res) {
        try {
            String id = res.getString("product_id");
            String name = res.getString("product_name");
            String manufacturer = res.getString("product_manufacturer");
            double costPrice = res.getDouble("cost_price");
            int minimumStockLevel = res.getInt("min_stock_level");
            String category_group = res.getString("group_id"); //TODO: Fetch categories by ID's
            String location = res.getString("location");

            Product product = new Product(id, name, manufacturer, costPrice, minimumStockLevel, location);

            // Category mapping
            String parentId = res.getString("parent_category_id");
            String subId = res.getString("sub_category_id");
            String subSubId = res.getString("sub_sub_category_id");

            String parentName = res.getString("parent_name");
            String subName = res.getString("sub_name");
            String subSubName = res.getString("sub_sub_name");

            Category parentCategory = new Category(parentId, parentName);
            Category subCategory = new Category(subId, subName);
            Category subSubCategory = new Category(subSubId, subSubName);

            parentCategory.addSubCategory(subCategory);
            subCategory.addSubCategory(subSubCategory);

            product.setCategory(parentCategory);

            parentCategory.addProduct(product);
            subCategory.addProduct(product);
            subSubCategory.addProduct(product);


            return product;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error mapping ResultSet to Product: " + e.getMessage());
            return null; // Return null if mapping fails
        }
    }
}
