package TransportModule.transport_module;

import HR_Mudol.domain.repository.EmployeeRepository;
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
//    private final EmployeeRepository employeeRepository;

    public TransportContorollerDomain() throws Exception{
        this.transportRepo = new TransportRepositoryIMP();
        this.ProductListDocumentRepo = new PLDRepositoryIMP();
//        this.employeeRepository = new EmployeeRepository(); // todo - check with Dekel how to get the repo

    }

    // For test section
    public TransportContorollerDomain(ITransportRepository transportRepo, IProductListDocumentRepository productListDocumentRepo) {
        this.transportRepo = transportRepo;
        this.ProductListDocumentRepo = productListDocumentRepo;
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

    public List<ProductListDocumentDto> getPLDbyTransportID(String transportID) throws Exception{
        Transport transport = transportRepo.getTransportByid(Integer.parseInt(transportID));
        List<ProductListDocument> PLDList = transport.getAllPLD();
        List<ProductListDocumentDto> PLDDTOList = new ArrayList<>();
        for(ProductListDocument pld : PLDList){
            PLDDTOList.add(ProductListDocumentRepo.pldToDTO(pld));
        }
        return PLDDTOList;
    }

    public void assignDriverTransport(String driverID, String transportID) throws Exception{
//        Driver driver =(Driver) employeeRepository.getById(Integer.parseInt(driverID));
//        Transport transport = transportRepo.getTransportByid(Integer.parseInt(transportID));
//
//        // try to assign driver
//        transport.addDriver(driver);
//        // try to save transport in DB
//        transportRepo.saveTransport(transportRepo.transportToTransportDTO(transport));
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

        ProductListDocumentRepo.saveProductListDocument(dto);
    }



    /**
     * Attaches a document to a transport.
     */
    public void attachProductListDocumentsToTransport(List<Integer> docId, int transportId) throws Exception {

        Transport transport = transportRepo.getTransportByid(transportId);
        for(int PLDId : docId){
            ProductListDocument PLD = ProductListDocumentRepo.getProductListDocumentByid(PLDId);
            transport.loadByDocument(PLD);
        }
        TransportDTO transportDTO = transportRepo.transportToTransportDTO(transport);
        transportRepo.saveTransport(transportDTO);

    }

    public List<TransportDTO> getNextWeekTransportsWithNoTrucks() throws Exception{
        List<TransportDTO> repoListDTO = getTransportNextWeek();
        List<TransportDTO> noTrucksDTOList = new ArrayList<>();
        for (TransportDTO dto: repoListDTO){
            if(Objects.equals(dto.getTruckPN(), "-1")){
                noTrucksDTOList.add(dto);
            }
        }
        return noTrucksDTOList;
    }

    public List<TransportDTO> getNextWeekTransportsWithNoDrivers() throws Exception{
        List<TransportDTO> repoListDTO = getTransportNextWeek();
        List<TransportDTO> noDriversDTOList = new ArrayList<>();
        for (TransportDTO dto: repoListDTO){
            if(Objects.equals(dto.getDriverID(), "-1")){
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
