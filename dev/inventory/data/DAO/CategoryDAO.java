package inventory.data.DAO;

import inventory.data.connection.DataBaseConnector;
import inventory.domain.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CategoryDAO {

    public void saveCategory(Category category) {
        String sql = """
                    INSERT INTO Categories (id, name)
                    VALUES (?, ?)
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, category.getId());
            statement.setString(2, category.getName());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error saving category: " + e.getMessage());
        }
    }

    public void saveCategoryGroup(String groupId, Category parentCategory, Category subCategory,
                                  Category subSubCategory) {
        String sql = """
                INSERT INTO "Inventory"."Category_Groups"
                (group_id, parent_category_id, sub_category_id, sub_sub_category_id)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, groupId);
            statement.setString(2, parentCategory.getId());
            statement.setString(3, subCategory.getId());
            statement.setString(4, subSubCategory.getId());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error saving category group: " + e.getMessage());
        }
    }

    public void updateCategory(Category category) {
        String sql = """
                    UPDATE "Inventory"."Categories"
                    SET category_name = ?
                    WHERE category_id = ?
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, category.getName());
            statement.setString(2, category.getId());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error updating category: " + e.getMessage());
        }
    }

    public Category getCategoryById(String id) { //TODO: decide if this should build the whole tree of category
        String sql = """
                SELECT * FROM "Inventory"."Categories"
                WHERE category_id = ?
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);
            ResultSet res = statement.executeQuery();

            if (res.next()) {
                return new Category(res.getString("category_id"), res.getString("category_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error retrieving category by ID: " + e.getMessage());
        }
        return null;
    }

    public Category getCategoryByName(String name) {
        String sql = """
                SELECT * FROM "Inventory"."Categories"
                WHERE category_name = ?
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            ResultSet res = statement.executeQuery();

            if (res.next()) {
                return new Category(res.getString("category_id"), res.getString("category_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error retrieving category by name: " + e.getMessage());
        }
        return null;
    }
}



