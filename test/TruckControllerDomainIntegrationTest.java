import TransportModule.transport_module.ITransportRepository;
import TransportModule.transport_module.Truck;
import TransportModule.transport_module.TruckRepositoryIMP;
import org.junit.jupiter.api.BeforeEach;
import TransportModule.DTO.TruckDto;
import TransportModule.transport_module.TruckControllerDomain;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TruckControllerDomainIntegrationTest {

    private TruckRepositoryIMP truckRepo;
    private TruckControllerDomain domain;

    @BeforeEach
    void setUp() throws Exception {
        truckRepo =  TruckRepositoryIMP.getInstance();
        domain = new TruckControllerDomain(truckRepo, mock(ITransportRepository.class));
    }

    @Test
    void addTruck_thenRepoContainsIt() throws Exception {
        TruckDto truck = new TruckDto(2000, "C", "9999");
        domain.addTruck(truck);

        Truck result = truckRepo.getTruckBYPlateNumber(9999);
        assertEquals("9999", result.getPlateNumber());
        assertEquals("C", result.getDrivingLicence().getCode());
    }

    @Test
    void deleteTruck_thenRepoDoesNotContainIt() throws Exception {
        TruckDto truck = new TruckDto(2000, "C", "9999");
        domain.addTruck(truck);
        domain.deleteTruck("9999");

        assertThrows(Exception.class, () -> truckRepo.getTruckBYPlateNumber(9999));
    }
}
