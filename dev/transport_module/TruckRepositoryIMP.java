package transport_module;

import DTO.TruckDto;
import Transport_Module_Exceptions.ATransportModuleException;

import java.util.HashMap;

public class TruckRepositoryIMP implements  ITruckRepository{


    @Override
    public void addTruck(Truck truck) throws ATransportModuleException {

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

    HashMap<String plateNumber, Truck>
}
