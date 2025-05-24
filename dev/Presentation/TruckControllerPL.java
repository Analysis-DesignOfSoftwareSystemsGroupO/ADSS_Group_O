package Presentation;
import DTO.TruckDto;
import Transport_Module_Exceptions.InvalidInputException;
import transport_module.TruckControllerDomain;

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

    public TruckDto[] getAllAvailableTrucks ( String date) throws Exception{
        if(date.isEmpty())
            throw new InvalidInputException("String is Empty");
        return TruckControllerDomain.getAllAvailableTrucks(date);
    }

    public void deleteTruck(String plate) throws Exception{
        if(plate.isEmpty())
            throw new InvalidInputException("plate is Empty String");
        TruckControllerDomain.deleteTruck(plate);

    }


}
