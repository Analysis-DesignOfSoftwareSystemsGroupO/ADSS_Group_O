package DTO;

import java.time.LocalTime;

public class ProductListDocumentDto {

    //maybe put id
    //private id
    private int id;
    private String siteDes;
    private String[] products;
    private int[] quantity;
    private LocalTime approximatedArrivalTime ;
    private int transportID;
    private int weight;

    public ProductListDocumentDto(int id, int transportID, String siteDes, String[] products, int[] quantity , int weight, LocalTime time ) {
        this.siteDes = siteDes;
        this.products = products;
        this.quantity = quantity;
        this.id = id;
        this.approximatedArrivalTime = time;
        this.transportID = transportID;
        this.weight = weight;
    }

    // Getters
    public int getTransportID(){return  transportID;}
    public int getId() {
        return id;
    }
    public int getWeight(){return this.weight;}

    public String getSiteDes() {
        return siteDes;
    }

    public String[] getProducts() {
        return products;
    }

    public int[] getQuantity() {
        return quantity;
    }


    // Setters
    public void setSiteDes(String siteDes) {
        this.siteDes = siteDes;
    }

    public void setProducts(String[] products) {
        this.products = products;
    }

    public void setQuantity(int[] quantity) {
        this.quantity = quantity;
    }

    public void setApproximatedArrivalTime(LocalTime time ){
        this.approximatedArrivalTime = time;
    }

    public LocalTime getApproximatedArrivalTime(){
        return this.approximatedArrivalTime;
    }

}

