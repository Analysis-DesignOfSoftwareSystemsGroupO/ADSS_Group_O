package transport_module;

import DTO.ProductListDocumentDto;
import Transport_Module_Exceptions.ATransportModuleException;

import java.util.Date;

public interface ITransportRepository {


    Transport getTransportByid(int id) throws ATransportModuleException;

    Transport[] getTransportsByDate(Date date);

    void saveTransport(Transport transport) throws ATransportModuleException ;

    void deleteTransport(Transport transport ) throws  ATransportModuleException;

}
