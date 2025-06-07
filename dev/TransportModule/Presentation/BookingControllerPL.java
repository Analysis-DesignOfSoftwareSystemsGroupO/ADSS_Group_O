package TransportModule.Presentation;

import TransportModule.DTO.ProductDTO;
import TransportModule.DTO.ProductListDocumentDto;
import TransportModule.DTO.TransportDTO;
import TransportModule.transport_module.TransportContorollerDomain;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class BookingControllerPL {

    private final TransportContorollerDomain domainController;


    public BookingControllerPL()throws Exception {
        this.domainController = new TransportContorollerDomain();
    }

    public BookingControllerPL(TransportContorollerDomain domainController) { // For test section
        this.domainController = domainController;
    }

    /**
     * Creates a new transport request
     */
    public void createTransport(int transportId, LocalDate date, String source, int maxWeight, LocalTime hour) throws Exception {

        TransportDTO transportDTO = new TransportDTO(transportId,date,false,maxWeight,null,null,source,hour);

        domainController.createTransport(transportDTO);
    }

    /**
     * Creates a delivery document and returns its ID
     */
    public int createProductListDocument(int transportId, String destination, List<ProductDTO> productDTOList, int totalweight, LocalDate date, LocalTime hour) throws Exception {

        int nextPLDId = domainController.getValidID(); // get the next valid input of PLD
        ProductListDocumentDto dto = new ProductListDocumentDto(nextPLDId,transportId,destination,productDTOList,totalweight,date,hour);
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

    public int getNewTransportId() throws Exception{
        return domainController.getNewTransportId();
    }
}