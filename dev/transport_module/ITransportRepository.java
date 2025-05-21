package transport_module;

import DataAccess.ProductListDocumentDto;
import Transport_Module_Exceptions.ATransportModuleException;

import java.util.Date;

public interface ITransportRepository {


    Transport getTransportByid(int id) throws ATransportModuleException;
    Transport getTransportsByDate(Date date);

    void saveTransport(TransportDTO transportDTO) throws ATransportModuleException ;

    void deleteTransport(TransportDTO transportDTODTO ) throws  ATransportModuleException;

}
