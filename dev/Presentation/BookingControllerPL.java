package Presentation;

import DTO.ProductDTO;
import DTO.ProductListDocumentDto;
import DTO.TransportDTO;
import DTO.TransportReqDTO;
import Transport_Module_Exceptions.InvalidInputException;
import transport_module.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookingControllerPL {

    private final TransportContorollerDomain domainController;


    public BookingControllerPL() {
        this.domainController = new TransportContorollerDomain();
    }

    /**
     * Creates a new transport request
     */
    public int createTransport(LocalDate date, LocalTime outtime, String source,String area, int maxWeight) throws Exception {
        if (date == null || outtime == null || source == null || source.isEmpty() ||  maxWeight<0)
            throw new InvalidInputException("Missing input for transport request");
        int transportId = domainController.getNewTransportId();
        // todo - add area name to TransportReqDTO
        TransportReqDTO transportDTO = new TransportReqDTO(transportId,date,maxWeight,source,outtime);
        return domainController.createTransport(transportDTO);
    }

    /**
     * Creates a delivery document and returns its ID
     */
    public int createProductListDocument(String destination, String time, String dateStr, int transportId) throws Exception {
        if (destination == null || destination.isEmpty() || time == null || time.isEmpty() || dateStr == null || dateStr.isEmpty())
            throw new InvalidInputException("Missing input for delivery document");
        int nextPLDId = domainController.getNewPDLId();
        ProductListDocumentDto dto = new ProductListDocumentDto(nextPLDId,transportId,destination,null,0,time);
        return domainController.createProductListDocument(dto);
    }

    /**
     * Adds a product to a specific delivery document
     */
    public void addProductToDocument(String product_name, int weight, int amount,ProductDTO productDTO, int docId) throws Exception {
        if (productDTO == null || docId < 0)
            throw new InvalidInputException("Invalid product or document ID");
        ProductDTO dto = new ProductDTO(product_name,weight,amount);
        domainController.addProductToDocument(productDTO, docId);
    }

    /**
     * Attaches a document to a transport by their IDs
     */
    public void attachProductListDocumentToTransport(int docId, int transportId) throws Exception {
        if (docId < 0 || transportId < 0)
            throw new InvalidInputException("Invalid IDs");

        domainController.attachProductListDocumentToTransport(docId, transportId);
    }
}
