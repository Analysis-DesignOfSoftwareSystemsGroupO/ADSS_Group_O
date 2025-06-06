package TransportModule.transport_module;

import TransportModule.DTO.DriverDto;
import TransportModule.DTO.TransportDTO;
import TransportModule.Transport_Module_Exceptions.ATransportModuleException;
import TransportModule.Transport_Module_Exceptions.TransportMismatchException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface ITransportRepository {


    Transport getTransportByid(int id) throws SQLException, ATransportModuleException;

    List<Transport> getTransportsByDate(LocalDate date) throws SQLException, ATransportModuleException;

    void saveTransport(TransportDTO transport) throws ATransportModuleException, SQLException;

    void deleteTransport(int transportID ) throws  SQLException;

    Transport TransportDTOtoTransport(TransportDTO dto ) throws SQLException, ATransportModuleException;

    TransportDTO transportToTransportDTO(Transport transport);

    List<TransportDTO> getTransportsDTOByDate(LocalDate date)throws SQLException;

    void attachTrucktoTransport(int transportId , String pn) throws SQLException, ATransportModuleException;

    int getAvailableid();

    void deleteAll()throws SQLException;

    List<Transport> getAllTransports() throws SQLException, ATransportModuleException;

    void updateTransport(TransportDTO t) throws SQLException, ATransportModuleException;

    void setSent(int transportID ) throws SQLException, ATransportModuleException;

    void setDriver(int transportID, String driverID) throws ATransportModuleException, SQLException;

    void injectPLDRepository(IProductListDocumentRepository rep);
}