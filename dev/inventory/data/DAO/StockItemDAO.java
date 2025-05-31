package inventory.data.DAO;

import inventory.data.connection.DataBaseConnector;
import inventory.domain.Product;
import inventory.domain.StockItem;
import inventory.domain.StockItemStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StockItemDAO {
    private final ProductDAO productDAO;

    public StockItemDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public void saveStockItem(StockItem stockItem) {
        String sql = """
                INSERT INTO "Inventory"."Stock_Items" 
                ("stock_id","quantity", "location", "expiry_date",  "status", "product_id") 
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, stockItem.getId());
            statement.setInt(2, stockItem.getQuantity());
            statement.setString(3, stockItem.getLocation());
            statement.setDate(4, java.sql.Date.valueOf(stockItem.getExpiryDate()));
            statement.setString(5, stockItem.getStatus().toString());
            statement.setString(6, stockItem.getProduct().getId());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateStockItem(StockItem stockItem) {
        String sql = """
                UPDATE "Inventory"."Stock_Items"
                SET "quantity" = ?, "location" = ?, "expiry_date" = ?, status = ?, "product_id" = ? 
                WHERE "stock_id" = ?
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, stockItem.getQuantity());
            statement.setString(2, stockItem.getLocation());
            statement.setDate(3, java.sql.Date.valueOf(stockItem.getExpiryDate()));
            statement.setString(4, stockItem.getStatus().name());
            statement.setString(5, stockItem.getProduct().getId());
            statement.setString(6, stockItem.getStockItemId());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteStockItem(String stockId) {
        String sql = """
                DELETE FROM "Inventory"."Stock_Items"
                WHERE "stock_id" = ?
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, stockId);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public StockItem getStockItemById(String stockId) {
        String sql = """
                SELECT *
                FROM "Inventory"."Stock_Items"
                WHERE stock_id = ?
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, stockId);
            ResultSet res = statement.executeQuery();

            if (res.next()) {
                String id = res.getString("stock_id");
                int quantity = res.getInt("quantity");
                String location = res.getString("location");
                LocalDate expiryDate = res.getDate("expiry_date").toLocalDate();
                StockItemStatus status = StockItemStatus.valueOf(res.getString("status"));
                String productId = res.getString("product_id");

                Product product = productDAO.getProductById(productId);

                return new StockItem(id, product, quantity, location, expiryDate, status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<StockItem> getAllStockItems() {
        List<StockItem> stockItems = new ArrayList<>();
        String sql = """
                SELECT *
                FROM "Inventory"."Stock_Items"
                """;
        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet res = statement.executeQuery()) {

            while (res.next()) {
                String id = res.getString("stock_id");
                int quantity = res.getInt("quantity");
                String location = res.getString("location");
                LocalDate expiryDate = res.getDate("expiry_date").toLocalDate();
                StockItemStatus status = StockItemStatus.valueOf(res.getString("status"));
                String productId = res.getString("product_id");

                Product product = productDAO.getProductById(productId);

                StockItem stockItem = new StockItem(id, product, quantity, location, expiryDate, status);
                stockItems.add(stockItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stockItems;
    }

    public List<StockItem> getStockItemsByProductId(String productId) {
        List<StockItem> stockItems = new ArrayList<>();
        String sql = """
                SELECT *
                FROM "Inventory"."Stock_Items"
                WHERE product_id = ?
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, productId);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                String id = res.getString("stock_id");
                int quantity = res.getInt("quantity");
                String location = res.getString("location");
                LocalDate expiryDate = res.getDate("expiry_date").toLocalDate();
                StockItemStatus status = StockItemStatus.valueOf(res.getString("status"));

                Product product = productDAO.getProductById(productId);

                StockItem stockItem = new StockItem(id, product, quantity, location, expiryDate, status);
                stockItems.add(stockItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stockItems;
    }


    public String getStockItemByBatch(Product product, String location, LocalDate expiryDate, StockItemStatus Status) {
        String sql = """
                SELECT stock_id
                FROM "Inventory"."Stock_Items"
                WHERE product_id = ? AND location = ? AND expiry_date = ? AND status = ?
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, product.getId());
            statement.setString(2, location);
            statement.setDate(3, java.sql.Date.valueOf(expiryDate));
            statement.setString(4, Status.name());

            ResultSet res = statement.executeQuery();

            if (res.next()) {
                return res.getString("stock_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int numInStorage(String productId) {
        String sql = """
                    SELECT COALESCE(SUM(quantity), 0) AS total
                    FROM "Inventory"."Stock_Items"
                    WHERE product_id = ? AND location = 'storage'
                """;
        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, productId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int numInStore(String productId) {
        String sql = """
                    SELECT COALESCE(SUM(quantity), 0) AS total
                    FROM "Inventory"."Stock_Items"
                    WHERE product_id = ? AND location != 'storage'
                """;
        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, productId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int numOfExpired(String productId) {
        String sql = """
                    SELECT COALESCE(SUM(quantity), 0) AS total
                    FROM "Inventory"."Stock_Items"
                    WHERE product_id = ? AND status = 'EXPIRED'
                """;
        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, productId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int numOfDamaged(String productId) {
        String sql = """
                    SELECT COALESCE(SUM(quantity), 0) AS total
                    FROM "Inventory"."Stock_Items"
                    WHERE product_id = ? AND status = 'DAMAGED'
                """;
        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, productId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean hasAnyStockItem(String productId) {
        String sql = """
                    SELECT 1
                    FROM "Inventory"."Stock_Items"
                    WHERE product_id = ?
                    LIMIT 1
                """;
        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, productId);
            ResultSet rs = statement.executeQuery();
            return rs.next(); // returns true if there is at least one stock item for this product
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public int numOfOk(String productId) {
        String sql = """
                    SELECT COALESCE(SUM(quantity), 0) AS total
                    FROM "Inventory"."Stock_Items"
                    WHERE product_id = ? AND status = 'OK'
                """;
        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, productId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


}

