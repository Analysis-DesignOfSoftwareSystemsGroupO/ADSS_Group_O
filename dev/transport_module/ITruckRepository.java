package transport_module;

import DataAccess.TruckDto;
import Transport_Module_Exceptions.ATransportModuleException;

public interface ITruckRepository {

    void addTruck(TruckDto truckDto)throws ATransportModuleException;

    Truck getTruckBYPlateNumber(String pn) throws  ATransportModuleException;

    void deleteTruck(String pn) throws ATransportModuleException;

    TruckDto[] getAllTrucks() throws Exception;

    TruckDto[] getAllAvailableTrucks ( String date) throws Exception;

}
