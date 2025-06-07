package TransportModule.DataAccess;

import TransportModule.DTO.DriverDto;

import java.sql.SQLException;
import java.util.List;

public interface IDriverDAO {

    DriverDto getDriverByID(String id) throws SQLException;
    void deleteDriver(String id)throws SQLException;
    void save(DriverDto dto) throws SQLException;
    List<String> getAllDriversID()throws SQLException;
    void addLicenceToDriver(String id, String Licence) throws SQLException;
}