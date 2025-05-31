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

    public TransportContorollerDomain() throws Exception{
        this.transportRepo = new TransportRepositoryIMP();
        this.documentRepo = new PLDRepositoryIMP();

    }

    /**
     * Creates a new Transport using data from DTO and saves it.
     */
    public void createTransport(TransportDTO dto) throws Exception {
        // try to create transport with Transport requeest DTO
         transportRepo.TransportDTOtoTransport(dto);
    }

    /**
     * Creates and saves a new delivery document.
     */
    public void createProductListDocument(ProductListDocumentDto dto) throws Exception {

        documentRepo.saveProductListDocument(dto);
    }



    /**
     * Attaches a document to a transport.
     */
    public void attachProductListDocumentsToTransport(List<Integer> docId, int transportId) throws Exception {

        Transport transport = transportRepo.getTransportByid(transportId);
        for(int PLDId : docId){
            ProductListDocument PLD = documentRepo.getProductListDocumentByid(PLDId);
            transport.loadByDocument(PLD);
        }
        TransportDTO transportDTO = transportRepo.transportToTransportDTO(transport);
        transportRepo.saveTransport(transportDTO);

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

    public int getValidID() throws Exception{ // get PLD next id
        return documentRepo.getValidID();
    }

    public int getNewTransportId() throws Exception{
        return transportRepo.getAvailableid();
    }
}
