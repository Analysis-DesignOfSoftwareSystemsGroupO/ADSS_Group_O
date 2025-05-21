package transport_module;

import Transport_Module_Exceptions.ATransportModuleException;

public interface ITruckRepository {

    void addTruck(Truck truck)throws ATransportModuleException;

    Truck getTruckBYPlateNumber(int pn) throws  ATransportModuleException;

    void deleteTruck(Truck truck) throws ATransportModuleException;


}
