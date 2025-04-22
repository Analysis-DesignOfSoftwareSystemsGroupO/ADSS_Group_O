package Domain;

import java.util.Objects;

public class PaymentMethod {
    private String paymentMethod;

    public PaymentMethod(String paymentMethod) {
        if (paymentMethod == null) {
            throw new NullPointerException("paymentMethod is null");
        }
        if (Objects.equals(paymentMethod, "CreditCard") || Objects.equals(paymentMethod, "Cash") | Objects.equals(paymentMethod, "Bank Transfer") || Objects.equals(paymentMethod, "Check")) {
            this.paymentMethod = paymentMethod;
        }
        else {
            throw new IllegalArgumentException("paymentMethod is not valid");
        }
    }

    public String getPaymentMethodName() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
