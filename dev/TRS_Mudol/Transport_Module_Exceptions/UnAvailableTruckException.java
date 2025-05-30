package Transport_Module_Exceptions;

public class UnAvailableTruckException extends ATransportModuleException {
    public UnAvailableTruckException(){
        super("Truck is not available at this date");

    }
}
