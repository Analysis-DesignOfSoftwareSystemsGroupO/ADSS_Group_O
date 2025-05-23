package DataAccess;

import DTO.TruckDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ITruckDAO {

    void save(TruckDto Truckdto) throws SQLException;
    Optional<TruckDto> findByTruckPN(String pn) throws SQLException;
    List<TruckDto> findAllTrucks() throws SQLException;
    void deleteTruck(String pn);
}
