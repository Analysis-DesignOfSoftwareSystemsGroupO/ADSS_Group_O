package DataAccess;

import DTO.TransportDTO;

import java.sql.SQLException;
import java.util.List;

public interface ITransportDAO {

    TransportDTO getTransportByid(int id ) throws SQLException;
    List<TransportDTO> getTransports() throws SQLException;
    TransportDTO save(TransportDTO transportDTO) throws SQLException;
    void deleteTransport(int id ) throws  SQLException;

}
