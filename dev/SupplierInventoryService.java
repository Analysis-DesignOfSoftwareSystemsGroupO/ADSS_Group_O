import SupplierMoudleSource.Service.OrderService;

import java.sql.Time;
import java.time.LocalTime;

public class SupplierInventoryService {
    private Time time;
    private OrderService orderService;
    public SupplierInventoryService() {
        LocalTime localTime = LocalTime.of(12, 0); // 12:00 PM
        time = Time.valueOf(localTime);
        orderService = new OrderService();
    }

    public void createImmediateOrder(String branchId, String productName, String manufacturer, int quantity) throws Exception {
        orderService.createImmediateOrder(branchId, productName, manufacturer, quantity);
    }

    public void createConstantOrder(String branchId) {
        orderService.createConstantOrder(branchId);
    }

    public void updateConstantOrder(String branchId) throws Exception {
        //TODO: implementthe logic to update constant order
    }

    public Time getTime(){
        return time;
    }
}

