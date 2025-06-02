package SupplierMoudleSource.Tests.TestDomain;

import SupplierMoudleSource.DTO.ConstantOrderDTO;
import SupplierMoudleSource.Domain.ConstantOrder;
import SupplierMoudleSource.Service.OrderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestConstantOrder {
    private static OrderService orderService;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        orderService = new OrderService();
    }

    @Test
    void testConstantOrder() throws Exception {
        ConstantOrderDTO constantOrderDTO = orderService.createRequirementToConstantOrder("3","1", "Monday");
        ConstantOrder constantOrder = new ConstantOrder(constantOrderDTO);
    }
}
