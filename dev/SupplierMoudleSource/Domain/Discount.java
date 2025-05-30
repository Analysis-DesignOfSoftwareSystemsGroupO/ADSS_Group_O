package SupplierMoudleSource.Domain;

import DTO.DiscountDTO;
import DTO.SuppliedItemDTO;

import java.util.Objects;

public class Discount {
    private SuppliedItem suppliedItem;
    private int quantity;
    private int discount;

    public Discount(SuppliedItem suppliedItem, int quantity, int discount) {
        if (suppliedItem == null) {
            throw new IllegalArgumentException("SuppliedItem cannot be null");
        }
        if (suppliedItem.getSuppliedItemPrice() * quantity < discount) {
            throw new IllegalArgumentException("SuppliedItemPrice cannot be less than quantity * price");
        }
        this.suppliedItem = suppliedItem;
        this.quantity = quantity;
        this.discount = discount;
    }

    public Discount(DiscountDTO discountDTO, SuppliedItemDTO suppliedItemDTO) {
        if (discountDTO == null) {
            throw new IllegalArgumentException("DiscountDTO cannot be null");
        }
        if (!Objects.equals(suppliedItemDTO.product.productID, discountDTO.suppliedItemid)){
            throw new IllegalArgumentException("Cant build discount with unmatching productid");
        }
        this.suppliedItem = new SuppliedItem(suppliedItemDTO);
        this.quantity = discountDTO.quantity;
        this.discount = discountDTO.discount;
    }

    public DiscountDTO getDiscountDTO() {
        return new DiscountDTO(this.suppliedItem.getSuppliedItemID(), this.quantity, this.discount);
    }

    public int getDiscount() {
        return discount;
    }

    public SuppliedItem getSuppliedItem() {
        return suppliedItem;
    }

    public String getProductId(){
        return this.suppliedItem.getProduct().getProductID();
    }

    public int getQuantity() {
        return quantity;
    }

    public String toString() {
        return "Buy " + this.quantity + " of " + this.suppliedItem.getProduct().getProductName() + " get " +
                this.discount + "₪ discount";
    }
}