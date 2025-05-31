package SupplierMoudleSource.DTO;

import java.util.Map;

public class requirementToConstantOrderDTO {
    private Map<SuppliedItemDTO, Integer> suppliedItems;
    private String branchID;
    private String supplerID;
    private String dayOfWeek;

    public requirementToConstantOrderDTO(String branchID, String  supplierID, Map<SuppliedItemDTO, Integer> suppliedItems, String dayOfWeek) {
        this.branchID = branchID;
        this.supplerID = supplierID;
        this.suppliedItems = suppliedItems;
        this.dayOfWeek = dayOfWeek;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getSupplierID() {
        return supplerID;
    }

    public String getBranchID() {
        return branchID;
    }

    public Map<SuppliedItemDTO, Integer> getSuppliedItems() {
        return suppliedItems;
    }

}