package Domain;
import SupplierMoudleSource.Domain.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AgreementTest {
    private Branch branch;
    private Supplier supplier;
    private Agreement agreement;
    private Discount discount1;
    private Discount discount2;
    private SuppliedItem suppliedItem1;
    private SuppliedItem suppliedItem2;
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
         branch = new Branch("1", "Petach tikva", "Zikhron Yaakov");
         supplier = new Supplier("1", "Dudi","Cash" , "5555", "10",
                "401", "Dudi", "0547559841",
                "Manager", "Temporary Delivery", "");
         agreement = new Agreement(branch, supplier);
         Product product = new Product("1", "Bamba", "Lulu");
         Product product2 = new Product("2", "Bamba", "Osem");
         suppliedItem2 = new SuppliedItem(25, product2);
         suppliedItem1 = new SuppliedItem(50, product);
         discount1 = new Discount(suppliedItem1, 5, 100);
         discount2 = new Discount(suppliedItem2, 5, 100);
         supplier.addProduct(product, 50);
    }



    @org.junit.jupiter.api.Test
    void getDiscounts() {
        assertInstanceOf(List.class, agreement.getDiscounts());
    }

    @org.junit.jupiter.api.Test
    void getSupplierItemsList() {
        assertInstanceOf(List.class, agreement.getSupplierItemsList());
    }


    @org.junit.jupiter.api.Test
    void addDiscount() {
        agreement.addItem(suppliedItem1);
        agreement.addDiscount(discount1);
        assertTrue(agreement.getDiscounts().contains(discount1), "Discount was not added");
        assertThrows(Exception.class, () -> agreement.addDiscount(discount2),
                "Discount of a product that doesnt exist was added");
        assertThrows(Exception.class, () -> agreement.addDiscount(discount1), "Discount was added twice");
        assertThrows(Exception.class, () -> agreement.addDiscount(null), "Failed when value was null");
    }


    @org.junit.jupiter.api.Test
    void addItem() {
        agreement.addItem(suppliedItem1);
        assertTrue(agreement.getSupplierItemsList().contains(suppliedItem1), "Supplier item was not added");
        assertThrows(Exception.class, () -> agreement.addItem(suppliedItem1), "Failed when the same item was added twice");
        assertThrows(Exception.class, () -> agreement.addItem(suppliedItem2), "Failed when added an item that doesnt exist for the supplier");
        assertThrows(Exception.class, () -> agreement.addItem(null), "Failed when value was null");
    }

    @org.junit.jupiter.api.Test
    void productInAgreement() {
        assertFalse(agreement.productInAgreement(suppliedItem1.getSuppliedItemID()),
                "Product that was not in the agreement returned true");
        agreement.addItem(suppliedItem1);
        assertTrue(agreement.productInAgreement(suppliedItem1.getSuppliedItemID()));
    }


    @org.junit.jupiter.api.Test
    void getSupplierID() {
        assertEquals(supplier.getID(), agreement.getSupplierID());
    }

    @org.junit.jupiter.api.Test
    void getBranchID() {
        assertEquals(branch.getBranchID(), agreement.getBranchID());
    }

    @org.junit.jupiter.api.Test
    void removeProduct() {
        agreement.addItem(suppliedItem1);
        agreement.removeProduct(suppliedItem1.getSuppliedItemID());
        assertNull(agreement.getSupplierItem(suppliedItem1.getSuppliedItemID()), "Supplier item was not removed");
        assertThrows(Exception.class, () -> agreement.removeProduct(suppliedItem2.getSuppliedItemID()),
                "Didnt throw when a product that did not exist in the agreement was removed");
        assertThrows(Exception.class, () -> agreement.removeProduct(null), "Failed when product was null");
    }

    @org.junit.jupiter.api.Test
    void removeDiscount() {
        agreement.addItem(suppliedItem1);
        agreement.addDiscount(discount1);
        agreement.removeDiscount(suppliedItem1.getSuppliedItemID());
        assertTrue(!agreement.getDiscounts().contains(discount1), "Discount was not removed");
        assertThrows(Exception.class, () -> agreement.removeDiscount(suppliedItem2.getSuppliedItemID()),
                "Discount that didnt exist didnt throw an exception");
        assertThrows(Exception.class, () -> agreement.removeDiscount(null));
    }

    @org.junit.jupiter.api.Test
    void testToString() {
    }
    @org.junit.jupiter.api.Test
    void getSupplierItem() {
        agreement.addItem(suppliedItem1);
        assertEquals(agreement.getSupplierItem(suppliedItem1.getSuppliedItemID()), suppliedItem1, "Supplier item " +
                "was not returned");
        assertNull(agreement.getSupplierItem(suppliedItem2.getSuppliedItemID()), "Supplier that didnt exist was returned");
    }

}