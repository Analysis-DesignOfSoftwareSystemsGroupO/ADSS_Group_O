package TransportModule;

import TransportModule.DTO.TruckDto;
import TransportModule.Transport_Module_Exceptions.InvalidInputException;
import TransportModule.Transport_Module_Exceptions.TruckNotFoundException;
import TransportModule.transport_module.TransportRepositoryIMP;
import TransportModule.transport_module.Truck;

import TransportModule.transport_module.TruckControllerDomain;
import TransportModule.transport_module.TruckRepositoryIMP;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class TruckControllerDomainIntegrationTest {

    private TruckRepositoryIMP truckRepository;
    private TruckControllerDomain truckDomain;

    @BeforeEach
    void setUp() throws Exception {
        truckRepository = TruckRepositoryIMP.getInstance();
        truckDomain = new TruckControllerDomain(truckRepository, TransportRepositoryIMP.getInstance());
    }

    @AfterEach
    void tearDown() {
        // todo - think about clear method in repository
//        truckRepository.clear();
    }

    @Test
    void addTruck_shouldStoreTruckInRepository() throws Exception {
        TruckDto dto = new TruckDto(5000, "C", "1234");

        truckDomain.addTruck(dto);

        Truck actualTruck = truckRepository.getTruckBYPlateNumber(1234);
        assertEquals("1234", actualTruck.getPlateNumber());
        assertEquals("C", actualTruck.getDrivingLicence().getCode());
        assertEquals(5000, actualTruck.getMaxWeight());
    }

    @Test
    void addTruck_nullDto_shouldThrowException() {
        assertThrows(InvalidInputException.class, () -> truckDomain.addTruck(null));
    }

    @Test
    void getAllTrucks_shouldReturnCorrectDTOs() throws Exception {
        truckDomain.addTruck(new TruckDto(3000, "B", "5678"));
        truckDomain.addTruck(new TruckDto(4000, "C", "9876"));

        List<TruckDto> trucks = truckDomain.getAllTrucks();

        assertEquals(2, trucks.size());
        assertTrue(trucks.stream().anyMatch(t -> t.getPlateNumber().equals("5678")));
        assertTrue(trucks.stream().anyMatch(t -> t.getPlateNumber().equals("9876")));
    }

    @Test
    void getAllTrucks_empty_shouldThrowTruckNotFoundException() {
        assertThrows(TruckNotFoundException.class, () -> truckDomain.getAllTrucks());
    }

    @Test
    void deleteTruck_validPlate_shouldRemoveTruck() throws Exception {
        TruckDto dto = new TruckDto(2500, "A", "7777");
        truckDomain.addTruck(dto);

        truckDomain.deleteTruck("7777");

        assertThrows(Exception.class, () -> truckRepository.getTruckBYPlateNumber(7777));
    }

    @Test
    void deleteTruck_emptyPlate_shouldThrowException() {
        assertThrows(InvalidInputException.class, () -> truckDomain.deleteTruck(""));
    }
}
