package DTO;

public class TransportDTO {
    private int id;
    private String date;
    private boolean is_sent ;
    private int maxWeight;
    private int driverID;
    private int truckPN;
    private String siteName;

    public TransportDTO(int id, String date, boolean is_sent, int maxWeight, int driverID, int truckPN, String siteName){
        this.id = id;
        this.date = date;
        this.is_sent = is_sent;
        this.maxWeight = maxWeight;
        this.driverID = driverID;
        this.truckPN = truckPN;
        this.siteName = siteName;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public boolean isSent() {
        return is_sent;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public int getDriverID() {
        return driverID;
    }

    public int getTruckPN() {
        return truckPN;
    }

    public String getSiteName() {
        return siteName;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setIsSent(boolean isSent) {
        this.is_sent = isSent;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

    public void setTruckPN(int truckPN) {
        this.truckPN = truckPN;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

}
