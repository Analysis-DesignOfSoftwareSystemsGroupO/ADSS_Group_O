package transport_module;

import DTO.TruckDto;
import Transport_Module_Exceptions.ATransportModuleException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface ITruckRepository {

    void addTruck(TruckDto truck) throws ATransportModuleException, SQLException;

    Truck getTruckBYPlateNumber(int pn) throws  ATransportModuleException;

    void deleteTruck(String pn) throws  SQLException;

    Truck DTOtoTruck(TruckDto dto) throws ATransportModuleException, SQLException;

    TruckDto truckToDTO(Truck truck);

    void AssignDateToTruck(LocalDate date , String pn) throws ATransportModuleException, SQLException;






}
