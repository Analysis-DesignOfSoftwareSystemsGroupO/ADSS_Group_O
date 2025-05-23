package transport_module;

import DataAccess.TruckDto;
import Transport_Module_Exceptions.ATransportModuleException;

public class TruckRepositoryIMP implements  ITruckRepository{


    @Override
    public void addTruck(TruckDto truckDTO) throws ATransportModuleException {

    }

    @Override
    public Truck getTruckBYPlateNumber(String pn) throws ATransportModuleException {
        return null;
    }

    @Override
    public void deleteTruck(String platenumber) throws ATransportModuleException {

    }

    @Override
    public TruckDto[] getAllTrucks() throws Exception{
        return null; // todo - return an array of DTO's of all trucks
    }

    @Override
    public TruckDto[] getAllAvailableTrucks ( String date) throws Exception{
        return null;// todo - return an array of DTO's of all trucks in this date
    }
}
