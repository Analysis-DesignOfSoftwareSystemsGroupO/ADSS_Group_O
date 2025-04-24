package Service;

import DataBase.BranchesDataBase;
import DataBase.ProductDataBase;
import DataBase.SuppliersDataBase;
import Domain.*;

public class AgreementService {
    SuppliersDataBase suppliersDataBase= SuppliersDataBase.getInstance();
    BranchesDataBase branchesDataBase = BranchesDataBase.getInstance();
    ProductDataBase productDataBase = ProductDataBase.getInstance();

    //removes an agreement
public void removeAgreement(String branchId, String supplierId) {
        suppliersDataBase.removeAgreement(branchId, supplierId);
    }
    //creates new agreement
    public void createNewAgreement(String supplierID, String branchId) throws Exception {
        if (suppliersDataBase.getAgreement(branchId, supplierID) != null){ // if an agreement exist throw
            throw new Exception("Agreement already exist");
        }
        Branch branch = branchesDataBase.getBranch(branchId);
        Supplier supplier = suppliersDataBase.getSupplier(supplierID);
        if (branch == null){
            throw new Exception("Branch does not exist");
        }
        if (supplier == null){
            throw new Exception("Supplier does not exist");
        }

        Agreement agreement = new Agreement(branch, supplier);
        suppliersDataBase.addAgreement(agreement);
}

    //print all agreement
    public void viewAllAgreements() {
        for (Agreement agreement : suppliersDataBase.getAllAgreement()){
            System.out.println("*********************************************************");
            System.out.println(agreement.toString());
        }
        System.out.println("**********************************************************");
    }

    // adds a product to the agreement
    public void addProductToAgreement(String branchid, String supplierID, String productName, int price,
                                      Integer quantity, Integer discount) throws Exception {
        if (quantity != null && quantity <= 0){
            throw new Exception("quantity have to be positive");
        }
        if (discount != null && discount <= 0){
            throw new Exception("discount have to be positive");
        }

        SuppliedItem suppliedItem = getProductFromSupplier(productName, supplierID);
        suppliersDataBase.addProductToAgreement(suppliedItem, branchid, supplierID);
        if (quantity != null && discount != null){ //add discount if needed
            if ((price * quantity) < discount){
                throw new Exception("Cannot confirm discount because discount will cause negative price");
            }
            Discount discount1 = new Discount(suppliedItem, quantity, discount);
            suppliersDataBase.addDiscountToAgreement(branchid, supplierID, discount1);
        }

    }

    //checks if an agreement exists
    public boolean agreementExists(String SupplierID, String branchid) {
        return suppliersDataBase.getAgreement(branchid, SupplierID) != null;
    }
    //check if an item exists in an Agreement
    public boolean productExistsInAgreement(String supplierID, String branchId, String productID) throws Exception {
        Agreement agreement = suppliersDataBase.getAgreement(branchId, supplierID);
        if (agreement == null) {
            throw new Exception("no agreement");
        }
        return agreement.productInAgreement(productID);
    }
    //views an agreement given branch id and supplier id (ued for creating a new order)
    public void viewAgreement(String branchId, String supplierID) {
        if (suppliersDataBase.getAgreement(branchId, supplierID) != null){
            System.out.println(suppliersDataBase.getAgreement(branchId, supplierID).toString());
        }
    }
    //removes a product from an existing agreement
    public void removeProductFromAgreement(String supplierID, String branchId, String productID) throws Exception {
        Agreement agreement = getAgreement(branchId, supplierID);
        if (!agreement.productInAgreement(productID)){
            throw new Exception("Agreement does not have this product");
        }
        agreement.removeProduct(productID);
    }

    //edits a product discount from an existing agreement
    public void editProductDiscount(String supplierID, String branchId, String productID, int quantity, int discount) throws Exception {

        Agreement agreement = getAgreement(branchId, supplierID);
        if (!agreement.productInAgreement(productID)){
            throw new Exception("Agreement does not have this product");
        }
        agreement.removeDiscount(productID);
        //add the Discount
        agreement.addDiscount(new Discount(agreement.getSupplierItem(productID), quantity, discount));
    }


    public void changeDeliveryWay(String supplierID, String branchId, String deliveryWay) throws Exception {
        if (suppliersDataBase.getAgreement(branchId, supplierID) != null){
            Agreement agreement = getAgreement(branchId, supplierID);
            agreement.getDelivery().setDeliveryWay(deliveryWay);
        }
    }

    private Agreement getAgreement(String branchId, String supplierID) throws Exception {
        Agreement agreement = suppliersDataBase.getAgreement(branchId, supplierID);
        if (agreement == null) {
            throw new Exception("agreement does not exist");
        }
        Supplier supplier =  suppliersDataBase.getSupplier(supplierID);
        if (supplier == null){
            throw new Exception("supplier does not exist");
        }
        if (!branchesDataBase.existsBranch(branchId)){
            throw new Exception("branch does not exist");
        }
        return agreement;
    }

    private SuppliedItem getProductFromSupplier(String productID, String supplierID) throws Exception {
        Supplier supplier =  suppliersDataBase.getSupplier(supplierID);
        if (supplier == null){
            throw new Exception("supplier does not exist");
        }
        SuppliedItem product = supplier.getProduct(productID);
        if (product == null){
            throw new Exception("supplier does not have this product");
        }
        return product;
    }

    //checks if an agreement is empty
    public boolean isAgreementEmpty(String branchId, String supplierId) {
        return suppliersDataBase.getAgreement(branchId, supplierId).getSupplierItemsList().isEmpty();
    }
}
