package dto;

import SupplierMoudleSource.Domain.Product;

public class SuppliedItemDTO {
    public int suppliedItemPrice;
    public ProductDTO product;
    public String suppliedItemID;

    public SuppliedItemDTO(int suppliedItemPrice, ProductDTO product, String suppliedItemID) {
        this.suppliedItemPrice = suppliedItemPrice;
        this.product = product;
        this.suppliedItemID = suppliedItemID;
    }
}
