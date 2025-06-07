package TransportModule.Presentation;

import TransportModule.DTO.ProductListDocumentDto;
import TransportModule.DTO.TransportDTO;
import TransportModule.transport_module.TransportContorollerDomain;

import java.util.List;

public class TransportManagerControllerPL {

    private final TransportContorollerDomain domainController;

    public TransportManagerControllerPL() throws Exception{
        this.domainController = new TransportContorollerDomain();
    }

    public TransportManagerControllerPL(TransportContorollerDomain domainController) { // For test section
        this.domainController = domainController;
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

    private void getPLDInfo(int transportId)throws Exception{

        System.out.println("To: \t");
        List<ProductListDocumentDto> DTOS =  getAllPLDSByTransportId(transportId);
        int sum = 0;
        for (ProductListDocumentDto dto: DTOS){
            sum+= dto.getWeight();
            System.out.println("\t"+dto.getSiteDes()+" - Approximated arrival time at: "+dto.getApproximatedArrivalTime()+" weight:"+dto.getWeight()+"\t");
        }
        System.out.println("\tTotal Weight of products: " +  sum + "");

    }


    public void printTransportDTO(TransportDTO dto){
        try{
            System.out.println("Transport number: "+dto.getId()+"\t");
            System.out.println("From: "+dto.getSiteName()+"\t");
            System.out.println("At date: "+dto.getDate()+"\t");
            System.out.println("Leaves at: "+dto.getDepartureTime()+"\t");
            getPLDInfo(dto.getId());
            System.out.println("Max weight: "+dto.getMaxWeight()+"\t");
            System.out.print("Driver: ");
            if(dto.getDriverID()== null)
                System.out.println(" No driver assigned to Transport");
            else
                System.out.println("id number - " +dto.getDriverID()+"\t" );
            System.out.print("Truck: ");
            if(dto.getTruckPN() ==null)
                System.out.println(" No Truck assigned to Transport"+"\t" );
            else
                System.out.println("Truck's Plate number - "+dto.getTruckPN()+"\t");
            if(!dto.isSent())
                System.out.println("wait to be sent.");
            else {
                System.out.println("already left.");
            }


        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void PrintTransportDTOList(List<TransportDTO> list) {
        if (list.isEmpty()){
            System.out.println("No transports for next week");
        }
        for (TransportDTO dto : list) {
            try {
                printTransportDTO(dto);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

}