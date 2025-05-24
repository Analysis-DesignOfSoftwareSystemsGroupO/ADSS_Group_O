package transport_module;

import DTO.TransportDTO;
import Transport_Module_Exceptions.ATransportModuleException;

import java.util.Date;

public interface ITransportRepository {


    Transport getTransportByid(int id) throws ATransportModuleException;

    Transport[] getTransportsByDate(Date date);

    void saveTransport(Transport transport) throws ATransportModuleException ;

    void deleteTransport(int transportID ) throws  ATransportModuleException;

    Transport TransportDTOtoTransport(TransportDTO dto );

    TransportDTO transportToTransportDTO(Transport transport);

}
