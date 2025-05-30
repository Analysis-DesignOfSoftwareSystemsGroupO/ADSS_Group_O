package Transport_Module_Exceptions;

public class TransportAlreadySentException extends ATransportModuleException{
    public TransportAlreadySentException(){
        super("Transport has already sent");
    }
}
