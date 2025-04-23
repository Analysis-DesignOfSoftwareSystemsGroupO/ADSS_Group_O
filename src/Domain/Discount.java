package Domain;

public class Discount {
    private SuppliedItem suppliedItem;
    private int quantity;
    private int discount;

    public Discount(SuppliedItem suppliedItem, int quantity, int discount) {
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

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void updateDiscount(int discount) {
        this.discount = discount;
    }

    public String getProductName(){
        return this.suppliedItem.getProduct().getProductName();
    }
    public int getQuantity() {
        return quantity;
    }

    public String toString() {
        return "Buy " + this.quantity + " of " + this.suppliedItem.getProduct().getProductName() + " get " +
                this.discount + "₪ discount";
    }
}
