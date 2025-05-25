package DTO;

//Product DTO object to describe the product weight and product quantity in a product list Document
public record ProductDTO (
        String serialNumber,
        int weight, // weight per unit of product
        int quantity
){ }
