package SupplierMoudleSource.DTO;

import java.util.Date;
import java.util.Map;

public class OrderDTO {
    private String orderID;
    private Date orderDate;
    private int totalPrice;
    private Map<SuppliedItemDTO, Integer> suppliedItems;
    private Boolean orderClosed;
    private String branch;
    private String suppler;

    public OrderDTO(String orderID, Date date, int totalPrice, Map<SuppliedItemDTO, Integer> suppliedItems, String branch, String  supplier) {
        this.orderID = orderID;
        this.orderDate = date;
        this.totalPrice = totalPrice;
        this.suppliedItems = suppliedItems;
        this.orderClosed = true;
        this.branch = branch;
        this.suppler = supplier;
    }

    public String getOrderID() {
        return orderID;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getBranchID() {
        return this.branch;
    }

    public String getSupplierID() {
        return suppler;
    }

    public Map<SuppliedItemDTO, Integer> getSuppliedItems() {
        return suppliedItems;
    }
}