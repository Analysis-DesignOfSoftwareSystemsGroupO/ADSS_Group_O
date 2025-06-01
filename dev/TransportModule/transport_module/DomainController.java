package TransportModule.transport_module;

import DataAccess.TruckDto;
import TransportModule.Transport_Module_Exceptions.ATransportModuleException;

public abstract class DomainController {



    public static void addNewTruckFromDto(TruckDto truckDTO)throws ATransportModuleException{
        //todo : check if truck is already in System by plate number field
        Truck truck = new Truck(new DrivingLicence(truckDTO.getLiceenceReq()), truckDTO.getMaxWeight(), truckDTO.getPlateNumber()); //create new Truck
         // Add it to Database

    }

   // public static ProductListDocument createPLDFromDTO(ProductListDocumentDTO dto){}





}
