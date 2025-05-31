
package SupplierMoudleSource.DataBase.LoadData;
import SupplierMoudleSource.DAO.AgreementDAO;
import SupplierMoudleSource.DAO.BranchDAO;
import SupplierMoudleSource.DAO.ProductDAO;
import SupplierMoudleSource.DAO.SupplierDAO;
import SupplierMoudleSource.DTO.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoadData {
    private ProductDAO productDataBase = new ProductDAO();
    private final BranchDAO branchesDataBase = new BranchDAO();
    private SupplierDAO suppliersDataBase = new SupplierDAO();
    private AgreementDAO agreementDAO = new AgreementDAO();

    public static void main(String[] args) throws Exception {
        LoadData loadData = new LoadData();
        loadData.LoadData();
    }
    //loads data to dataBase
    public void LoadData() throws Exception {
        //add branches
        String[][] branchData = {
                { "Beer Sheva", "Rager 101"},
                {"Lehavim", "HaGefen 12"},
                {"Tel Aviv", "Ibn Gabirol 25"},
                {"Jerusalem", "King George 10"},
                {"Haifa", "Herzl 15"},
                {"Ramat Gan", "Jabotinsky 120"},
                {"Netanya", "Ben Gurion 45"},
                {"Eilat", "Sderot Hatmarim 3"},
                {"Petah Tikva", "Bar Kochva 78"}
        };
        for (String[] data : branchData) {
            try {
                branchesDataBase.addBranch(data[0], data[1]);
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // add suppliers with full data for the constructor
        String[][] supplierData = {
                {"1", "Yosi", "CreditCard", "12", "286", "570", "Noa", "0501234567", "Manager", "Constant Delivery", "Sunday"},
                {"2", "Dina", "Cash", "123", "12", "102", "Rami", "0507654321", "Sales", "Temporary Delivery", ""},
                {"3", "Avi", "Check", "111", "10", "103", "Lior", "0521112233", "Clerk", "Self Pick Up", ""},
                {"4", "Liat", "Bank Transfer", "11", "555", "104", "Dana", "0543334455", "Assistant", "Self Pick Up", ""},
                {"5", "Moshe", "CreditCard", "14", "888", "105", "Eli", "0539998877", "Supervisor", "Self Pick Up", ""}
        };

        for (String[] data : supplierData) {
            BankDTO bankDTO = new BankDTO(data[3], data[4], data[5], data[0]);
            DeliveryDTO deliveryDTO = new DeliveryDTO(data[9]);
            InformationContactDTO informationContactDTO = new InformationContactDTO(data[6], data[7], data[8]);
            List<InformationContactDTO> informationContactDTOList = new ArrayList<>();
            informationContactDTOList.add(informationContactDTO);
            PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO(data[2]);
            suppliersDataBase.addSupplier(data[1], bankDTO, paymentMethodDTO, deliveryDTO, informationContactDTO);
        }

        // add products
        Object[][] productData = {
                {"1", "Bamba", "Osem", 60},
                {"2", "Bisli", "Osem", 60},
                {"3", "Cola", "Coca Cola", 120},
                {"4", "Sprite", "Tempo", 120},
                {"5", "Milk", "Tnuva", 14},
                {"6", "Bread", "Ariel Bakery", 7},
                {"7", "Water Bottle", "Tempo", 100},
                {"8", "Cheese", "Tnuva", 30},
                {"9", "Yogurt", "Shtraus", 21},
                {"10", "Chocolate", "Elit", 60},
                {"11", "Pita", "Ariel Bakery", 7},
                {"12", "Rice", "Osem", 80},
                {"13", "Pasta", "Osem", 80},
                {"14", "Ketchup", "Osem", 90},
                {"15", "Toilet Paper", "SuperLi", 365},
                {"16", "Bamba", "Lulu", 60}
        };
        //add to product dataBase
        for (Object[] data : productData) {
            try {
                productDataBase.addProduct((String) data[1], (String) data[2], (int) data[3]);
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //add agreements
        String[][] agreementData = {
                { "1", "1" },
                { "1", "3" },
                { "1", "4" },
                { "1", "5" },
                { "2", "1" },
                { "2", "2" },
                { "2", "4" },
                { "2", "5" },
                { "3", "1" },
                { "3", "2" },
                { "3", "3" },
                { "3", "5" },
                { "4", "2" },
                { "4", "3" },
                { "5", "1" },
                { "5", "3" },
                { "5", "4" },
                { "6", "1" },
                { "6", "4" },
                { "6", "5" },
                { "7", "1" },
                { "7", "2" },
                { "7", "5" },
                { "8", "1" },
                { "8", "2" },
                { "8", "3" },
                { "9", "2" },
                { "9", "3" },
                { "9", "4" },
                { "4", "4" },
                { "5", "5" }
        };

        for (String[] data : agreementData) {
            agreementDAO.addAgreement(data[0], data[1]);
        }


    //add products to agreements
        Object[][] items = {
                { 17, "1", "3", "1"},
                { 14, "2", "7", "2" },
                { 22, "3", "1", "4"},
                { 27, "4", "8", "3" },
                { 10, "5", "6", "5"},
                { 24, "6", "9", "2" },
                { 19, "7", "2", "1" },
                { 11, "8", "5", "3" },
                { 13, "9", "4", "4" },
                { 29, "10", "7", "1" },
                { 9,  "11", "1", "5" },
                { 20, "12", "3", "5" },
                { 25, "13", "6", "4"},
                { 15, "14", "9", "4" },
                { 12, "15", "2", "1" },
                { 18, "16", "8", "3" }
        };


// First: add products to suppliers
        for (Object[] entry : items) {
            String productID = (String) entry[1];
            String supplierId = (String) entry[3];
            int price = (int) entry[0];
            try {
                ProductDTO productDTO = productDataBase.getProduct(productID);
                SuppliedItemDTO suppliedItemDTO = new SuppliedItemDTO(price, productDTO);
                suppliersDataBase.addProduct(supplierId, suppliedItemDTO);

            }catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Product : " + productID);
                System.out.println("Supplier : " + supplierId);
            }
        }

        Random rand = new Random();
// Then: add supplied items to agreements
        for (Object[] entry : items) {
            try {
                int price = (int) entry[0];
                String productID = (String) entry[1];
                String branchId = (String) entry[2];
                String supplierId = (String) entry[3];
                SuppliedItemDTO item = new SuppliedItemDTO(price, productDataBase.getProduct(productID));
                agreementDAO.addProductToAgreement(branchId, supplierId, item);
                if (rand.nextBoolean()){
                    int quantity = rand.nextInt(20) + 1;
                    int price1 = rand.nextInt(40) + 1;
                    if (quantity * price < price1){ // to avoid errors
                        continue;
                    }
                    agreementDAO.addDiscountToAgreement(branchId, supplierId, new DiscountDTO(item.product.productID, quantity, price1));

                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("error int adding items to agreement");
            }
            System.out.println("✅ Data loaded successfully.");

        }
    }
}
