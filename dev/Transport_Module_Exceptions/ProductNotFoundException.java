package Transport_Module_Exceptions;

public class ProductNotFoundException extends ATransportModuleException {
    public ProductNotFoundException() {
        super("Product is not found in document");
    }
}