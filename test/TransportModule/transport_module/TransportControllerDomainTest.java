package TransportModule.transport_module;


import TransportModule.DTO.TransportDTO;


import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransportControllerDomainTest {

    private TransportContorollerDomain controller;
    private ITransportRepository transportRepository;
    private IProductListDocumentRepository productListDocumentRepository;

    @BeforeEach
    void setUp() {
        try {


//        transportRepository.clear();
//        productListDocumentRepository.clear();

            controller = new TransportContorollerDomain();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
//        transportRepository.clear();
//        productListDocumentRepository.clear();
    }

    @Test
    void createTransport_shouldSaveTransportInRepository() throws Exception {
        // arrange
        int id = controller.getNewTransportId();
        TransportDTO dto = new TransportDTO(
                id,
                LocalDate.now().plusDays(7),
                false,
                1000,
                null,
                null,
                "Tel Aviv",
                LocalTime.of(9, 0)
        );

        // act
        controller.createTransport(dto);
        List<TransportDTO> list = controller.getTransportNextWeek();

        // assert
        assertTrue(list.stream().anyMatch(t -> t.getId() == id));
    }

    @Test
    void removeTransportById_shouldRemoveTransportFromRepository() throws Exception {
        int id = controller.getNewTransportId();
        TransportDTO dto = new TransportDTO(
                id,
                LocalDate.now().plusDays(1),
                false,
                2000,
                null,
                null,
                "Haifa",
                LocalTime.of(11, 0)
        );
        controller.createTransport(dto);

        controller.removeTransportById(String.valueOf(id));

        List<TransportDTO> list = controller.getTransportNextWeek();
        assertFalse(list.stream().anyMatch(t -> t.getId() == id));
    }

    @Test
    void getNextWeekTransportsWithNoDrivers_shouldReturnOnlyUnassigned() throws Exception {
        int id1 = controller.getNewTransportId();

        controller.createTransport(new TransportDTO(
                id1, LocalDate.now().plusDays(7), false, 1000, null, null, "Jerusalem", LocalTime.of(8, 0)
        ));


        List<TransportDTO> result = controller.getNextWeekTransportsWithNoDrivers();
        assertTrue(result.stream().anyMatch(t -> t.getId() == id1));

    }
}