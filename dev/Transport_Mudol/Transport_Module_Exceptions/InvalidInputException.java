package Transport_Module_Exceptions;

public class InvalidInputException extends ATransportModuleException {

    public InvalidInputException(){
        super("Invalid input");
    }
    public InvalidInputException(String message){
        super(message);
    }

}
