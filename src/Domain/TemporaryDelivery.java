package Domain;

public class TemporaryDelivery extends Delivery {
    private String deliveryDays;
    public String getNextDeliveryDate() {
        return deliveryDays;
    }
}
