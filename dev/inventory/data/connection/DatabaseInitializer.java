package inventory.data.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void createAllTablesIfNotExists() {
        String sql = """
            CREATE SCHEMA IF NOT EXISTS "Inventory";

            CREATE TABLE IF NOT EXISTS "Inventory"."Categories" (
                category_id   TEXT PRIMARY KEY,
                category_name TEXT NOT NULL
            );

            CREATE TABLE IF NOT EXISTS "Inventory"."Category_Groups" (
                group_id            TEXT PRIMARY KEY,
                parent_category_id  TEXT NOT NULL,
                sub_category_id     TEXT NOT NULL,
                sub_sub_category_id TEXT NOT NULL,
                FOREIGN KEY (parent_category_id) REFERENCES "Inventory"."Categories"(category_id),
                FOREIGN KEY (sub_category_id) REFERENCES "Inventory"."Categories"(category_id),
                FOREIGN KEY (sub_sub_category_id) REFERENCES "Inventory"."Categories"(category_id)
            );

            CREATE TABLE IF NOT EXISTS "Inventory"."Discounts" (
                discount_id          TEXT PRIMARY KEY,
                discount_description TEXT NOT NULL,
                discount_percentage  INTEGER NOT NULL
            );

            CREATE TABLE IF NOT EXISTS "Inventory"."Products" (
                product_id           TEXT PRIMARY KEY,
                product_name         TEXT NOT NULL,
                product_manufacturer TEXT NOT NULL,
                cost_price           DOUBLE PRECISION NOT NULL,
                min_stock_level      INTEGER NOT NULL,
                gorup_id             TEXT NOT NULL,
                location             TEXT NOT NULL,
                FOREIGN KEY (gorup_id) REFERENCES "Inventory"."Category_Groups"(group_id)
            );

            CREATE TABLE IF NOT EXISTS "Inventory"."Stock_Items" (
                stock_id    TEXT PRIMARY KEY,
                quantity    INTEGER NOT NULL,
                location    TEXT NOT NULL,
                expiry_date TEXT NOT NULL,
                status      TEXT NOT NULL,
                product_id  TEXT,
                FOREIGN KEY (product_id) REFERENCES "Inventory"."Products"(product_id)
            );

            CREATE TABLE IF NOT EXISTS "Inventory"."Selling_Prices" (
                product_id             TEXT PRIMARY KEY,
                selling_price          DOUBLE PRECISION NOT NULL,
                discount_selling_price DOUBLE PRECISION,
                discount_id            TEXT,
                FOREIGN KEY (product_id) REFERENCES "Inventory"."Products"(product_id),
                FOREIGN KEY (discount_id) REFERENCES "Inventory"."Discounts"(discount_id)
            );

            CREATE TABLE IF NOT EXISTS "Inventory"."Products_by_Categories" (
                product_id  TEXT NOT NULL,
                category_id TEXT NOT NULL,
                PRIMARY KEY (product_id, category_id),
                FOREIGN KEY (product_id) REFERENCES "Inventory"."Products"(product_id),
                FOREIGN KEY (category_id) REFERENCES "Inventory"."Categories"(category_id)
            );

            CREATE TABLE IF NOT EXISTS "Inventory"."Discount_Store_Target" (
                discount_id          TEXT PRIMARY KEY,
                discount_target_type TEXT NOT NULL,
                discount_target_id   TEXT NOT NULL,
                start_date           DATE NOT NULL,
                end_date             TEXT NOT NULL,
                FOREIGN KEY (discount_id) REFERENCES "Inventory"."Discounts"(discount_id)
            );

            CREATE TABLE IF NOT EXISTS "Inventory"."Stock_Item_Orders" (
                stock_id         TEXT PRIMARY KEY,
                order_date       TEXT NOT NULL,
                supplier_id      TEXT NOT NULL,
                product_id       TEXT NOT NULL,
                total_cost_price DOUBLE PRECISION NOT NULL,
                FOREIGN KEY (stock_id) REFERENCES "Inventory"."Stock_Items"(stock_id),
                FOREIGN KEY (product_id) REFERENCES "Inventory"."Products"(product_id)
            );
        """;

        try (Connection conn = DataBaseConnector.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Inventory schema and tables created.");
        } catch (SQLException e) {
            System.err.println("Failed to initialize database:");
            e.printStackTrace();
        }
    }
}
