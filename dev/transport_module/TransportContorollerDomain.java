package transport_module;

import DTO.ProductListDocumentDto;
import DTO.ProductDTO;
import DTO.TransportDTO;
import Transport_Module_Exceptions.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class TransportContorollerDomain {

    private final ITransportRepository transportRepo;
    private final IProductListDocumentRepository documentRepo;

    public TransportContorollerDomain() {
        this.transportRepo = new TransportRepositoryIMP();
        this.documentRepo = new ProductListDocumentRepositoryIMP();

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

    public TransportDTO makeTransportDTOFromTransport(Transport transport) throws Exception{
        int driverId = -1;
        int truckId = -1;
        if(transport.getTruck()!= null)
            truckId = Integer.parseInt(transport.getTruck().getPlateNumber());
        if ( transport.getDriver()!= null)
            driverId = Integer.parseInt(transport.getDriver().getId());
        return new TransportDTO(transport.getId(),transport.getDate().toString(), transport.isSent(), transport.getMaxWeight(),driverId,truckId,transport.getSource().getName());
    }


    public TransportDTO[] getWeeklyTransportsRequests(LocalDate date) throws Exception{
        List<TransportDTO> transportList = new ArrayList<>();

        LocalDate nextSunday = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        for (int i = 0; i < 7; i++) {
            LocalDate day = nextSunday.plusDays(i);
            List<TransportDTO> todaysList = transportRepo.getTransportsDTOByDate(day);
            transportList.addAll(todaysList);
        }
        TransportDTO[] transportDTOS = new TransportDTO[transportList.size()];
        for(int i=0; i<transportList.size();i++){
            transportDTOS[i] = transportList.get(i);
        }
        return transportDTOS;
    }
}
