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

    private String getPLDInfo(int transportId)throws Exception{

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("To: ").append("\n").append("\t");
        List<ProductListDocumentDto> DTOS =  getAllPLDSByTransportId(transportId);
        int sum = 0;
        for (ProductListDocumentDto dto: DTOS){
            sum+= dto.getWeight();
            stringBuilder.append("\t").append(dto.getSiteDes()).append(" - Approximated arrival time at: ").append(dto.getApproximatedArrivalTime()).append(" weight:" ).append(dto.getWeight()).append("\n").append("\t");
        }
        stringBuilder.append("\tTotal Weight of products: " +  sum + "\n\t");
        return stringBuilder.toString();

    }

    public String printTransportDTO(TransportDTO dto){
        StringBuilder stringBuilder = new StringBuilder();
        try{
            stringBuilder.append("Transport number: ").append(dto.getId()).append("\n").append("\t");
            stringBuilder.append("From: ").append(dto.getSiteName()).append("\n").append("\t");
            stringBuilder.append("At date: ").append(dto.getDate()).append("\n").append("\t");
            stringBuilder.append("Leaves at: ").append(dto.getDepartureTime()).append("\n").append("\t");
            stringBuilder.append(getPLDInfo(dto.getId()));
            stringBuilder.append("Max weight: ").append(dto.getMaxWeight()).append("\n").append("\t");
            stringBuilder.append("Driver: ");
            if(dto.getDriverID()== null)
                stringBuilder.append(" No driver assigned to Transport");
            else
                stringBuilder.append("id number - ").append(dto.getDriverID());
            stringBuilder.append("\n\t");
            stringBuilder.append("Truck: ");
            if(dto.getTruckPN() ==null)
                stringBuilder.append(" No Truck assigned to Transport");
            else
                stringBuilder.append("Truck's Plate number - ").append(dto.getTruckPN());
            stringBuilder.append("\n\t");
            if(!dto.isSent())
                stringBuilder.append("wait to be sent.\n");
            else
                stringBuilder.append("already left.\n");


        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return stringBuilder.toString();
    }

    public void PrintTransportDTOList(List<TransportDTO> list) {
        if (list.isEmpty()){
            System.out.println("No transports for next week");
        }
        for (TransportDTO dto : list) {
            try {
                System.out.println(printTransportDTO(dto));
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

}