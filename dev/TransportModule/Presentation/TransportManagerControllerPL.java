package TransportModule.Presentation;

import TransportModule.DTO.ProductListDocumentDto;
import TransportModule.DTO.TransportDTO;
import TransportModule.DTO.TruckDto;
import TransportModule.transport_module.TransportContorollerDomain;
import TransportModule.transport_module.TruckControllerDomain;

import java.util.ArrayList;
import java.util.List;

public class TransportManagerControllerPL {

    private final TransportContorollerDomain domainController;
    private final TruckControllerDomain TruckController;

    public TransportManagerControllerPL() throws Exception{
        this.domainController = new TransportContorollerDomain();
        this.TruckController = new TruckControllerDomain();
    }



    public List<TransportDTO> getNextWeekTransports() throws Exception{
        return domainController.getTransportNextWeek();
    }

    public List<TransportDTO> getNextWeekTransportsWithNoTrucks() throws Exception{
        return domainController.getNextWeekTransportsWithNoTrucks();
    }

    public List<TransportDTO> getNextWeekTransportsWithNoDrivers() throws Exception{
        return domainController.getNextWeekTransportsWithNoDrivers();
    }

    public void removeTransportById(String transportId) throws Exception{
        domainController.removeTransportById(transportId);
    }
    public List<ProductListDocumentDto> getAllPLDSByTransportId(int transportId) throws Exception{
        return domainController.getPLDbyTransportID(Integer.toString(transportId));
    }



    public void printTransportDTO(TransportDTO dto){
        List<ProductListDocumentDto> DTOS = new ArrayList<>();
        String truckWeight = "";
        int sum = 0;
        try {
            if(dto.getTruckPN()!=null){
                TruckDto truckDto = TruckController.getTruckDTOByPlateNumber(dto.getTruckPN());
                truckWeight = String.valueOf(truckDto.getMaxWeight());
            }
            DTOS = getAllPLDSByTransportId(dto.getId());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println("Transport number: "+dto.getId());
        System.out.println("\tFrom: "+dto.getSiteName());
        System.out.println("\tAt date: "+dto.getDate());
        System.out.println("\tLeaves at: "+dto.getDepartureTime());
        System.out.println("\tTo: \t");
        for (ProductListDocumentDto pldDto: DTOS){
            sum+=pldDto.getWeight();
            System.out.println("\t\t"+pldDto.getSiteDes()+": ");
            System.out.println( "\t\t\tApproximated arrival time at: "+pldDto.getApproximatedArrivalTime());
            System.out.println("\t\t\tweight:"+pldDto.getWeight()+"\n");
        }
        System.out.println("\tTotal Weight of products: " +  sum);
        System.out.print("\tDriver: ");
        if(dto.getDriverID()== null)
            System.out.println("*** No driver assigned to Transport ***");
        else
            System.out.println("id number - " +dto.getDriverID() );
        System.out.print("\tTruck: ");
        if(dto.getTruckPN() ==null)
            System.out.println("*** No Truck assigned to Transport ***" );
        else
            System.out.println("Truck's Plate number - "+dto.getTruckPN()+" with maximum weight of: "+truckWeight);
        if(!dto.isSent())
            System.out.println("\twait to be sent.");
        else {
            System.out.println("\talready left.");
        }
        System.out.println("");



    }

    public void PrintTransportDTOList(List<TransportDTO> list) {
        if (list.isEmpty()){
            System.out.println("No transports for next week");
        }
        for (TransportDTO dto : list) {
            try {
                System.out.println("****************************************************************************\n");
                printTransportDTO(dto);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

}