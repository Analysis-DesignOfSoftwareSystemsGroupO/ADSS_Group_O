package Transport_Module_Exceptions;


public class InvalidUserException extends ATransportModuleException {
    public InvalidUserException(String message){
        super(message);
    }
}

