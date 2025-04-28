package inventory.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CategoryTest {
    @Test
    void testCategoryConstructor() {
        Category category = new Category("Electronics");
        assertEquals("Electronics", category.getName());
    }

    @Test
    void testCategorySetName() {
        Category category = new Category("Electronics");
        category.setName("Home Appliances");
        assertEquals("Home Appliances", category.getName());
    }

    @Test
    void testCategoryCantSetNameToNull() {
        Category category = new Category("Electronics");
        try {
            category.setName(null);
        } catch (NullPointerException e) {
            return; // This is the expected behavior
        }
        throw new AssertionError("Expected IllegalArgumentException was not thrown");
    }

}