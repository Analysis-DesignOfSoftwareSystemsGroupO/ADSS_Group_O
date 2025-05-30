package SupplierMoudleSource.Repository;

import DTO.AgreementDTO;
import DTO.DiscountDTO;
import DTO.SuppliedItemDTO;
import SupplierMoudleSource.DAO.AgreementDAO;

import java.sql.SQLException;
import java.util.*;

public class AgreementRepository {
    private static AgreementRepository instance;
    private Map<SupplierBranchKey, AgreementDTO> suppliersAgreements;
    private AgreementDAO agreementDAO;

    private AgreementRepository() {
        suppliersAgreements = new HashMap<>();
        agreementDAO = new AgreementDAO();
    }

    public static AgreementRepository getInstance() {
        if (instance == null) {
            instance = new AgreementRepository();
        }
        return instance;
    }


    public void removeAgreement(String branchId, String supplierID) throws Exception {
        for (SupplierBranchKey branchKey : suppliersAgreements.keySet()) {
            if (Objects.equals(branchKey.branchID(), branchId) && Objects.equals(branchKey.supplierID(), supplierID)) {
                suppliersAgreements.remove(branchKey);
                return;
            }
        }
        try {
            agreementDAO.removeAgreement(branchId, supplierID);
        } catch (Exception e) {
            throw new Exception("Agreement does not exist");
        }
    }

    public void removeProductFromAgreement(String productID, String branchId, String supplierID) throws Exception {
        agreementDAO.removeProductFromAgreement(branchId, supplierID, productID);
    }

    public void addAgreement(AgreementDTO agreement) throws Exception {
        SupplierBranchKey supBKey = new SupplierBranchKey(agreement.getSupplierID(), agreement.getBranchId());
        suppliersAgreements.put(supBKey, agreement);
        agreementDAO.addAgreement(agreement.getBranchId(), agreement.getSupplierID());
    }

    public boolean isAgreementExist(String branchId, String supplierID) throws Exception {
        return agreementDAO.checkAgreement(branchId, supplierID);
    }


    public AgreementDTO getAgreement(String branchID, String supplierID) throws Exception {
        if (supplierID == null || branchID == null) {
            throw new NullPointerException("branchID or supplierID does not exist");
        }
        for (SupplierBranchKey key : suppliersAgreements.keySet()) {
            if (Objects.equals(key.branchID(), branchID) && Objects.equals(key.supplierID(), supplierID)) {
                return suppliersAgreements.get(key);
            }
        }
        try {
            AgreementDTO agreementDTO = agreementDAO.getAgreement(branchID, supplierID);
            suppliersAgreements.put(new SupplierBranchKey(supplierID, branchID), agreementDTO);
            return agreementDTO;

        } catch (Exception e) {
            throw new Exception("Agreement does not exist");
        }
    }

    //add product to agreement
    public void addProductToAgreement(SuppliedItemDTO product, String branchID, String supplierID) throws Exception {
        for (SupplierBranchKey key : suppliersAgreements.keySet()) {
            if (key.branchID().equals(branchID) && Objects.equals(key.supplierID(), supplierID)) {
                suppliersAgreements.get(key).getSupplierItemsList().add(product);
            }
        }

        agreementDAO.addProductToAgreement(branchID, supplierID, product);
    }

    public void addDiscountToAgreement(String branchID, String supplierID, DiscountDTO discount) throws Exception {
        SupplierBranchKey supBKey = new SupplierBranchKey(branchID, supplierID);
        if (suppliersAgreements.containsKey(supBKey)) {
            suppliersAgreements.get(supBKey).getDiscounts().add(discount);
        }
        try {
            this.agreementDAO.addDiscountToAgreement(branchID, supplierID, discount);

        } catch (SQLException e) {
            throw new Exception("Agreement doesnt exist");
        }

    }
    public List<AgreementDTO> getAllAgreement() throws Exception {
        List<AgreementDTO> agreements;
        try {
            agreements = agreementDAO.getAllAgreement();
            for (AgreementDTO agreement : agreements) { //add to rep
                this.suppliersAgreements.put(new SupplierBranchKey(agreement.getSupplierID(), agreement.getBranchId()), agreement);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to get agreements: " + e.getMessage(), e);
        }

        return agreements;

    }


    public record SupplierBranchKey(String supplierID, String branchID) {

        public boolean equals(String supplierID, String branchID) {
            return this.supplierID.equals(supplierID) && this.branchID.equals(branchID);
        }
    }
}