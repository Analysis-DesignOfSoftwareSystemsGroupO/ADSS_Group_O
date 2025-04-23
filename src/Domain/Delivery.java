package Domain;

public  class Delivery {
    private String deliveryWay;


    public Delivery(String deliveryWay) {
        if (deliveryWay == null) {
            throw new IllegalArgumentException("deliveryWay is not valid");
        }
        if(!(deliveryWay.equals("Constant Delivery") || deliveryWay.equals("Temporary Delivery") || deliveryWay.equals("Self Pick Up"))) {
            throw new IllegalArgumentException("deliveryWay is not valid");
        }
        this.deliveryWay = deliveryWay;
    }
    public String getDeliveryWay() {
        return deliveryWay;
    }
    public void setDeliveryWay(String deliveryWay) {
        if(!(deliveryWay.equals("Constant Delivery") || deliveryWay.equals("Temporary Delivery") || deliveryWay.equals("Self Pick Up"))) {
            throw new IllegalArgumentException("deliveryWay is not valid");
        }
        this.deliveryWay = deliveryWay;
    }
}
