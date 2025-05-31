package SupplierMoudleSource;


import SupplierMoudleSource.Service.OrderService;

public class testMain {
    public static void main(String[] args) {
        OrderService orderService = new OrderService();
        try {
            orderService.createRequirementToConstantOrder("3", "2", "Monday");

            int price = 1;
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
