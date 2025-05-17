package Transport_Module_Exceptions;

public class InvalidDocumentException extends ATransportModuleException {

    public InvalidDocumentException(){
        super("Invalid Document");
    }
    public InvalidDocumentException(String message){
        super(message);
    }
}
