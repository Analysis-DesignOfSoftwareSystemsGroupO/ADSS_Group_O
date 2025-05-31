
package SupplierMoudleSource.Domain;

import SupplierMoudleSource.DTO.DeliveryDTO;

public  class Delivery {
    private String deliveryWay;


    //empty case used for diffrent methods of delivery
    String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};


    public Delivery(String deliveryWay) {
        if (deliveryWay == null) {
            throw new IllegalArgumentException("deliveryWay is not valid");
        }
        if(!(deliveryWay.equals("Constant Delivery") || deliveryWay.equals("Temporary Delivery") || deliveryWay.equals("Self Pick Up"))) {
            throw new IllegalArgumentException("deliveryWay is not valid");
        }
     this.deliveryWay = deliveryWay;
    }
    public Delivery(DeliveryDTO deliveryDTO) {
        this.deliveryWay = deliveryDTO.getDeliveryWay();

    }
    public String getDeliveryWay() {
        return deliveryWay;
    }

    private boolean validDay(String day) {
        for (String dayOfWeek : daysOfWeek) {
            if (dayOfWeek.equals(day)) {
                return true;
            }
        }
        return false;
    }
    public DeliveryDTO getDeliveryDTO() {
        return new DeliveryDTO(deliveryWay);
    }

}
