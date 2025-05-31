package inventory.data.DAO;

import inventory.data.connection.DataBaseConnector;
import inventory.domain.Discount;
import inventory.domain.DiscountTargetType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DiscountDAO {

    public void saveDiscount(Discount discount) {
        String insertDiscountSql = """
                INSERT INTO "Inventory"."Discounts" 
                (discount_id, discount_description, discount_percentage)
                VALUES (?, ?, ?)
                """;

        String insertTargetSql = """
                INSERT INTO "Inventory"."Discount_Store_Target" 
                (discount_id, discount_target_type, discount_target_id , start_date, end_date)
                VALUES (?, ?, ?, ?,?)
                """;

        try (Connection connection = DataBaseConnector.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement1 = connection.prepareStatement(insertDiscountSql);
                 PreparedStatement statement2 = connection.prepareStatement(insertTargetSql)) {

                statement1.setString(1, discount.getId());
                statement1.setString(2, discount.getDescription());
                statement1.setDouble(3, (int) discount.getDiscountPercentage());
                statement1.executeUpdate();

                statement2.setString(1, discount.getId());
                statement2.setString(2, discount.getTargetType().name());
                statement2.setString(3, discount.getTargetId());
                statement2.setDate(4, java.sql.Date.valueOf(discount.getStartDate()));
                statement2.setDate(5, java.sql.Date.valueOf(discount.getEndDate()));
                statement2.executeUpdate();

                connection.commit();

            } catch (Exception e) {
                connection.rollback();
                e.printStackTrace();
                System.err.println("Error saving discount: " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }

    public void updateDiscount(Discount discount) {
        String updateDiscountSql = """
                UPDATE "Inventory"."Discounts"
                SET discount_description = ?, discount_percentage = ?
                WHERE discount_id = ?
                """;

        String updateTargetSql = """
                UPDATE "Inventory"."Discount_Store_Target"
                SET discount_target_type = ?, discount_target_id = ?, start_date = ?, end_date = ?
                WHERE discount_id = ?
                """;

        try (Connection connection = DataBaseConnector.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement1 = connection.prepareStatement(updateDiscountSql);
                 PreparedStatement statement2 = connection.prepareStatement(updateTargetSql)) {

                statement1.setString(1, discount.getDescription());
                statement1.setDouble(2, (int) discount.getDiscountPercentage());
                statement1.setString(3, discount.getId());
                statement1.executeUpdate();

                statement2.setString(1, discount.getTargetType().name());
                statement2.setString(2, discount.getTargetId());
                statement2.setDate(3, java.sql.Date.valueOf(discount.getStartDate()));
                statement2.setDate(4, java.sql.Date.valueOf(discount.getEndDate()));
                statement2.setString(5, discount.getId());
                statement2.executeUpdate();

                connection.commit();

            } catch (Exception e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error updating discount: " + e.getMessage());
        }
    }

    public List<Discount> getAllDiscounts() {
        List<Discount> discounts = new ArrayList<>();

        String sql = """
                SELECT d.discount_id, d.discount_description, d.discount_percentage, 
                       dst.discount_target_type, dst.discount_target_id, 
                       dst.start_date, dst.end_date
                FROM "Inventory"."Discounts" d
                JOIN "Inventory"."Discount_Store_Target" dst 
                ON d.discount_id = dst.discount_id
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet res = statement.executeQuery()) {

            while (res.next()) {
                String id = res.getString("discount_id");
                String description = res.getString("discount_description");
                int discountPercentage = res.getInt("discount_percentage");
                String targetTypeStr = res.getString("discount_target_type");
                String targetId = res.getString("discount_target_id");
                LocalDate startDate = res.getDate("start_date").toLocalDate();
                LocalDate endDate = res.getDate("end_date").toLocalDate();

                DiscountTargetType targetType = DiscountTargetType.valueOf(targetTypeStr);
                Discount discount = new Discount(id, description, targetType, targetId, discountPercentage, startDate, endDate);
                discounts.add(discount);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error retrieving discounts: " + e.getMessage());
        }
        return discounts;
    }

    public void deleteDiscount(String id) {
        String deleteTargetSql = """
                DELETE FROM "Inventory"."Discount_Store_Target"
                WHERE discount_id = ?
                """;
        String deleteDiscountSql = """
                DELETE FROM "Inventory"."Discounts"
                WHERE discount_id = ?
                """;

        try (Connection connection = DataBaseConnector.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement1 = connection.prepareStatement(deleteTargetSql);
                 PreparedStatement statement2 = connection.prepareStatement(deleteDiscountSql)) {

                statement1.setString(1, id);
                statement1.executeUpdate();

                statement2.setString(1, id);
                statement2.executeUpdate();

                connection.commit();

            } catch (Exception e) {
                connection.rollback();
                e.printStackTrace();
                System.err.println("Error deleting discount: " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }

    public double getDiscountPercentageByProductId(String productId) {
        String sql = """
                SELECT d.discount_percentage
                FROM "Inventory"."Selling_Prices" sp
                JOIN "Inventory"."Discounts" d ON sp.discount_id = d.discount_id
                WHERE sp.product_id = ?
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, productId);
            ResultSet res = statement.executeQuery();

            if (res.next()) {
                return res.getDouble("discount_percentage");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error retrieving discount percentage: " + e.getMessage());
        }
        return 0.0;
    }

    public void updateAllDiscountsSellingPrices() {
        try (Connection connection = DataBaseConnector.getConnection()) {
            connection.setAutoCommit(false);

            // 1. Remove expired discounts
            String clearExpiredSql = """
                        UPDATE "Inventory"."Selling_Prices"
                        SET discount_id = NULL, discount_selling_price = NULL
                        WHERE discount_id IS NOT NULL
                          AND discount_id IN (
                              SELECT discount_id
                              FROM "Inventory"."Discount_Store_Target"
                              WHERE NOW() NOT BETWEEN start_date AND end_date
                          )
                    """;

            // 2. Apply active product-specific discounts
            String productDiscountSql = """
                        UPDATE "Inventory"."Selling_Prices" sp
                        SET discount_id = dst.discount_id,
                            discount_selling_price = sp.selling_price * (1 - d.discount_percentage)
                        FROM "Inventory"."Discount_Store_Target" dst
                        JOIN "Inventory"."Discounts" d ON dst.discount_id = d.discount_id
                        WHERE dst.discount_target_type = 'product'
                          AND dst.discount_target_id = sp.product_id
                          AND NOW() BETWEEN dst.start_date AND dst.end_date
                    """;

            // 3. Apply category-wide discounts to products without any discount
            String categoryDiscountSql = """
                        UPDATE "Inventory"."Selling_Prices" sp
                        SET discount_id = dst.discount_id,
                            discount_selling_price = sp.selling_price * (1 - d.discount_percentage)
                        FROM "Inventory"."Discount_Store_Target" dst
                        JOIN "Inventory"."Discounts" d ON dst.discount_id = d.discount_id
                        JOIN "Inventory"."Products_by_Categories" pc ON pc.category_id = dst.discount_target_id
                        WHERE dst.discount_target_type = 'category'
                          AND pc.product_id = sp.product_id
                          AND sp.discount_id IS NULL
                          AND NOW() BETWEEN dst.start_date AND dst.end_date
                    """;

            try (
                    PreparedStatement clearStmt = connection.prepareStatement(clearExpiredSql);
                    PreparedStatement productStmt = connection.prepareStatement(productDiscountSql);
                    PreparedStatement categoryStmt = connection.prepareStatement(categoryDiscountSql)
            ) {
                clearStmt.executeUpdate();
                productStmt.executeUpdate();
                categoryStmt.executeUpdate();

                connection.commit();
                System.out.println("All active discounts applied successfully.");
            } catch (Exception e) {
                connection.rollback();
                System.err.println("Error applying discounts: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public void updateAllDiscountsAndSellingPrices() {

        try (Connection connection = DataBaseConnector.getConnection()) {
            connection.setAutoCommit(false);


            String clearExpiredSql = """
                    UPDATE "Inventory"."Selling_Prices"
                    SET discount_id = NULL, discount_selling_price = NULL
                    WHERE discount_id IS NOT NULL
                      AND discount_id IN (
                          SELECT d.discount_id
                          FROM "Inventory"."Discounts" d 
                          JOIN "Inventory"."Discount_Store_Target" dst ON d.discount_id = dst.discount_id
                          WHERE CURRENT_DATE > dst.end_date
                      )
                    """;

            String getActiveDiscountsSql = """
                    SELECT d.discount_id, d.discount_percentage, dst.discount_target_type, dst.discount_target_id
                    FROM "Inventory"."Discounts" d
                    JOIN "Inventory"."Discount_Store_Target" dst ON d.discount_id = dst.discount_id
                    WHERE CURRENT_DATE BETWEEN dst.start_date AND dst.end_date
                    """;

            String clearNoActiveDiscountsSql = """
                    UPDATE "Inventory"."Selling_Prices"
                    SET discount_id = NULL, discount_selling_price = NULL
                    WHERE product_id NOT IN (
                            SELECT DISTINCT 
                                CASE 
                                    WHEN dst.discount_target_type = 'product' THEN dst.discount_target_id
                                    WHEN dst.discount_target_type = 'category' THEN pbc.product_id
                                END
                            FROM "Inventory"."Discount_Store_Target" dst
                            LEFT JOIN "Inventory"."Products_by_Categories" pbc 
                                ON dst.discount_target_type = 'category' AND pbc.category_id = dst.discount_target_id
                            WHERE CURRENT_DATE BETWEEN dst.start_date AND dst.end_date
                      )
                      AND discount_selling_price IS NOT NULL
                    """;

            try (
                    PreparedStatement clearExpiredStmt = connection.prepareStatement(clearExpiredSql);
                    PreparedStatement getActiveDiscountsStmt = connection.prepareStatement(getActiveDiscountsSql);
                    PreparedStatement clearNoActiveDiscountsStmt = connection.prepareStatement(clearNoActiveDiscountsSql);
            ) {
                clearExpiredStmt.executeUpdate();

                try (ResultSet res = getActiveDiscountsStmt.executeQuery()) {
                    while (res.next()) {
                        String discountId = res.getString("discount_id");
                        double discountPercentage = res.getDouble("discount_percentage");
                        String targetType = res.getString("discount_target_type");
                        String targetId = res.getString("discount_target_id");

                        List<String> productIds = new ArrayList<>();

                        if ("product".equalsIgnoreCase(targetType)) {
                            productIds.add(targetId);
                        } else if ("category".equalsIgnoreCase(targetType)) {
                            String getProducts = """
                                    
                                        SELECT product_id 
                                    FROM "Inventory"."Products_by_Categories" 
                                    WHERE category_id = ?
                                    
                                    """;
                            try (PreparedStatement ps2 = connection.prepareStatement(getProducts
                            )) {
                                ps2.setString(1, targetId);
                                try (
                                        ResultSet rs2 = ps2.executeQuery
                                                ()) {
                                    while (rs2.next()) {
                                        productIds.add(rs2.getString(

                                                "product_id"));
                                    }
                                }
                            }
                        }

                        for (String productId : productIds) {
                            String updatePriceSql = """
                                    UPDATE "Inventory"."Selling_Prices"
                                    SET discount_selling_price = selling_price * (1 - ? / 100),
                                        discount_id = ?
                                    WHERE product_id = ?
                                    """;

                            try (PreparedStatement ps3 = connection.prepareStatement(updatePriceSql)) {
                                ps3.setDouble(1, discountPercentage);
                                ps3.setString(2, discountId);
                                ps3.setString(3, productId);
                                ps3.executeUpdate();
                            }
                        }
                    }
                }
                clearNoActiveDiscountsStmt.executeUpdate();
                connection.commit();


            } catch (Exception e) {
                connection.rollback();
                System.err.println("Error updating discounts and selling prices: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void cleanupInvalidDiscounts() {
        try (Connection connection = DataBaseConnector.getConnection()) {
            // Delete discounts with invalid PRODUCT target
            String sqlProduct = """
            DELETE FROM "Inventory"."Discounts"
            WHERE discount_id IN (
                SELECT d.discount_id
                FROM "Inventory"."Discounts" d
                JOIN "Inventory"."Discount_Store_Target" dst ON d.discount_id = dst.discount_id
                WHERE dst.discount_target_type = 'PRODUCT'
                  AND dst.discount_target_id NOT IN (
                      SELECT product_id FROM "Inventory"."Products"
                  )
            )
            """;
            try (PreparedStatement ps = connection.prepareStatement(sqlProduct)) {
                ps.executeUpdate();
            }

            // Delete discounts with invalid CATEGORY target
            String sqlCategory = """
            DELETE FROM "Inventory"."Discounts"
            WHERE discount_id IN (
                SELECT d.discount_id
                FROM "Inventory"."Discounts" d
                JOIN "Inventory"."Discount_Store_Target" dst ON d.discount_id = dst.discount_id
                WHERE dst.discount_target_type = 'CATEGORY'
                  AND dst.discount_target_id NOT IN (
                      SELECT category_id FROM "Inventory"."Categories"
                  )
            )
            """;
            try (PreparedStatement ps = connection.prepareStatement(sqlCategory)) {
                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error cleaning up invalid discounts: " + e.getMessage());
        }
    }

}
