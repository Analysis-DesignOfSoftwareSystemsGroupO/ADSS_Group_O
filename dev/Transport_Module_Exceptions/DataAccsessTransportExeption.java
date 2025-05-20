package Transport_Module_Exceptions;

public class DataAccsessTransportExeption extends RuntimeException {
    public DataAccsessTransportExeption(String message) {
        super("Faild while communicate with DataBase ");
    }
}
