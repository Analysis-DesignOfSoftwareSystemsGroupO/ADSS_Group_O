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

import static java.util.Collections.nCopies;

public class ProductDAO implements ProductRepository {
    // This class will handle database operations related to products,

    @Override
    public void saveProduct(Product product) {
        String sql = """
                INSERT INTO "Inventory"."Products" (
                product_id, product_name, product_manufacturer, 
                min_stock_level, group_id, location, selling_price
                ) VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, product.getId());
            statement.setString(2, product.getName());
            statement.setString(3, product.getManufacturer());
            statement.setInt(4, product.getMinimumStockLevel());
            statement.setString(5, product.getCategoryGroupId());
            statement.setString(6, product.getLocation());
            statement.setDouble(7, product.getSellingPrice());

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

    public List<Product> getProductsByCategoryNames(List<String> categoryNames) {
        List<Product> products = new ArrayList<>();
        if (categoryNames == null || categoryNames.isEmpty()) {
            return products;
        }

        String placeholders = String.join(",", nCopies(categoryNames.size(), "?"));

        String sql = """
                SELECT p.*
                FROM "Inventory"."Products" p
                JOIN "Inventory"."Products_by_Categories" pc ON p.product_id = pc.product_id
                JOIN "Inventory"."Categories" c ON pc.category_id = c.category_id
                WHERE c.category_name IN (""" + placeholders + ")";

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 0; i < categoryNames.size(); i++) {
                statement.setString(i + 1, categoryNames.get(i));
            }

            ResultSet res = statement.executeQuery();

            while (res.next()) {
                products.add(mapResultSetToProduct(res));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    public void saveSellingPrice(Product product) {
        String sql = """
                INSERT INTO "Inventory"."Selling_Prices" (
                product_id, selling_price
                ) VALUES (?, ?)
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, product.getId());
            statement.setDouble(2, product.getSellingPrice());


            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error saving product: " + e.getMessage());
        }

    }

    public void updateSellingPriceperProduct(Product product) {
        String sql = """
        UPDATE "Inventory"."Products" p
        SET selling_price = sp.effective_price
        FROM (
            SELECT product_id,
                   COALESCE(discount_selling_price, selling_price) AS effective_price
            FROM "Inventory"."Selling_Prices"
            WHERE product_id = ?
        ) sp
        WHERE p.product_id = sp.product_id
          AND p.product_id = ?
    """;

        try (Connection connection = DataBaseConnector.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, product.getId());
                statement.setString(2, product.getId());

                int rowsUpdated = statement.executeUpdate();
                connection.commit();

                if (rowsUpdated > 0) {
                    System.out.println("Updated selling price for product " + product.getId());
                } else {
                    System.out.println("No update performed for product " + product.getId());
                }
            } catch (Exception e) {
                connection.rollback();
                System.err.println("Error updating selling price: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void updateAllProductSellingPricesInBulk() {
        String sql = """
        UPDATE "Inventory"."Products" p
        SET selling_price = sp.effective_price
        FROM (
            SELECT product_id,
                   COALESCE(discount_selling_price, selling_price) AS effective_price
            FROM "Inventory"."Selling_Prices"
        ) sp
        WHERE p.product_id = sp.product_id
          AND p.selling_price <> sp.effective_price
    """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int updatedRows = statement.executeUpdate();
            System.out.println("Bulk update complete. Rows updated: " + updatedRows);

        } catch (Exception e) {
            System.err.println("Error during bulk selling price update.");
            e.printStackTrace();
        }
    }


}