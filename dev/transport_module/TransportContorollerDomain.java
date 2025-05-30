package transport_module;

import DTO.ProductListDocumentDto;
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

    public TransportContorollerDomain() {
        this.transportRepo = new TransportRepositoryIMP();
        this.documentRepo = new ProductListDocumentRepositoryIMP();
        this.productRepo = ProductRepositoryIMP();
        this.siteRepo = SiteRepositoryIMP();
    }

    /**
     * Creates a new Transport using data from DTO and saves it.
     */
    public int createTransport(TransportReqDTO dto) throws ATransportModuleException {
        // try to create transport with Transport requeest DTO
        return transportRepo.TransportDTOtoTransport(dto);
    }

    /**
     * Creates and saves a new delivery document.
     */
    public int createProductListDocument(ProductListDocumentDto dto) throws ATransportModuleException {

        ProductListDocument productListDocument= productRepo.PLDdtoTOPLD(doc);
        return productListDocument.getId();
    }

    /**
     * Adds a product to a delivery document.
     */
    public void addProductToDocument(ProductDTO dto, int docId) throws ATransportModuleException {

        documentRepo.addProductToDocument(dto,docId);
    }

    /**
     * Attaches a document to a transport.
     */
    public void attachProductListDocumentToTransport(int docId, int transportId) throws ATransportModuleException {

        // todo - check if repository updates the transport
        transportRepo.attachProductListDocumentToTransport(docId, transportId);
    }

    public void fetchAvailableDriversFromHR() throws Exception {
        // todo - check how to do it
        HRController.requestAvailableDrivers();
    }

    public void assignDriver(String driverId, int transportId) throws Exception {
        // todo - check how to do it
        Driver driver = DriverRepository.getDriverById(driverId);
        Transport transport = TransportRepository.getTransportByid(transportId);

        transport.addDriver(driver);
        TransportRepository.saveTransport(transport);
    }
}
