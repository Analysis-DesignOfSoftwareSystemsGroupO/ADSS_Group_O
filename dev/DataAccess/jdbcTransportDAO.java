package DataAccess;

import DTO.TransportDTO;

import java.sql.SQLException;
import java.util.List;

public class jdbcTransportDAO implements ITransportDAO {
    @Override
    public TransportDTO getTransportByid(int id) throws SQLException {
        return null;
    }

    @Override
    public List<TransportDTO> getTransports() throws SQLException {
        return null;
    }

    @Override
    public TransportDTO save(TransportDTO transportDTO) throws SQLException {
        return null;
    }

    @Override
    public void deleteTransport(int id) throws SQLException {

    }
}
