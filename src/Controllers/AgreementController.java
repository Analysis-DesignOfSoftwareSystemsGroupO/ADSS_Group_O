package Controllers;
import Service.AgreementService;
import Service.BranchService;

public class AgreementController {
    private AgreementService agreementService = new AgreementService();
    private BranchService branchService = new BranchService();

    public void removeAgreement(String agreementId) {
        agreementService.removeAgreement(agreementId);
    }

    public void createNewAgreement(String supplierID, String branchId) {
        agreementService.createNewAgreement(supplierID, branchId);
    }

    //create a supplier and saves it in the database
    public void viewAllAgreements() {
        agreementService.viewAllAgreements();
    }

    // adds a product to the agreement
    public void addProductToAgreement(String agreementId, String productName, int quantity, int discount) {
        agreementService.addProductToAgreement(agreementId, productName, quantity, discount);
    }

    //checks if an agreement exists
    public boolean agreementExists(String supplierID, String branchId) {
        return agreementService.agreementExists(supplierID, branchId);
    }
    //check if an item exists in an Agreement
    public boolean productExistsInAgreement(String supplierID, String branchId, String productID) {
        return productExistsInAgreement(supplierID, branchId, productID);
    }

    public void viewAgreement(String agreementID) {
        agreementService.viewAgreement(agreementID);
    }

    public void editProductDiscount(String agreementID, String productId, int quantity, int discount){

    }

    public void removeProductFromAgreement(String agreementID, String productID) {

    }

}
