package transport_module;

import DataAccess.DriverDto;
import DataAccess.TruckDto;
import Transport_Module_Exceptions.ATransportModuleException;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class DomainController {



    public static void addNewTruckFromDto(TruckDto truckDTO)throws ATransportModuleException{
        //todo : check if truck is already in System by plate number field
        Truck truck = new Truck(new DrivingLicence(truckDTO.getLiceenceReq()), truckDTO.getMaxWeight(), truckDTO.getPlateNumber()); //create new Truck
         // Add it to Database

    }

   // public static ProductListDocument createPLDFromDTO(ProductListDocumentDTO dto){}





}
