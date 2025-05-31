package SupplierMoudleSource.DTO;

import java.util.List;

public class AgreementDTO {
    private  String supplierID;
    private  String branchId;
    private List<SuppliedItemDTO> supplierItemsList;
    private List<DiscountDTO> discounts;

    public AgreementDTO(String supplierID, String branchId, List<SuppliedItemDTO> supplierItemsList, List<DiscountDTO> discounts) {
        this.supplierID = supplierID;
        this.branchId = branchId;
        this.supplierItemsList = supplierItemsList;
        this.discounts = discounts;
    }

    public String getSupplierID() {
        return supplierID;
    }
    public String getBranchId() {
        return branchId;
    }

    public List<SuppliedItemDTO> getSupplierItemsList() {
        return supplierItemsList;
    }

    public List<DiscountDTO> getDiscounts() {
        return discounts;
    }
}