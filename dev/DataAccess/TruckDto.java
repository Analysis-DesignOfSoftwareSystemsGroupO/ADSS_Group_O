package DataAccess;

public class TruckDto {
    private int weight;
    private final int maxWeight;
    private final String liceenceReq;
    private final String plateNumber;

    public TruckDto(int maxWeight, String liceenceReq, String plateNumber) {
        this.maxWeight = maxWeight;
        this.liceenceReq = liceenceReq;
        this.plateNumber = plateNumber;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public String getLiceenceReq() {
        return liceenceReq;
    }


    public String getPlateNumber() {
        return plateNumber;
    }



}
