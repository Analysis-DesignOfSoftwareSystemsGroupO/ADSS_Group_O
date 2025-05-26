package dto;

import SupplierMoudleSource.Domain.Agreement;
import SupplierMoudleSource.Domain.Branch;
import SupplierMoudleSource.Domain.SuppliedItem;

import java.util.Date;
import java.util.Map;

public class OrderDTO {
    private String orderID;
    private Date orderDate;
    private int totalPrice;
    private Map<SuppliedItemDTO, Integer> suppliedItems;
    private Boolean orderClosed = false;
    private BranchDTO branch;
    private SupplierDTO suppler;

    public OrderDTO() {

    }

}
