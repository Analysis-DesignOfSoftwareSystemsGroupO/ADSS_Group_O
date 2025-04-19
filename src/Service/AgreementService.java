package Service;

import Domain.Branch;

public class AgreementService {
    public void removeAgreement(String agreementID) {
        //todo
    }

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

    public void viewAgreement(String agreementID) {
        //todo
    }

    public void removeProductFromAgreement(String agreementID, String productID) {
        //todo
    }
}
