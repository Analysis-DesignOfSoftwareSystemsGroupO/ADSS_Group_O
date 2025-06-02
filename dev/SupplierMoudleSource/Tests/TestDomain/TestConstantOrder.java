package SupplierMoudleSource.Tests.TestDomain;

import SupplierMoudleSource.Service.OrderService;
import org.junit.jupiter.api.BeforeAll;

public class TestConstantOrder {
    private static OrderService orderService;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        orderService = new OrderService();

    }
}
