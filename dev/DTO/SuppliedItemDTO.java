package DTO;

public class SuppliedItemDTO {
    public int suppliedItemPrice;
    public ProductDTO product;

    public SuppliedItemDTO(int suppliedItemPrice, ProductDTO product) {
        this.suppliedItemPrice = suppliedItemPrice;
        this.product = product;
    }
}