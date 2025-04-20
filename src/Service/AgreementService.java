package Service;

import Domain.Branch;

public class AgreementService {
    //removes an agreement
    public void removeAgreement(String agreementID) {
        //todo
    }
    //creates new agreement
    public void createNewAgreement(String supplierID, String branchId) {
        //todo
    }

    //create a supplier and saves it in the database
    public void viewAllAgreements() {
        //todo
    }

    // adds a product to the agreement
    public void addProductToAgreement(String agreementId, String productName, int quantity, int discount) {
        //todo
    }

    //checks if an agreement exists
    public boolean agreementExists(String SupplierID, String branch) {
        return false;
        //todo
    }
    //check if an item exists in an Agreement
    public boolean productExistsInAgreement(String supplierID, String branchId, String productID) {
        return false;
        //todo
    }
    //view agreement given agreement id's (used for editing existing agreement)
    public void viewAgreement(String agreementID) {
        //todo
    }
    //views an agreement given branch id and supplier id (ued for creating a new order)
    public void viewAgreement(String branchId, String supplierID) {
        //todo
    }
    //removes a product from an existing agreement
    public void removeProductFromAgreement(String agreementID, String productID) {
        //todo
    }
    //edits a product discount from an existing agreement
    public void editProductDiscount(String agreementID, String productId, int quantity, int discount){
        //todo
    }

}
