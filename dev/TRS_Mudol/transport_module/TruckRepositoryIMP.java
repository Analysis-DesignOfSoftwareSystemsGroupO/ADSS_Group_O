package transport_module;

import DTO.TruckDto;
import Transport_Module_Exceptions.ATransportModuleException;

import java.util.HashMap;

public class TruckRepositoryIMP implements  ITruckRepository{


    private HashMap<String , Truck> trucksHashMap ;


    @Override
    public void addTruck(Truck truck) throws ATransportModuleException {
        //Adding track to data base
        // if succesed :Add Truck to truckHashMap
    }

    @Override
    public Truck getTruckBYPlateNumber(int pn) throws ATransportModuleException {
        return null;
    }

    @Override
    public void deleteTruck(String pn ) throws ATransportModuleException {

    }

    @Override
    public Truck DTOtoTruck(TruckDto dto) {
        return null;
    }

    @Override
    public TruckDto truckToDTO(Truck truck) {
        return null;
    }

}
