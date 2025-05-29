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

}
