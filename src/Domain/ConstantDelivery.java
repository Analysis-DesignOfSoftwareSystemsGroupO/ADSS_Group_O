package Domain;

public class ConstantDelivery extends Delivery {
    private String deliveryDays;

    public String getNextDeliveryDate() {
        return deliveryDays;
    }
}
