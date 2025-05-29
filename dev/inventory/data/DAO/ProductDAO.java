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
                INSERT INTO "Inventory"."Products" (
                product_id, product_name, product_manufacturer, 
                min_stock_level, group_id, location
                ) VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, product.getId());
            statement.setString(2, product.getName());
            statement.setString(3, product.getManufacturer());
            statement.setInt(4, product.getMinimumStockLevel());
            statement.setString(5, product.getCategoryGroupId());
            statement.setString(6, product.getLocation());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error saving product: " + e.getMessage());
        }
    }

    @Override
    public void updateProduct(Product product) {
        String sql = """
                UPDATE "Inventory"."Products"
                SET product_name = ?, product_manufacturer = ?, 
                    min_stock_level = ?, group_id = ?, 
                    location = ?
                WHERE product_id = ?
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, product.getName());
            statement.setString(2, product.getManufacturer());
            statement.setInt(3, product.getMinimumStockLevel());
            statement.setString(4, product.getCategoryGroupId());
            statement.setString(5, product.getLocation());
            statement.setString(6, product.getId());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error updating product: " + e.getMessage());
        }
    }

    @Override
    public void deleteProduct(String id) {
        String sql = """
                DELETE FROM "Inventory"."Products"
                WHERE product_id = ?
                """;

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
                SELECT *
                FROM "Inventory"."Products" p
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
                SELECT * FROM "Inventory"."Products" p
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
            int minimumStockLevel = res.getInt("min_stock_level");
            String category_group = res.getString("group_id");
            String location = res.getString("location");

            Product product = new Product(id, name, manufacturer, minimumStockLevel, category_group, location);

            return product;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error mapping ResultSet to Product: " + e.getMessage());
            return null; // Return null if mapping fails
        }
    }


    public void removeFromProductsByCategory(String productId) {
        String sql = """
                DELETE FROM "Inventory"."Product_by_Category"
                WHERE product_id = ?
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, productId);
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error removing products by category: " + e.getMessage());
        }
    }

    public boolean productExists(String name, String manufacturer) {
        String sql = """
                SELECT 1
                FROM "Inventory"."Products"
                WHERE product_name = ? AND product_manufacturer = ? 
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, manufacturer);
            ResultSet res = statement.executeQuery();

            return res.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }
}


