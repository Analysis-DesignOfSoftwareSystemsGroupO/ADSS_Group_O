package Transport_Module_Exceptions;

import java.time.LocalDate;

public abstract class TransportModuleException extends Exception{
    public TransportModuleException(String message) {
        super(message);
    }
}

// ********************************************************************************************************************* InvalidDateException
class InvalidDateException extends TransportModuleException{
    public InvalidDateException(LocalDate date){
        super("The date " + date.toString() + " is not valid");
    }
}