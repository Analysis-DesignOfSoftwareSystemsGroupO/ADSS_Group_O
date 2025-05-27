package DTO;


public class DiscountDTO {
    public String suppliedItemid;
    public int quantity;
    public int discount;

    public DiscountDTO(String suppliedItemid, int quantity, int discount) {
        this.suppliedItemid = suppliedItemid;
        this.quantity = quantity;
        this.discount = discount;
    }

}
