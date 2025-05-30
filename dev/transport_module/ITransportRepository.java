package transport_module;

import DTO.TransportDTO;
import Transport_Module_Exceptions.ATransportModuleException;
import Transport_Module_Exceptions.TransportMismatchException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ITransportRepository {


    Transport getTransportByid(int id) throws SQLException;

    List<Transport> getTransportsByDate(LocalDate date) throws SQLException;

    void saveTransport(TransportDTO transport) throws ATransportModuleException, SQLException;

    void deleteTransport(int transportID ) throws  SQLException;

    Transport TransportDTOtoTransport(TransportDTO dto ) throws SQLException, TransportMismatchException;

    TransportDTO transportToTransportDTO(Transport transport);

    List<TransportDTO> getTransportsDTOByDate(LocalDate date)throws SQLException;

    int getAvailableid();


}
