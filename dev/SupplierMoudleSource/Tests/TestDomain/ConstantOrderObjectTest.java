package SupplierMoudleSource.Tests.TestDomain;

import SupplierMoudleSource.Domain.Agreement;
import SupplierMoudleSource.Domain.ConstantOrder;
import SupplierMoudleSource.Domain.Product;
import SupplierMoudleSource.Domain.SuppliedItem;
import SupplierMoudleSource.DTO.*;
import SupplierMoudleSource.Repository.AgreementRepository;
import SupplierMoudleSource.Repository.BranchesRepository;
import SupplierMoudleSource.Repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ConstantOrderObjectTest {

    private AgreementDTO agreementDTO;
    private ConstantOrder constantOrder;

    @BeforeEach
    void setup() throws Exception {
        // Make sure these exist or are mocked appropriately
        int branchId = 1;
        int supplierId = 2;

        agreementDTO = AgreementRepository.getInstance().getAgreement(Integer.toString(branchId), Integer.toString(supplierId));
        assertNotNull(agreementDTO, "AgreementDTO should not be null");

        constantOrder = new ConstantOrder(
                new Agreement(
                        BranchesRepository.getInstance().getBranch(Integer.toString(branchId)),
                        SupplierRepository.getInstance().getSupplier(Integer.toString(supplierId)),
                        agreementDTO
                ),
                "Monday"
        );
    }

    @Test
    void testAddValidItemToOrder() throws Exception {
        SuppliedItem item = constantOrder.getAgreement().getSupplierItemsList().get(0);
        String itemId = item.getSuppliedItemID();
        int quantity = 5;

        constantOrder.addItemToOrder(itemId, quantity);
        assertEquals(quantity * item.getSuppliedItemPrice(), constantOrder.getTotalPrice());
    }

    @Test
    void testAddInvalidItemToOrderThrows() {
        Exception exception = assertThrows(Exception.class, () -> {
            constantOrder.addItemToOrder("invalid-id", 5);
        });
        assertTrue(exception.getMessage().contains("doesnt exist in the agreement"));
    }

    @Test
    void testToDTOAndBack() throws Exception {
        SuppliedItem item = constantOrder.getAgreement().getSupplierItemsList().get(0);
        constantOrder.addItemToOrder(item.getSuppliedItemID(), 2);

        ConstantOrderDTO dto = constantOrder.getConstantOrderDTO();
        ConstantOrder reconstructed = new ConstantOrder(dto);

        assertEquals(constantOrder.getTotalPrice(), reconstructed.getTotalPrice());
        assertEquals(dto.getDayOfWeek(), reconstructed.getConstantOrderDTO().getDayOfWeek());
    }
}