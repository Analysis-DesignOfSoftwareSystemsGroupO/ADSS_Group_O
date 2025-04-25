package Domain;


import org.junit.jupiter.api.Test;
import SupplierMoudleSource.Domain.PaymentMethod;
import static org.junit.jupiter.api.Assertions.*;

class PaymentMethodTest {
    private PaymentMethod paymentMethod;

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
}