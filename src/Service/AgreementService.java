package Service;

import DataBase.BranchesDataBase;
import DataBase.ProductDataBase;
import DataBase.SuppliersDataBase;
import Domain.Agreement;
import Domain.Branch;
import Domain.Supplier;

import java.util.Objects;

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
            throw new Exception("agreement already exist");
        }
        Branch branch = branchesDataBase.getBranch(branchId);
        Supplier supplier = suppliersDataBase.getSupplier(supplierID);
        if (branch == null){
            throw new Exception("branch does not exist");
        }
        if (supplier == null){
            throw new Exception("supplier does not exist");
        }
        Agreement agreement = new  Agreement(branch, supplier);
        suppliersDataBase.addAgreement(supplier, branch, agreement);
}

    //print all agreement
    public void viewAllAgreements() {
        for (Agreement agreement : suppliersDataBase.getAllAgreement()){
            System.out.println(agreement.toString());
        }
    }

    // adds a product to the agreement
    public void addProductToAgreement(String branchid, String  supplierID, String productName, int quantity, int discount) {
       //todo
    }

    //checks if an agreement exists
    public boolean agreementExists(String SupplierID, String branchid) {
        return suppliersDataBase.getAgreement(branchid, SupplierID) != null;
    }
    //check if an item exists in an Agreement
    public boolean productExistsInAgreement(String supplierID, String branchId, String productname) throws Exception {
        Agreement agreement = suppliersDataBase.getAgreement(branchId, supplierID);
        if (agreement == null) {
            throw new Exception("no agreement");
        }
        return agreement.productInAgreement(productDataBase.getProduct(productname));
    }
    //views an agreement given branch id and supplier id (ued for creating a new order)
    public void viewAgreement(String branchId, String supplierID) {
        System.out.println(suppliersDataBase.getAgreement(branchId, supplierID));
    }
    //removes a product from an existing agreement
    public void removeProductFromAgreement(String supplierID, String branchId, String productname) {

    }
    //edits a product discount from an existing agreement
    public void editProductDiscount(String supplierID, String branchId, String productId, int quantity, int discount){
        //todo
    }

}
