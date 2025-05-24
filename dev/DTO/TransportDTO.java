package DTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class TransportDTO {
    private int id;
    private LocalDate date;
    private boolean is_sent ;
    private int maxWeight;
    private String driverID;
    private String truckPN;
    private String siteName;
    private LocalTime departureTime ;
    public TransportDTO(int id, LocalDate date, boolean is_sent, int maxWeight, String driverID, String truckPN, String siteName, LocalTime departureTime){
        this.id = id;
        this.date = date;
        this.is_sent = is_sent;
        this.maxWeight = maxWeight;
        this.driverID = driverID;
        this.truckPN = truckPN;
        this.siteName = siteName;
        this.departureTime = departureTime;
    }

    // Getters
    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isSent() {
        return is_sent;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public String getDriverID() {
        return driverID;
    }

    public String getTruckPN() {
        return truckPN;
    }

    public String getSiteName() {
        return siteName;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setIsSent(boolean isSent) {
        this.is_sent = isSent;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public void setTruckPN(String truckPN) {
        this.truckPN = truckPN;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public void setDepartureTime(LocalTime time ){
        this.departureTime = departureTime;
    }
    public LocalTime getDepartureTime(){
        return this.departureTime;
    }

}
