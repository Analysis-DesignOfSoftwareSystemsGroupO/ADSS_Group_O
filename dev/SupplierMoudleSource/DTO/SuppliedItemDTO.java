package SupplierMoudleSource.DTO;

public class SuppliedItemDTO {
    public int suppliedItemPrice;
    public ProductDTO product;

    public SuppliedItemDTO(int suppliedItemPrice, ProductDTO product) {
        this.suppliedItemPrice = suppliedItemPrice;
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SuppliedItemDTO that = (SuppliedItemDTO) o;

        return this.product.productID.equals(that.product.productID);
    }

    @Override
    public int hashCode() {
        return product.productID.hashCode();
    }
}