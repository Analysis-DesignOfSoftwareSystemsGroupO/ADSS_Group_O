
package LoadData;
import DataBase.BranchesDataBase;
import DataBase.OrderDataBase;
import DataBase.ProductDataBase;
import DataBase.SuppliersDataBase;
import Domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoadData {
    private ProductDataBase productDataBase = ProductDataBase.getInstance();
    private BranchesDataBase branchesDataBase = BranchesDataBase.getInstance();
    private SuppliersDataBase suppliersDataBase = SuppliersDataBase.getInstance();
    private OrderDataBase orderDataBase = OrderDataBase.getInstance();

    //loads data to dataBase
    public void LoadData() {
        //add branches
        String[][] branchData = {
                {"1", "Beer Sheva", "Rager 101"},
                {"2", "Lehavim", "HaGefen 12"},
                {"3", "Tel Aviv", "Ibn Gabirol 25"},
                {"4", "Jerusalem", "King George 10"},
                {"5", "Haifa", "Herzl 15"},
                {"6", "Ramat Gan", "Jabotinsky 120"},
                {"7", "Netanya", "Ben Gurion 45"},
                {"8", "Eilat", "Sderot Hatmarim 3"},
                {"9", "Petah Tikva", "Bar Kochva 78"}
        };
        for (String[] data : branchData) {
            branchesDataBase.addBranch(new Branch(data[0], data[1], data[2]));
        }
        // add suppliers with full data for the constructor
        String[][] supplierData = {
                {"1", "Yosi", "CreditCard", "12", "286", "570", "Noa", "0501234567", "Manager"},
                {"2", "Dina", "Cash", "123", "12", "102", "Rami", "0507654321", "Sales"},
                {"3", "Avi", "Check", "111", "10", "103", "Lior", "0521112233", "Clerk"},
                {"4", "Liat", "Bank Transfer", "11", "555", "104", "Dana", "0543334455", "Assistant"},
                {"5", "Moshe", "CreditCard", "14", "888", "105", "Eli", "0539998877", "Supervisor"}
        };

        for (String[] data : supplierData) {
            suppliersDataBase.addSupplier(new Supplier(
                    data[0], data[1], data[2], data[3], data[4],
                    data[5], data[6], data[7], data[8]
            ));
        }

        // add products
        String[][] productData = {
                {"1", "Bamba", "Osem"},
                {"2", "Bisli", "Osem"},
                {"3", "Cola", "Coca Cola"},
                {"4", "Sprite", "Tempo"},
                {"5", "Milk", "Tnuva"},
                {"6", "Bread", "Ariel Bakery"},
                {"7", "Water Bottle", "Tempo"},
                {"8", "Cheese", "Tnuva"},
                {"9", "Yogurt", "Shtraus"},
                {"10", "Chocolate", "Elit"},
                {"11", "Pita", "Ariel Bakery"},
                {"12", "Rice", "Osem"},
                {"13", "Pasta", "Osem"},
                {"14", "Ketchup", "Osem"},
                {"15", "Toilet Paper", "SuperLi"},
                {"16", "Bamba", "Lulu"}
        };

        for (String[] data : productData) {
            productDataBase.addProduct(new Product(data[0], data[1], data[2]));
        }
        //add agreements
        String[][] agreementData = {
                { "1", "1", "Constant Delivery" },
                { "1", "3", "Temporary Delivery" },
                { "1", "4", "Self Pick Up" },
                { "1", "5", "Constant Delivery" },
                { "2", "1", "Temporary Delivery" },
                { "2", "2", "Self Pick Up" },
                { "2", "4", "Constant Delivery" },
                { "2", "5", "Temporary Delivery" },
                { "3", "1", "Constant Delivery" },
                { "3", "2", "Temporary Delivery" },
                { "3", "3", "Self Pick Up" },
                { "3", "5", "Constant Delivery" },
                { "4", "2", "Temporary Delivery" },
                { "4", "3", "Self Pick Up" },
                { "5", "1", "Constant Delivery" },
                { "5", "3", "Temporary Delivery" },
                { "5", "4", "Self Pick Up" },
                { "6", "1", "Constant Delivery" },
                { "6", "4", "Temporary Delivery" },
                { "6", "5", "Self Pick Up" },
                { "7", "1", "Temporary Delivery" },
                { "7", "2", "Self Pick Up" },
                { "7", "5", "Constant Delivery" },
                { "8", "1", "Self Pick Up" },
                { "8", "2", "Constant Delivery" },
                { "8", "3", "Temporary Delivery" },
                { "9", "2", "Self Pick Up" },
                { "9", "3", "Temporary Delivery" },
                { "9", "4", "Constant Delivery" },
                { "4", "4", "Self Pick Up" },
                { "5", "5", "Temporary Delivery" }
        };


        for (String[] data : agreementData) {
            Agreement agreement = new Agreement(branchesDataBase.getBranch(data[0]),
                    suppliersDataBase.getSupplier(data[1]), new Delivery(data[2]));
            suppliersDataBase.addAgreement(agreement);
        }


    //add products to agreements
        Object[][] items = {
                { 17, "1", "3", "1", "Constant Delivery" },
                { 14, "2", "7", "2", "Temporary Delivery" },
                { 22, "3", "1", "4", "Self Pick Up" },
                { 27, "4", "8", "3", "Constant Delivery" },
                { 10, "5", "6", "5", "Temporary Delivery" },
                { 24, "6", "9", "2", "Self Pick Up" },
                { 19, "7", "2", "1", "Temporary Delivery" },
                { 11, "8", "5", "3", "Constant Delivery" },
                { 13, "9", "4", "4", "Self Pick Up" },
                { 29, "10", "7", "1", "Temporary Delivery" },
                { 9,  "11", "1", "5", "Constant Delivery" },
                { 20, "12", "3", "5", "Self Pick Up" },
                { 25, "13", "6", "4", "Constant Delivery" },
                { 15, "14", "9", "4", "Temporary Delivery" },
                { 12, "15", "2", "1", "Self Pick Up" },
                { 18, "16", "8", "3", "Constant Delivery" }
        };


// First: add products to suppliers
        for (Object[] entry : items) {
            String productID = (String) entry[1];
            String supplierId = (String) entry[3];
            int price = (int) entry[0];
            try {
                suppliersDataBase.getSupplier(supplierId).addProduct(productDataBase.getProduct(productID), price);

            }catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Product : " + productID);
                System.out.println("Supplier : " + supplierId);
            }
        }

        Random rand = new Random();
// Then: add supplied items to agreements
        for (Object[] entry : items) {
            int price = (int) entry[0];
            String productID = (String) entry[1];
            String branchId = (String) entry[2];
            String supplierId = (String) entry[3];
            SuppliedItem item = new SuppliedItem(price, productDataBase.getProduct(productID));
            suppliersDataBase.addProductToAgreement(item, branchId, supplierId);
            if (rand.nextBoolean()){
                int quantity = rand.nextInt(20) + 1;
                int price1 = rand.nextInt(40) + 1;
                suppliersDataBase.addDiscountToAgreement(branchId, supplierId, new Discount(item, quantity, price1));
            }

        }
    }
}
