package SupplierMoudleSource.Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Agreement {
    private final Supplier supplier;
    private final Branch Branch;
    private List<SuppliedItem> supplierItemsList;
    private List<Discount> discounts;


    public Agreement(Branch branch, Supplier supplier) {
        if (branch == null || supplier == null) {
            throw new NullPointerException("Supplier or branch is null");
        }
        this.supplier = supplier;
        this.Branch = branch;
        this.supplierItemsList = new ArrayList<SuppliedItem>();
        this.discounts = new ArrayList<Discount>();

    }

    public Agreement(Agreement other) {
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
        for (SuppliedItem supplierItem : supplierItemsList){
            if (Objects.equals(suppliedItem.getProduct().getProductName(), supplierItem.getProduct().getProductName())){
                throw new IllegalArgumentException(suppliedItem.getProduct().getProductName() +
                        " already exists in the agreement");
            }
        }
        if (supplier.getProduct(suppliedItem.getProduct().getProductID()) == null) {
            throw new IllegalArgumentException("Supplier doesnt have this product (" +
                    suppliedItem.getProduct().getProductName() + ")");
        }
        supplierItemsList.add(suppliedItem);
    }

    //return true if a product in an agreement
    public boolean productInAgreement(String productID){
        for (SuppliedItem supplierItem : supplierItemsList){
            if (Objects.equals(productID, supplierItem.getProduct().getProductID())){
                return true;
            }
        }
        return false;
    }

    public void addDiscount(Discount discount){
        if (discount == null){
            throw new NullPointerException();
        }
        for (Discount discountItem : discounts){
            if (discountItem.getProductId().equals(discount.getProductId())){
                throw new IllegalArgumentException("Discount already exists in the agreement");
            }
        }

        for (SuppliedItem supplierItem : supplierItemsList){
            if (Objects.equals(supplierItem.getProduct().getProductID(), discount.getProductId())){
                discounts.add(discount);
                return;
            }
        }
        throw new NullPointerException("Product does not exist in the agreement");


    }

    public String getSupplierID(){
        return this.supplier.getID();
    }
    public String getBranchID(){ return this.Branch.getBranchID();}

    public void removeProduct(String productID){
        if (productID == null){
            throw new NullPointerException("Product id was null");
        }
        for (SuppliedItem supplierItem : supplierItemsList){
            if (Objects.equals(productID, supplierItem.getProduct().getProductID())){
                try {
                    this.removeDiscount(productID);
                }catch (NullPointerException _){
                }
                supplierItemsList.remove(supplierItem);
                return;
            }
        }
        throw new NullPointerException("product does not exist in the agreement");
    }

    public void removeDiscount(String productID){
        if (productID == null){
            throw new NullPointerException("Product id was null");
        }
        for (Discount discount : discounts){
            if (Objects.equals(discount.getSuppliedItem().getSuppliedItemID(), productID)){
                discounts.remove(discount);
                return;
            }
        }
        throw new NullPointerException("discount does not exist in the agreement");
    }
    public List<SuppliedItem> getSupplierItemsList() {
        return supplierItemsList;
    }

    public SuppliedItem getSupplierItem(String productID){
        for (SuppliedItem supplierItem : supplierItemsList){
            if (Objects.equals(productID, supplierItem.getSuppliedItemID())){
                return supplierItem;
            }
        }
        return null;
    }

    public String toString(){
        StringBuilder returnString = new StringBuilder("Agreement between, " + "Supplier id: " + this.supplier.getID() +
                ", Branch id : " + this.Branch.getBranchID() + "\n" + "Product List: \n");
        for (SuppliedItem supplierItem : supplierItemsList){
            returnString.append(supplierItem.toString()).append(" Price: ").append(supplierItem.getSuppliedItemPrice()).append("₪\n");
        }

        for (Discount discount : discounts){
            returnString.append(discount.toString()).append("\n");
        }
        return returnString.toString();
    }
}
