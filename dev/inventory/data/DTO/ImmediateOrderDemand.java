package inventory.data.DTO;

public class ImmediateOrderDemand {
    private final String name;
    private final String manufacturer;
    private final int amountToOrder;



    public ImmediateOrderDemand(String name, String manufacturer, int amountToOrder){
        this.name = name;
        this.manufacturer = manufacturer;
        this.amountToOrder = amountToOrder;
    }

    // Getters
    public String getProductName() { return name; }
    public String getManufacturer() { return manufacturer; }
    public int getAmountToOrder() { return amountToOrder; }

    @Override
    public String toString() {
        return name + " (" + manufacturer + "): " + amountToOrder;
    }
}
