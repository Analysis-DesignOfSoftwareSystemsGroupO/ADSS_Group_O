package DTO;

import SupplierMoudleSource.Domain.Branch;
import SupplierMoudleSource.Domain.Discount;
import SupplierMoudleSource.Domain.SuppliedItem;
import SupplierMoudleSource.Domain.Supplier;

import java.util.List;
import java.util.Map;

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
}