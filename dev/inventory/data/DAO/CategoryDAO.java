package inventory.data.DAO;

import inventory.data.connection.DataBaseConnector;
import inventory.domain.Category;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CategoryDAO {

    public void saveCategory(Category category) {
        String sql = """
                    INSERT INTO "Inventory"."Categories" 
                        ("category_id", "category_name")
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

    public Category getCategoryById(String id) {
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

    public String getOrCreateCategoryGroup(String parentId, String subId, String subSubId){
        String sql = """
                SELECT group_id FROM "Inventory"."Category_Groups"
                WHERE parent_category_id = ? AND sub_category_id = ? AND sub_sub_category_id = ?
                """;
        try (Connection connection = DataBaseConnector.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setString(1, parentId);
                statement.setString(2, subId);
                statement.setString(3, subSubId);

                ResultSet res = statement.executeQuery();

                if (res.next()) {
                    return res.getString("group_id");
                } else {
                    String newGroupId = java.util.UUID.randomUUID().toString();
                    saveCategoryGroup(newGroupId, getCategoryById(parentId), getCategoryById(subId), getCategoryById(subSubId));
                    return newGroupId;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error retrieving or creating category group: " + e.getMessage());
            }
        return null;
    }


    public List<String> getCategoryGroupsByCategoryId(String categoryId) {
        List<String> groupIds = new ArrayList<>();
        String sql = """
                SELECT group_id FROM "Inventory"."Category_Groups"
                WHERE parent_category_id = ? OR sub_category_id = ? OR sub_sub_category_id = ?
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, categoryId);
            statement.setString(2, categoryId);
            statement.setString(3, categoryId);

            ResultSet res = statement.executeQuery();

            while (res.next()) {
                groupIds.add(res.getString("group_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error retrieving category groups by category ID: " + e.getMessage());
        }
        return groupIds;
    }


    public List<String> getCategoriesByGroupId(String groupId) {
        List<String> groupIds = new ArrayList<>();
        String sql = """
                SELECT  parent_category_id,sub_category_id,sub_sub_category_id
                FROM "Inventory"."Category_Groups"
                WHERE group_id = ?
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, groupId);


            ResultSet res = statement.executeQuery();

            while (res.next()) {
                groupIds.add(res.getString("group_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error retrieving category groups by category ID: " + e.getMessage());
        }
        return groupIds;
    }

    public boolean categoryExists(String name) {
        String sql = """
                SELECT 1 FROM "Inventory"."Categories"
                WHERE category_name = ?
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            ResultSet res = statement.executeQuery();

            return res.next();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error checking if category exists: " + e.getMessage());
            return false;
        }
    }

    public void SaveCategoryByProductPair(String categoryId, String productId) {
        String sql = """
                    INSERT INTO "Inventory"."Products_by_Categories" 
                        ("product_id", "category_id")
                    VALUES (?, ?)
                """;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, categoryId);
            statement.setString(2, productId);

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error saving category by product: " + e.getMessage());
        }

    }






}



