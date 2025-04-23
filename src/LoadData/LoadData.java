package LoadData;
import DataBase.BranchesDataBase;
import DataBase.OrderDataBase;
import DataBase.ProductDataBase;
import DataBase.SuppliersDataBase;
import Domain.*;

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
                {"1", "Yosi", "CreditCard", "568", "286", "101", "Noa", "0501234567", "Manager"},
                {"2", "Dina", "Cash", "123", "456", "102", "Rami", "0507654321", "Sales"},
                {"3", "Avi", "Check", "111", "222", "103", "Lior", "0521112233", "Clerk"},
                {"4", "Liat", "Bank Transfer", "444", "555", "104", "Dana", "0543334455", "Assistant"},
                {"5", "Moshe", "CreditCard", "777", "888", "105", "Eli", "0539998877", "Supervisor"}
        };

        for (String[] data : supplierData) {
            suppliersDataBase.addSupplier(new Supplier(
                    data[0], data[1], data[2], data[3], data[4],
                    data[5], data[6], data[7], data[8]
            ));
        }

        // add products
        String[][] productData = {
                {"1", "Bamba"},
                {"2", "Bisli"},
                {"3", "Coca Cola"},
                {"4", "Sprite"},
                {"5", "Milk"},
                {"6", "Bread"},
                {"7", "Water Bottle"},
                {"8", "Cheese"},
                {"9", "Yogurt"},
                {"10", "Chocolate"},
                {"11", "Pita"},
                {"12", "Rice"},
                {"13", "Pasta"},
                {"14", "Ketchup"},
                {"15", "Toilet Paper"}
        };

        for (String[] data : productData) {
            productDataBase.addProduct(new Product(data[0], data[1]));
        }
        //add agreements
        String[][] agreementData = {
                {"1", "1"}, {"1", "3"}, {"1", "4"}, {"1", "5"}, {"2", "1"}, {"2", "2"},
                {"2", "4"}, {"2", "5"}, {"3", "1"}, {"3", "2"}, {"3", "3"}, {"3", "5"},
                {"4", "2"}, {"4", "3"}, {"5", "1"}, {"5", "3"}, {"5", "4"}, {"6", "1"},
                {"6", "4"}, {"6", "5"}, {"7", "1"}, {"7", "2"}, {"7", "5"}, {"8", "1"},
                {"8", "2"}, {"8", "3"}, {"9", "2"}, {"9", "3"}, {"9", "4"}, {"4", "4"},
                {"5", "5"}
        };

        for (String[] data : agreementData) {
            Agreement agreement = new Agreement(
                    branchesDataBase.getBranch(data[0]),
                    suppliersDataBase.getSupplier(data[1])
            );
            suppliersDataBase.addAgreement(agreement);
        }

        //add products to agreements
        Object[][] suppliedItems = {
                {10, "Bamba", "1", "1"},
                {13, "Pasta", "1", "1"},
                {15, "Bisli", "2", "2"},
                {20, "Coca Cola", "3", "3"},
                {25, "Sprite", "4", "4"},
                {30, "Milk", "5", "5"},
                {12, "Bread", "6", "1"},
                {18, "Water Bottle", "7", "2"},
                {22, "Cheese", "8", "3"},
                {14, "Yogurt", "9", "4"},
                {16, "Chocolate", "1", "5"},
                {11, "Pita", "2", "1"},
                {19, "Rice", "3", "2"},
                {13, "Pasta", "4", "3"},
                {17, "Ketchup", "5", "4"},
                {21, "Toilet Paper", "6", "5"},
                {9, "Bamba", "7", "1"},
                {8, "Bisli", "8", "2"},
                {10, "Coca Cola", "9", "3"},
                {20, "Sprite", "1", "4"},
                {24, "Milk", "2", "5"},
                {26, "Bread", "3", "1"},
                {7, "Water Bottle", "4", "2"},
                {6, "Cheese", "5", "3"},
                {5, "Yogurt", "6", "4"},
                {4, "Chocolate", "7", "5"},
                {3, "Pita", "8", "1"},
                {2, "Rice", "9", "2"},
                {1, "Pasta", "1", "3"},
                {30, "Ketchup", "2", "4"},
                {28, "Toilet Paper", "3", "5"}
        };

// First: add products to suppliers
        for (Object[] entry : suppliedItems) {
            String productName = (String) entry[1];
            String supplierId = (String) entry[3];

            suppliersDataBase.getSupplier(supplierId).addProduct(productDataBase.getProduct(productName));
        }

// Then: add supplied items to agreements
        for (Object[] entry : suppliedItems) {
            int quantity = (int) entry[0];
            String productName = (String) entry[1];
            String branchId = (String) entry[2];
            String supplierId = (String) entry[3];

            SuppliedItem item = new SuppliedItem(quantity, productDataBase.getProduct(productName));
            suppliersDataBase.addProductToAgreement(item, branchId, supplierId);
        }
    }
}
