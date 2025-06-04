package TransportModule.Presentation;

import TransportModule.DTO.ProductDTO;
import TransportModule.DTO.ProductListDocumentDto;
import TransportModule.DTO.TransportDTO;
import TransportModule.transport_module.TransportContorollerDomain;
import transport_module.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class BookingControllerPL {

    private final TransportContorollerDomain domainController;


    public BookingControllerPL()throws Exception {
        this.domainController = new TransportContorollerDomain();
    }

    /**
     * Creates a new transport request
     */
    public int createTransport(LocalDate date, String source, int maxWeight,LocalTime hour) throws Exception {

        int transportId = domainController.getNewTransportId();

        TransportDTO transportDTO = new TransportDTO(transportId,date,false,maxWeight,"-1","-1",source,hour);

        domainController.createTransport(transportDTO);
        return  transportId;
    }

    /**
     * Creates a delivery document and returns its ID
     */
    public int createProductListDocument(String destination, List<ProductDTO> productDTOList, int totalweight, LocalDate date, LocalTime hour) throws Exception {

        int nextPLDId = domainController.getValidID(); // get the next valid input of PLD
        ProductListDocumentDto dto = new ProductListDocumentDto(nextPLDId,-1,destination,productDTOList,totalweight,date,hour);
        domainController.createProductListDocument(dto);
        return nextPLDId;
    }



    /**
     * Attaches a document to a transport by their IDs
     */
    public void attachProductListDocumentsToTransport(List<Integer> PLDIdList, int transportId) throws Exception {

        domainController.attachProductListDocumentsToTransport(PLDIdList, transportId);
    }

    public List<TransportDTO> getWeeklyTransportsRequests() throws Exception{

        return domainController.getTransportNextWeek();

    }
}
