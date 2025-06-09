package TransportModule.DataAccess;

import TransportModule.DTO.TransportDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ITransportDAO {

    Optional<TransportDTO> getTransportByid(int id ) throws SQLException;
    List<TransportDTO> getTransports() throws SQLException;
    void save(TransportDTO transportDTO) throws SQLException;
    void deleteTransport(int id ) throws  SQLException;
    int getHieghestTransportID() throws  SQLException;
    List<TransportDTO> getTransportsWithoutTruck()throws SQLException;
    void assignTruckToTransport(int transportID, String truckPN) throws SQLException;
    List<TransportDTO> getTransportsByDate(LocalDate date) throws SQLException;
    void deleteAll() throws SQLException;
    List<Integer > getIDs() throws SQLException;
    void setSent(int id) throws SQLException;
    void setDriver(int Tid, String driverID) throws SQLException;
    void setMaxWeight(int tID , int w)throws SQLException;
}
