package Presentation;
import DTO.TruckDto;
import Transport_Module_Exceptions.InvalidInputException;
import transport_module.TruckControllerDomain;

import java.time.LocalDate;

public class TruckControllerPL {

    public TruckControllerPL() {
    }

    public void addTruck(String plate, int maxWeight, String licenceCode) throws Exception{

        TruckDto truckDto = new TruckDto(maxWeight,licenceCode,plate);
        TruckControllerDomain.addTruck(truckDto);
    }
    public TruckDto[] getAllTrucks() throws Exception{

        return TruckControllerDomain.getAllTrucks();
    }



    public void deleteTruck(String plate) throws Exception{
        if(plate.isEmpty())
            throw new InvalidInputException("plate is Empty String");
        TruckControllerDomain.deleteTruck(plate);

    }

     // Attach truck - aopplu attach truck in domain
    public void attachTruck(String transportId, String truckPlt) throws Exception{

        TruckControllerDomain.assignTruckToTransport(Integer.parseInt( transportId),Integer.parseInt(truckPlt));
    }


}
