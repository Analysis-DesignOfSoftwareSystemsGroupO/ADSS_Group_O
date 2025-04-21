package Controllers;
import Service.AgreementService;
import Service.BranchService;

public class AgreementController {
    private AgreementService agreementService = new AgreementService();
    private BranchService branchService = new BranchService();

    public void removeAgreement(String branchId, String supplierId) {
        agreementService.removeAgreement(branchId, supplierId);
    }

    public void createNewAgreement(String supplierID, String branchId) throws Exception {
        agreementService.createNewAgreement(supplierID, branchId);
    }

    //create a supplier and saves it in the database
    public void viewAllAgreements() {
        agreementService.viewAllAgreements();
    }

    // adds a product to the agreement
    public void addProductToAgreement(String branchid, String supplierID, String productName, int quantity, int discount) {
        agreementService.addProductToAgreement(branchid, supplierID, productName, quantity, discount);
    }

    //checks if an agreement exists
    public boolean agreementExists(String supplierID, String branchId) {
        return agreementService.agreementExists(supplierID, branchId);
    }
    //view an agreement given branch id and supplier id
    public void viewAgreement(String branchId, String supplierID) {
        agreementService.viewAgreement(branchId, supplierID);
    }

    public void editProductDiscount(String branchId, String supplierID, String productId, int quantity, int discount){
        agreementService.editProductDiscount(supplierID, branchId, productId, quantity, discount);
    }

    public void removeProductFromAgreement(String supplierID, String branchId, String productID) {
        agreementService.removeProductFromAgreement(supplierID, branchId, productID);
    }



}
