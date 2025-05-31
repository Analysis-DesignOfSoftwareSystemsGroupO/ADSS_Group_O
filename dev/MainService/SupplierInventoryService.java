package MainService;
import SupplierMoudleSource.DTO.requirementToConstantOrderDTO;
import SupplierMoudleSource.Service.OrderService;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

public class SupplierInventoryService {
    private Time time;
    private OrderService orderService;
    public SupplierInventoryService(OrderService orderService) {
        LocalTime localTime = LocalTime.of(12, 0); // 12:00 PM
        time = Time.valueOf(localTime);
        this.orderService = orderService;
    }

    public SupplierInventoryService(){
        LocalTime localTime = LocalTime.of(12, 0); // 12:00 PM
        time = Time.valueOf(localTime);
        this.orderService = new OrderService();
    }



    public void createImmediateOrder(String branchId, String productName, String manufacturer, int quantity) throws Exception {
        orderService.createImmediateOrder(branchId, productName, manufacturer, quantity);
    }

//    public void createConstantOrder(String branchId) {
//        orderService.createConstantOrder(branchId);
//    }

    public void updateConstantOrder(String branchId, String supplierId, String productName, String manufacturer, int newQuantity) throws Exception {
        orderService.updateExistingConstantOrder(branchId, supplierId, productName, manufacturer, newQuantity);
    }

    public List<requirementToConstantOrderDTO> getOrdersByDayOfWeek(String dayOfWeek) throws Exception {
        return orderService.getConstantOrdersByDayOfWeek(dayOfWeek);
    }

    public List<requirementToConstantOrderDTO> getOrdersByNameAndManufacturer(String productName, String manufacturer) throws Exception {
        return orderService.getConstantOrdersByProductNameAndManufacturer(productName, manufacturer);
    }

    public Time getTime(){
        return time;
    }
}

