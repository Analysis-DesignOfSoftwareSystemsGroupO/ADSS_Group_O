package SupplierMoudleSource.Tests.TestDomain;


import SupplierMoudleSource.DTO.PaymentMethodDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import SupplierMoudleSource.Domain.PaymentMethod;
import static org.junit.jupiter.api.Assertions.*;

class PaymentMethodTest {
    private PaymentMethod paymentMethod;

    @BeforeEach
    void setUp() {
        paymentMethod = new PaymentMethod("Cash");
    }

    @Test
    void testConstructor() {
        assertThrows(Exception.class, () -> new PaymentMethod(null), "didnt throw when argument was null");
        assertThrows(Exception.class, () -> new PaymentMethod("inValid"), "didnt throw when argument was not valid");
        assertDoesNotThrow(() -> new PaymentMethod("Cash"), "threw when argument was valid");
    }


    @Test
    void getPaymentMethodName() {
        PaymentMethod paymentMethod = new PaymentMethod("Cash");
        assertEquals("Cash", paymentMethod.getPaymentMethodName());
    }

    @Test
    void getDTO() {
        PaymentMethodDTO paymentMethodDTO = paymentMethod.getPaymentMethodDTO();
        assertEquals(paymentMethodDTO.getPaymentMethod(), paymentMethod.getPaymentMethodName());
    }
}