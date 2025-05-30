package Transport_Module_Exceptions;

public class InvalidAmountException extends ATransportModuleException {
    public InvalidAmountException(String message){
        super(message);
    }
}
