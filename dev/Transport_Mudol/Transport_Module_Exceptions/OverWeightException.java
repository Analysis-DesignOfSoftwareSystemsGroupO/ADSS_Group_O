package Transport_Module_Exceptions;

public class OverWeightException extends ATransportModuleException{
    public OverWeightException (int difference){
        super("Truck has Over Weight: excess by " + difference + " kg");
    }
}
