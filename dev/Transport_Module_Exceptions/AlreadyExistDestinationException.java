package Transport_Module_Exceptions;

public class AlreadyExistDestinationException extends ATransportModuleException{
    public AlreadyExistDestinationException(){
        super("Destination is already a destination in transport - please edit the write ProductListDocument");
    }
}
