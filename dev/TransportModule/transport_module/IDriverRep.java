package TransportModule.transport_module;

import TransportModule.DTO.DriverDto;
import TransportModule.Transport_Module_Exceptions.ATransportModuleException;

import java.sql.SQLException;
import java.util.List;

public interface IDriverRep {

    List<Driver> getAllDrivers() throws SQLException;
    void save(DriverDto driverDto)throws SQLException, ATransportModuleException;
    void deleteDriver(String Driverid)throws SQLException;
    Driver getDriverByID(String id )throws SQLException;
    Driver convertDTOtoDriver(DriverDto dto) throws SQLException, ATransportModuleException;
}
