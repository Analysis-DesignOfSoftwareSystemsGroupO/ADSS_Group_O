package DataAccess;

import DTO.TransportDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ITransportDAO {

    Optional<TransportDTO> getTransportByid(int id ) throws SQLException;
    List<TransportDTO> getTransports() throws SQLException;
    void save(TransportDTO transportDTO) throws SQLException;
    void deleteTransport(int id ) throws  SQLException;
    int getHieghestTransportID() throws  SQLException;
}
