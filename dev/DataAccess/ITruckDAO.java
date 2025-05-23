package DataAccess;

import DTO.TruckDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ITruckDAO {

    TruckDto save(TruckDto Truckdto) throws SQLException;
    Optional<TruckDto> findByTruckPN(String plateNumber) throws SQLException;
    List<TruckDto> findAllTrucks() throws SQLException;
}
