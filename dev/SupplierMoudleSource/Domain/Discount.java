package SupplierMoudleSource.Domain;

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
    public int getDiscount() {
        return discount;
    }

    public SuppliedItem getSuppliedItem() {
        return suppliedItem;
    }

    public String getProductId(){
        return this.suppliedItem.getProduct().getProductID();
    }

    public String toString() {
        return "Buy " + this.quantity + " of " + this.suppliedItem.getProduct().getProductName() + " get " +
                this.discount + "₪ discount";
    }
}
