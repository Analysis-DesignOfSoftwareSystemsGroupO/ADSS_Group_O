package DataAccess;

import DTO.TransportDTO;
import DataLayer.DataBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

public class jdbcTransportDAO implements ITransportDAO {
    private static final Logger log = LogManager.getLogger(DataBase.class);

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
