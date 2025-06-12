package TransportModule.transport_module;

import TransportModule.DTO.DriverDto;
import TransportModule.DTO.ProductListDocumentDto;
import TransportModule.DTO.TransportDTO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransportContorollerDomain implements ITransportController {

    private final ITransportRepository transportRepo;
    private final IProductListDocumentRepository ProductListDocumentRepo;
    private final DriverControllerDomain driverControllerDomain;

    public TransportContorollerDomain() throws Exception{
        this.transportRepo = TransportRepositoryIMP.getInstance();
        this.ProductListDocumentRepo =  PLDRepositoryIMP.getInstance();
        transportRepo.injectPLDRepository(ProductListDocumentRepo);
        ProductListDocumentRepo.injectTransportRepository(transportRepo);
        ProductListDocumentRepo.initRep();
        transportRepo.initRep();
        this.driverControllerDomain = new DriverControllerDomain();


    }

    public void addDriverFromDto(DriverDto dto) throws Exception {
        driverControllerDomain.addDriverFromDto(dto);
    }

    public String getLicenceRequiredByTransportID(int Tid)throws Exception{
        Transport t = transportRepo.getTransportByid(Tid);
        if(t == null )return null;
        return t.getTruck().getDrivingLicence().getCode();
    }

    // For test section
    public TransportContorollerDomain(ITransportRepository transportRepo, IProductListDocumentRepository productListDocumentRepo, DriverControllerDomain driverControllerDomain) {
        this.transportRepo = transportRepo;
        this.ProductListDocumentRepo = productListDocumentRepo;
        this.driverControllerDomain = driverControllerDomain;
    }

    public List<TransportDTO> getTransportNextWeek() throws Exception{
        int day = LocalDate.now().getDayOfMonth();
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        LocalDate today = LocalDate.of(year, month, day);

        List<TransportDTO> transportList = new ArrayList<>();

        LocalDate nextSunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        for (int i = 0; i < 7; i++) {
            LocalDate nextday = nextSunday.plusDays(i);
            List<TransportDTO> todaysList = transportRepo.getTransportsDTOByDate(nextday);
            transportList.addAll(todaysList);
        }

        return transportList;
    }

    public List<ProductListDocumentDto> getPLDbyTransportID(int transportID) throws Exception{
        Transport transport = transportRepo.getTransportByid(transportID);
        List<ProductListDocument> PLDList = transport.getAllPLD();
        List<ProductListDocumentDto> PLDDTOList = new ArrayList<>();
        for(ProductListDocument pld : PLDList){
            PLDDTOList.add(ProductListDocumentRepo.pldToDTO(pld));
        }
        return PLDDTOList;
    }

    public void assignDriverTransport(String driverID, int transportID) throws Exception{
        DriverDto driverDto = driverControllerDomain.getDriverById(driverID);
        Driver driver = driverControllerDomain.getDriverFromDTO(driverDto);
        Transport transport = transportRepo.getTransportByid(transportID);

        transport.addDriver(driver); // throws exception if failed
        TransportDTO tDTO =  new TransportDTO(transport.getId(), transport.getDate(), transport.isSent(), transport.getMaxWeight() ,transport.getDriver().getId(), transport.getTruck().getPlateNumber(),transport.getSource().getName(), transport.getDeparture_time() );
        transportRepo.updateTransport(tDTO);
    }


    /**
     * Creates a new Transport using data from DTO and saves it.
     */
    public void createTransport(TransportDTO dto) throws Exception {
        // try to create transport with Transport requeest DTO
        transportRepo.saveTransport(dto);
    }

    /**
     * Creates and saves a new delivery document.
     */
    public void createProductListDocument(ProductListDocumentDto dto) throws Exception {

        ProductListDocumentRepo.saveProductListDocument(dto);
    }



    /**
     * Attaches a document to a transport.
     */
    public void attachProductListDocumentsToTransport(List<Integer> docId, int transportId) throws Exception {

        Transport transport = transportRepo.getTransportByid(transportId);
        for(int PLDId : docId){
            ProductListDocument PLD = ProductListDocumentRepo.getProductListDocumentByid(PLDId);
            ProductListDocumentRepo.attachTransport(PLDId,transportId);
            transport.loadByDocument(PLD);
        }

    }

    public List<TransportDTO> getNextWeekTransportsWithNoTrucks() throws Exception{
        List<TransportDTO> repoListDTO = getTransportNextWeek();
        List<TransportDTO> noTrucksDTOList = new ArrayList<>();
        for (TransportDTO dto: repoListDTO){
            if(dto.getTruckPN() == null){
                noTrucksDTOList.add(dto);
            }
        }
        return noTrucksDTOList;
    }

    public List<TransportDTO> getNextWeekTransportsWithNoDrivers() throws Exception{
        List<TransportDTO> repoListDTO = getTransportNextWeek();
        List<TransportDTO> noDriversDTOList = new ArrayList<>();
        for (TransportDTO dto: repoListDTO){
            if(dto.getDriverID() == null){
                noDriversDTOList.add(dto);
            }
        }
        return noDriversDTOList;
    }

    public void removeTransportById(String transportId) throws Exception{
        transportRepo.deleteTransport(Integer.parseInt(transportId));
    }



    public int getValidID() throws Exception{ // get PLD next id
        return ProductListDocumentRepo.getValidID();
    }

    public int getNewTransportId() throws Exception{
        return transportRepo.getAvailableid();
    }


}