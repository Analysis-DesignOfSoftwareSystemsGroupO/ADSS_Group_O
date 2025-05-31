package DataAccess;

import DTO.TruckDto;
import Transport_Module_Exceptions.UnAvailableTruckException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ITruckDAO {

    void save(TruckDto Truckdto) throws SQLException;
    Optional<TruckDto> findByTruckPN(String pn) throws SQLException;
    List<TruckDto> findAllTrucks() throws SQLException;
    void deleteTruck(String pn) throws SQLException;
    boolean checkAvailabilityOfTruck(String truckPN, LocalDate date)throws SQLException;
    void assignTruckToDate(String truckPN, LocalDate date)throws SQLException, UnAvailableTruckException;
    List<LocalDate> getListofOccupiedDates(String truckPn) throws SQLException;
}
