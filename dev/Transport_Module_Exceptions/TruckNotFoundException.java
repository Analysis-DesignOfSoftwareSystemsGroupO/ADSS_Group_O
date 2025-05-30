package Transport_Module_Exceptions;

public class TruckNotFoundException extends ATransportModuleException{

    public TruckNotFoundException(String message) {
        super(message);
    }
}
