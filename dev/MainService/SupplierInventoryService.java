package MainService;

import SupplierMoudleSource.DTO.ConstantOrderDTO;
import SupplierMoudleSource.Service.AgreementService;
import SupplierMoudleSource.Service.OrderService;
import inventory.data.DTO.ImmediateOrderDemand;
import inventory.service.UserApplication;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.time.DayOfWeek;
import java.time.LocalDate;


public class SupplierInventoryService {
    private Time time;
    private OrderService orderService;
    private AgreementService agreementService;
    private UserApplication inventoryService = new UserApplication("1");
    private ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

    public SupplierInventoryService(OrderService orderService) {
        LocalTime localTime = LocalTime.of(12, 0); // 12:00 PM
        time = Time.valueOf(localTime);
        this.orderService = orderService;
        this.agreementService = new AgreementService();
    }

    public SupplierInventoryService() {
        LocalTime localTime = LocalTime.of(12, 0); // 12:00 PM
        time = Time.valueOf(localTime);
        this.orderService = new OrderService();
        this.agreementService = new AgreementService();
    }

    public void createImmediateOrder(String branchId, String productName, String manufacturer, int quantity) throws Exception {
        orderService.createImmediateOrder(branchId, productName, manufacturer, quantity);
    }

    public void getsPossibleConstantOrdersForBranch(String branchId) throws Exception {
        orderService.getsPossibleConstantOrdersForBranch(branchId);
    }

    public ConstantOrderDTO createRequirementToConstantOrder(String branchID, String supplierID, String day) throws Exception {
        return orderService.createRequirementToConstantOrder(branchID, supplierID, day);
    }

    public void updateConstantOrder() throws Exception {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<ConstantOrderDTO> ordersToUpdate = getOrdersByDayOfWeek(tomorrow.getDayOfWeek().toString());
        for (ConstantOrderDTO order : ordersToUpdate) {
            String supplierId = order.getSupplierID();
            String productName = order.getSuppliedItems().keySet().iterator().next().product.productName;
            String manufacturer = order.getSuppliedItems().keySet().iterator().next().product.productManufacturer;
            int newQuantity = inventoryService.getAmountToOrder(productName, manufacturer);
            orderService.updateExistingConstantOrder(inventoryService.branchId, supplierId, productName, manufacturer, newQuantity);

        }
    }

    public void viewAgreement(String branchID, String supplierID) throws Exception {
        agreementService.viewAgreement(branchID, supplierID);
    }

    public List<ConstantOrderDTO> getOrdersByDayOfWeek(String dayOfWeek) throws Exception {
        return orderService.getConstantOrdersByDayOfWeek(dayOfWeek);
    }

    public void finishOrder(ConstantOrderDTO constantOrderDTO) throws Exception {
        orderService.closeConstantOrder(constantOrderDTO);
    }

    public ConstantOrderDTO addProductToOrder(ConstantOrderDTO constantOrderDTO, String productID, int quantity) throws Exception {
        return orderService.addProductToConstantOrder(constantOrderDTO, productID, quantity);
    }

    public List<ConstantOrderDTO> getOrdersByNameAndManufacturer(String productName, String manufacturer) throws Exception {
        return orderService.getConstantOrdersByProductNameAndManufacturer(productName, manufacturer);
    }
    public Time getTime () {
        return time;
    }
    public void displayConstantOrder(ConstantOrderDTO constantOrderDTO) throws Exception {
        orderService.displayConstantOrder(constantOrderDTO);
    }
    public void deleteConstantOrder(String branchId, String supplierId, String productName, String manufacturer) throws Exception
    {
        orderService.deleteConstantOrder(branchId, supplierId, productName, manufacturer);
    }

    public List<ConstantOrderDTO> getConstantOrdersBySupplierId(String branchId, String supplierId) throws Exception {
        return orderService.getConstantOrdersBySupplierId(branchId, supplierId);
    }

    public void checkAndCreateImmediateOrder() throws Exception {
        List<ImmediateOrderDemand> orderList = inventoryService.checkAndCreateImmediateOrderDemands();

        for (ImmediateOrderDemand immediateOrder : orderList) {
            System.out.println("Immediate Order Demand: " + immediateOrder);
            try {
                createImmediateOrder(inventoryService.branchId, immediateOrder.getProductName(), immediateOrder.getManufacturer(), immediateOrder.getAmountToOrder());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void startScheduledTask(){
        scheduledExecutor.scheduleAtFixedRate(() -> {
            try {
                checkAndCreateImmediateOrder();
//                System.out.println("Checking and creating immediate orders...");
                updateConstantOrder();
//                System.out.println("Updating constant orders...");
                //todo scheduled constant orders
            } catch (Exception e) {
                System.out.println(e);
            }
        }, 0, 24, TimeUnit.HOURS);
    }
}

