package TransportModule.Presentation;
import TransportModule.DTO.TruckDto;
import TransportModule.Transport_Module_Exceptions.InvalidInputException;
import TransportModule.transport_module.TruckControllerDomain;

import java.util.List;

public class TruckControllerPL {
    private final TruckControllerDomain truckControllerDomain;

    public TruckControllerPL() throws Exception{
        truckControllerDomain = new TruckControllerDomain();
    }

    public TruckControllerPL(TruckControllerDomain truckControllerDomain) { // For test section
        this.truckControllerDomain = truckControllerDomain;
    }

    public void addTruck(String plate, int maxWeight, String licenceCode) throws Exception{

        TruckDto truckDto = new TruckDto(maxWeight,licenceCode,plate);
        truckControllerDomain.addTruck(truckDto);
    }
    public List<TruckDto> getAllTrucks() throws Exception{

        return truckControllerDomain.getAllTrucks();
    }



    public void deleteTruck(String plate) throws Exception{
        if(plate.isEmpty())
            throw new InvalidInputException("plate is Empty String");
        truckControllerDomain.deleteTruck(plate);

    }

    // Attach truck - aopplu attach truck in domain
    public void attachTruck(String transportId, String truckPlt) throws Exception{

        truckControllerDomain.assignTruckToTransport(Integer.parseInt( transportId),Integer.parseInt(truckPlt));
    }


}
