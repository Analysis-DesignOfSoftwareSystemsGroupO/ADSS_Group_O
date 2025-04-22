package Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Agreement {
    private String agreementID;
    private final Supplier supplier;
    private final Branch Branch;
    private List<SuppliedItem> supplierItemsList;
    private List<Discount> discounts;

    public Agreement(Branch branch, Supplier supplier) {
        if (branch == null || supplier == null) {
            throw new NullPointerException();
        }
        this.supplier = supplier;
        this.Branch = branch;
        this.supplierItemsList = new ArrayList<SuppliedItem>();
        this.discounts = new ArrayList<Discount>();

    }

    public Agreement(Agreement other) {
        this.agreementID = other.agreementID;
        this.supplier = new Supplier(other.supplier);
        this.Branch = other.Branch;
        this.supplierItemsList = other.supplierItemsList;
        this.discounts = other.discounts;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public void addItem(SuppliedItem suppliedItem) {
        if (suppliedItem == null) {
            throw new NullPointerException();
        }
        supplierItemsList.add(suppliedItem);
    }

    //return true if a product in an agreement
    public boolean productInAgreement(Product product){
        for (SuppliedItem supplierItem : supplierItemsList){
            if (Objects.equals(product.getProductID(), supplierItem.getProduct().getProductID())){
                return true;
            }
        }
        return false;
    }

    public void addDiscount(Discount discount){
        if (discount == null){
            throw new NullPointerException();
        }
        discounts.add(discount);

    }

    public String getSupplierID(){
        return this.supplier.getID();
    }
    public String getBranchID(){ return this.Branch.getBranchID();}

    public void removeProduct(Product product){
        for (SuppliedItem supplierItem : supplierItemsList){
            if (Objects.equals(product.getProductName(), supplierItem.getProduct().getProductName())){
                supplierItemsList.remove(supplierItem);
                break;
            }
        }

       this.removeDiscount(product);
    }

    public void removeDiscount(Product product){
        for (Discount discount : discounts){
            if (Objects.equals(discount.getProductName(), product.getProductName())){
                discounts.remove(discount);
                break;
            }
        }
    }
    public List<SuppliedItem> getSupplierItemsList() {
        return supplierItemsList;
    }

    public List<Discount> getDiscountsList() {
        return discounts;
    }
    public SuppliedItem getSupplierItem(String productName){
        for (SuppliedItem supplierItem : supplierItemsList){
            if (Objects.equals(productName, supplierItem.getProduct().getProductName())){
                return supplierItem;
            }
        }
        return null;
    }
}
