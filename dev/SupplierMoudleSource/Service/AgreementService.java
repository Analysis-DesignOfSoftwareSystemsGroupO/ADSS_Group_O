package SupplierMoudleSource.Service;

import SupplierMoudleSource.DTO.AgreementDTO;
import SupplierMoudleSource.DTO.BranchDTO;
import SupplierMoudleSource.DTO.SupplierDTO;
import SupplierMoudleSource.Repository.AgreementRepository;
import SupplierMoudleSource.Repository.BranchesRepository;
import SupplierMoudleSource.Repository.ProductRepository;
import SupplierMoudleSource.Repository.SupplierRepository;
import SupplierMoudleSource.Domain.*;

import java.util.List;

public class AgreementService {
    SupplierRepository supplierRepository = SupplierRepository.getInstance();
    AgreementRepository agreementRepository = AgreementRepository.getInstance();
    BranchesRepository branchesDataBase = BranchesRepository.getInstance();
    ProductRepository productRepository = ProductRepository.getInstance();
    BranchesRepository branchRepository = BranchesRepository.getInstance();

    //removes an agreement
    public void removeAgreement(String branchId, String supplierId) throws Exception {
        agreementRepository.removeAgreement(branchId, supplierId);
    }
    //creates new agreement
    public void createNewAgreement(String supplierID, String branchId) throws Exception {
        if (agreementRepository.isAgreementExist(branchId, supplierID)){ // if an agreement exist throw
            throw new Exception("Agreement already exist");
        }
        BranchDTO branchDTO = branchRepository.getBranch(branchId);
        SupplierDTO supplierDTO = supplierRepository.getSupplier(supplierID);
        if (branchDTO == null){
            throw new Exception("Branch does not exist");
        }
        if (supplierDTO == null){
            throw new Exception("Supplier does not exist");
        }
        Supplier supplier = new Supplier(supplierDTO);
        Branch branch = new Branch(branchDTO);

        Agreement agreement = new Agreement(branch, supplier);
        agreementRepository.addAgreement(agreement.getAgreementDTO());
    }

    //print all agreement
    public void viewAllAgreements() throws Exception {
        List<AgreementDTO> agreements = agreementRepository.getAllAgreement();

        for (AgreementDTO agreementDTO : agreements){
            System.out.println("*********************************************************");
            BranchDTO branchDTO = branchRepository.getBranch(agreementDTO.getBranchId());
            SupplierDTO supplierDTO = supplierRepository.getSupplier(agreementDTO.getSupplierID());
            if (branchDTO == null){
                throw new Exception("Branch does not exist");
            }
            if (supplierDTO == null){
                throw new Exception("Supplier does not exist");
            }
            Agreement agreement = new Agreement(branchDTO, supplierDTO, agreementDTO);
            System.out.println(agreement);
        }
        System.out.println("**********************************************************");
    }

    // adds a product to the agreement
    public void addProductToAgreement(String branchid, String supplierID, String productID, int price,
                                      Integer quantity, Integer discount) throws Exception {
        if (quantity != null && quantity <= 0){
            throw new Exception("quantity have to be positive");
        }
        if (discount != null && discount <= 0){
            throw new Exception("discount have to be positive");
        }

        SuppliedItem suppliedItem = getProductFromSupplier(productID, supplierID);
        agreementRepository.addProductToAgreement(suppliedItem.getSuppliedItemDTO(), branchid, supplierID);
        if (quantity != null && discount != null){ //add discount if needed
            if ((price * quantity) < discount){
                throw new Exception("Cannot confirm discount because discount will cause negative price");
            }
            Discount discount1 = new Discount(suppliedItem, quantity, discount);
            agreementRepository.addDiscountToAgreement(branchid, supplierID, discount1.getDiscountDTO());
        }

    }



    //views an agreement given branch id and supplier id (ued for creating a new order)
    public void viewAgreement(String branchId, String supplierID) throws Exception {
        AgreementDTO agreementDTO = agreementRepository.getAgreement(branchId, supplierID);
        if (agreementDTO != null){
            BranchDTO branchDTO = branchRepository.getBranch(agreementDTO.getBranchId());
            SupplierDTO supplierDTO = supplierRepository.getSupplier(agreementDTO.getSupplierID());
            Agreement agreement = new Agreement(branchDTO, supplierDTO, agreementDTO);
            System.out.println(agreement);
        }
    }
    //removes a product from an existing agreement
    public void removeProductFromAgreement(String supplierID, String branchId, String productID) throws Exception {
        Agreement agreement = getAgreement(branchId, supplierID);
        if (!agreement.productInAgreement(productID)){
            throw new Exception("Agreement does not have this product");
        }
        agreement.removeProduct(productID);
        agreementRepository.removeProductFromAgreement(productID, branchId, supplierID);
    }

    //edits a product discount from an existing agreement
    public void editProductDiscount(String supplierID, String branchId, String productID, int quantity, int discount) throws Exception {
        if (quantity <= 0 || discount <= 0){
            throw new Exception("Quantity and discount amount have to be positive");
        }
        Agreement agreement = getAgreement(branchId, supplierID);
        if (!agreement.productInAgreement(productID)){
            throw new Exception("Agreement does not have this product");
        }
        agreement.removeDiscount(productID);
        //add the Discount
        agreement.addDiscount(new Discount(agreement.getSupplierItem(productID), quantity, discount));
    }



    private Agreement getAgreement(String branchId, String supplierID) throws Exception {

        SupplierDTO supplierDTO =  supplierRepository.getSupplier(supplierID);
        if (supplierDTO == null){
            throw new Exception("supplier does not exist");
        }
        BranchDTO branchDTO = branchRepository.getBranch(branchId);
        if (branchDTO ==null){
            throw new Exception("branch does not exist!");
        }
        AgreementDTO agreementDTO = agreementRepository.getAgreement(branchId, supplierID);
        if (agreementDTO == null) {
            throw new Exception("agreement does not exist");
        }
        return new Agreement(branchDTO, supplierDTO, agreementDTO);
    }

    private SuppliedItem getProductFromSupplier(String productID, String supplierID) throws Exception {
        SupplierDTO supplierDTO =  supplierRepository.getSupplier(supplierID);
        if (supplierDTO == null){
            throw new Exception("supplier does not exist");
        }
        Supplier supplier = new Supplier(supplierDTO);
        SuppliedItem product = supplier.getProduct(productID);
        if (product == null){
            throw new Exception("supplier does not have this product");
        }
        return product;
    }

    //checks if an agreement is empty
    public boolean isAgreementEmpty(String branchId, String supplierId) throws Exception {
        return agreementRepository.getAgreement(branchId, supplierId).getSupplierItemsList().isEmpty();
    }
}