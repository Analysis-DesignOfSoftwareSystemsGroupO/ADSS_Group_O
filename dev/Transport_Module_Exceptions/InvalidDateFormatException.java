package Transport_Module_Exceptions;

public class InvalidDateFormatException extends ATransportModuleException {
    public InvalidDateFormatException(){
        super("Date format must be DD/MM/YYYY");
    }
}
