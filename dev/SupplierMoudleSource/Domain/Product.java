package SupplierMoudleSource.Domain;

import DTO.ProductDTO;

public class Product {
    private final String productID;
    private final String productName;
    private final String productManufacturer;
    private final int shelfLifeDays;

    public Product(String productID, String productName, String productManufacturer,  int shelfLifeDays) {
        if (productID == null || productName == null || productManufacturer == null) {
            throw new NullPointerException("Product Details cannot be null");
        }
        this.productID = productID;
        this.productName = productName;
        this.productManufacturer = productManufacturer;
        this.shelfLifeDays = 0;
    }


    public String getProductID() {
        return productID;
    }
    public String getProductName() {
        return productName;
    }

    public String getProductManufacturer() {
        return productManufacturer;
    }

    public ProductDTO ggetProductDTO(){
        return new ProductDTO(productID, productName, productManufacturer, shelfLifeDays);
    }

    public String toString(){
        return "Product ID: " + productID + ", Product Name: " + productName + ", Product Manufacturer: " + productManufacturer;
    }
}
