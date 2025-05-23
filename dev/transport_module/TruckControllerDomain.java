package transport_module;


import DataAccess.TruckDto;

import Transport_Module_Exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TruckControllerDomain {

    private static ITruckRepository truckRepository= new TruckRepositoryIMP();

    public TruckControllerDomain(){

    }
    /** A function that creates truck instance from Truck DTO*/
    public static Truck getTruckFromDto(TruckDto truckDto) throws ATransportModuleException{
        // send DTO to TruckRepositoryIMP

        if( truckDto == null)
            throw new InvalidInputException();

        // Get all information from DTO
        int weight = truckDto.getWeight();
        int maxWeight = truckDto.getMaxWeight();
        String liceenceReq = truckDto.getLiceenceReq();
        String plateNumber = truckDto.getPlateNumber();

        return truckRepository.getTruckBYPlateNumber(plateNumber);

    }
    public static void addTruck(TruckDto truckDto){
        // send DTO to TruckRepositoryIMP

    }

    /** A function that creates DTO from truck*/
    public static TruckDto makeDtoFromTruck(Truck truck) throws ATransportModuleException{
        if (truck == null )
            throw new InvalidInputException();
        // Create and return a DTO with trucks arguments
        return new TruckDto(truck.getMaxWeight(),truck.getDrivingLicence().getCode(), truck.getPlateNumber());
    }
}
