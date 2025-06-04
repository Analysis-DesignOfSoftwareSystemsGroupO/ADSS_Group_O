package TransportModule;

import TransportModule.DTO.ProductListDocumentDto;
import TransportModule.DTO.TransportDTO;
import TransportModule.DTO.DriverDto;
import TransportModule.transport_module.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransportControllerPLIntegrationTest {

    private TransportContorollerDomain domain;

    @BeforeEach
    void setUp() {
        try {


            ITransportRepository transportRepo = TransportRepositoryIMP.getInstance();
            IProductListDocumentRepository pldRepo = PLDRepositoryIMP.getInstance();
            DriverControllerDomain driverDomain = new DriverControllerDomain();

            domain = new TransportContorollerDomain(transportRepo, pldRepo, driverDomain);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @AfterEach
    void tearDown() throws Exception {
//        TransportRepositoryIMP.getInstance().clear();
//        PLDRepositoryIMP.getInstance().clear();
    }

    @Test
    void createAndAttachPLDToTransport_shouldUpdateTransportSuccessfully() throws Exception {
        // arrange
        int transportId = domain.getNewTransportId();
        TransportDTO dto = new TransportDTO(
                transportId,
                LocalDate.now(),
                false,
                1500,
                "-1",
                "-1",
                "Tel Aviv",
                LocalTime.of(10, 30)
        );
        domain.createTransport(dto);

        int pldId = domain.getValidID();
        ProductListDocumentDto pldDto = new ProductListDocumentDto(
                pldId,
                -1,
                "Jerusalem",
                List.of(),
                1000,
                LocalDate.now(),
                LocalTime.of(10, 30)
        );
        domain.createProductListDocument(pldDto);

        // act
        domain.attachProductListDocumentsToTransport(List.of(pldId), transportId);

        // assert
        List<ProductListDocumentDto> pldsFromTransport = domain.getPLDbyTransportID(String.valueOf(transportId));
        assertEquals(1, pldsFromTransport.size());
        assertEquals("Jerusalem", pldsFromTransport.getFirst().getSiteDes());
    }

    @Test
    void assignDriver_shouldSetDriverOnTransport() throws Exception {
        int transportId = domain.getNewTransportId();
        TransportDTO dto = new TransportDTO(
                transportId,
                LocalDate.now(),
                false,
                1500,
                "-1",
                "-1",
                "Tel Aviv",
                LocalTime.of(12, 0)
        );
        domain.createTransport(dto);

        String validDriverId = "101";

        // act
        domain.assignDriverTransport(validDriverId, String.valueOf(transportId));

        // assert
        List<TransportDTO> transports = domain.getTransportNextWeek();
        TransportDTO updated = transports.stream()
                .filter(t -> t.getId() == transportId)
                .findFirst()
                .orElseThrow();

        assertEquals(validDriverId, updated.getDriverID());
    }
}
