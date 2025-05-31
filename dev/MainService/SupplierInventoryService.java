package MainService;

import SupplierMoudleSource.DTO.ConstantOrderDTO;
import SupplierMoudleSource.Service.AgreementService;
import SupplierMoudleSource.Service.OrderService;

import java.sql.Time;
import java.time.LocalTime;

public class SupplierInventoryService {
    private Time time;
    private OrderService orderService;
    private AgreementService agreementService;

    public SupplierInventoryService() {
        LocalTime localTime = LocalTime.of(12, 0); // 12:00 PM
        time = Time.valueOf(localTime);
        orderService = new OrderService();
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

    public void viewAgreement(String branchID, String supplierID) throws Exception {
        agreementService.viewAgreement(branchID, supplierID);
    }

    public void finishOrder(ConstantOrderDTO constantOrderDTO) throws Exception {
        orderService.closeConstantOrder(constantOrderDTO);
    }

    public void addProductToOrder(ConstantOrderDTO constantOrderDTO, String productID, int quantity) throws Exception {
        orderService.addProductToConstantOrder(constantOrderDTO, productID, quantity);
    }

    public void updateConstantOrder(String branchId) throws Exception {
        //TODO: implementthe logic to update constant order
    }

    public Time getTime(){
        return time;
    }


    public void displayConstantOrder(ConstantOrderDTO constantOrderDTO) throws Exception {
        orderService.displayConstantOrder(constantOrderDTO);
    }
}

