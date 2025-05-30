package Transport_Module_Exceptions;

public class InvalidProductException extends ATransportModuleException {
    public InvalidProductException(String message) {
        super(message);
    }
}