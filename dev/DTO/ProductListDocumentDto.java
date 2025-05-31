package DTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ProductListDocumentDto {

    //maybe put id
    //private id
    private int id;
    private String siteDes;
    private List<ProductDTO> products;
    private LocalTime approximatedArrivalTime ;
    private int transportID;
    private int weight;
    private LocalDate date;

    public ProductListDocumentDto(int id, int transportID, String siteDes, List<ProductDTO> products , int weight, LocalDate date, LocalTime time ) {
        this.siteDes = siteDes;
        this.products = products;
        this.id = id;
        this.approximatedArrivalTime = time;
        this.transportID = transportID;
        this.weight = weight;
        this.date = date;
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

    public List<ProductDTO> getProducts() {
        return products;
    }
    public LocalDate getDate(){return date;}

    // Setters
    public void setSiteDes(String siteDes) {
        this.siteDes = siteDes;
    }



    public void setApproximatedArrivalTime(LocalTime time ){
        this.approximatedArrivalTime = time;
    }

    public LocalTime getApproximatedArrivalTime(){
        return this.approximatedArrivalTime;
    }

}