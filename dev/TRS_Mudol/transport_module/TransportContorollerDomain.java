package transport_module;

import DataAccess.*;
import DTO.ProductDTO;
import DTO.TransportReqDTO;
import Transport_Module_Exceptions.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class TransportContorollerDomain {

    private final ITransportRepository transportRepo;
    private final IProductListDocumentRepository documentRepo;
    private final IProductRepository productRepo;
    private final ISiteRepository siteRepo;

    public TransportContorollerDomain(ITransportRepository transportRepo,
                                     IProductListDocumentRepository documentRepo,
                                     IProductRepository productRepo,
                                     ISiteRepository siteRepo) {
        this.transportRepo = transportRepo;
        this.documentRepo = documentRepo;
        this.productRepo = productRepo;
        this.siteRepo = siteRepo;
    }

    /**
     * Creates a new Transport using data from DTO and saves it.
     */
    public int createTransport(TransportReqDTO dto) throws ATransportModuleException {
        // Get source site from repository
        // todo - ask for site name and area from user
        // todo - posposed later will be integrated with more models ST site will have more functionality - right now just site name and area name
        Site source = siteRepo.getSiteByName(dto.getSource());
        if (source == null) throw new InvalidInputException("Source site not found");

        // Convert date/time to strings for constructor
        String dateStr = dto.date().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String timeStr = dto.departureTime().toString(); // format: HH:mm

        // Create and persist transport
        // todo - send DTO to repository
        Transport t = new Transport(dateStr, timeStr, source);

        // todo use Repository getTransportID
        return t.getId();
    }

    /**
     * Creates and saves a new delivery document.
     */
    public int createProductListDocument(String destinationName, String time, String dateStr) throws ATransportModuleException {
        Site destination = siteRepo.getSiteByName(destinationName);
        if (destination == null) throw new InvalidInputException("Destination site not found");

        ProductListDocument doc = new ProductListDocument(destination, dateStr, time);
        documentRepo.saveDocument(doc);
        return doc.getId();
    }

    /**
     * Adds a product to a delivery document.
     */
    public void addProductToDocument(ProductDTO dto, int docId) throws ATransportModuleException {
        ProductListDocument doc = documentRepo.getDocumentById(docId);
        if (doc == null) throw new InvalidInputException("Document not found");

        Product p = productRepo.getProductById(dto.getProductId());
        if (p == null) throw new InvalidInputException("Product not found");

        doc.addProduct(p, dto.getAmount());
        documentRepo.updateDocument(doc);
    }

    /**
     * Attaches a document to a transport.
     */
    public void attachProductListDocumentToTransport(int docId, int transportId) throws ATransportModuleException {
        ProductListDocument doc = documentRepo.getDocumentById(docId);
        if (doc == null) throw new InvalidInputException("Document not found");

        Transport transport = transportRepo.getTransportByid(transportId);
        if (transport == null) throw new InvalidInputException("Transport not found");

        transport.loadByDocument(doc);
        transportRepo.updateTransport(transport);
    }

    public void fetchAvailableDriversFromHR() throws Exception {

        HRController.requestAvailableDrivers();
    }

    public void assignDriver(String driverId, int transportId) throws Exception {
        Driver driver = DriverRepository.getDriverById(driverId);
        Transport transport = TransportRepository.getTransportByid(transportId);

        transport.addDriver(driver);
        TransportRepository.saveTransport(transport);
    }
}
