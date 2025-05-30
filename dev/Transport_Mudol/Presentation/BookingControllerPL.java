package Presentation;

import DTO.ProductDTO;
import DTO.TransportReqDTO;
import Transport_Module_Exceptions.InvalidInputException;
import transport_module.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookingControllerPL {

    private final TransportContorollerDomain domainController;


    public BookingControllerPL() {
        // todo Create repositories in domain
        ITransportRepository transportRepo = new TransportRepositoryIMP();
        IProductListDocumentRepository documentRepo = new ProductListDocumentRepositoryIMP();
        IProductRepository productRepo = new ProductRepositoryIMP();
        ISiteRepository siteRepo = new SiteRepositoryIMP();

        this.domainController = new TransportControllerDomain(transportRepo, documentRepo, productRepo, siteRepo);
    }

    /**
     * Creates a new transport request
     */
    public int createTransport(LocalDate date, LocalTime outtime, String source,int maxWeight) throws Exception {
        if (date == null || outtime == null || source == null || source.isEmpty() ||  maxWeight<0)
            throw new InvalidInputException("Missing input for transport request");
        int transportId = domainController.getNewTransportId();
        TransportReqDTO dto = new TransportReqDTO(transportId, date, maxWeight, source, outtime);
        return domainController.createTransport(dto);
    }

    /**
     * Creates a delivery document and returns its ID
     */
    public int createProductListDocument(String destination, String time, String dateStr) throws Exception {
        if (destination == null || destination.isEmpty() || time == null || time.isEmpty() || dateStr == null || dateStr.isEmpty())
            throw new InvalidInputException("Missing input for delivery document");

        return domainController.createProductListDocument(destination, time, dateStr);
    }

    /**
     * Adds a product to a specific delivery document
     */
    public void addProductToDocument(ProductDTO productDTO, int docId) throws Exception {
        if (productDTO == null || docId < 0)
            throw new InvalidInputException("Invalid product or document ID");

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
