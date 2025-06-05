package TransportModule;

import TransportModule.DTO.TruckDto;
import TransportModule.Presentation.TruckControllerPL;
import TransportModule.transport_module.TruckControllerDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TruckControllerPLIntegrationTest {  // IT = Integration Test

    private TruckControllerPL controller;

    @BeforeEach
    void setUp() throws Exception {
        TruckControllerDomain domain = new TruckControllerDomain();
        controller = new TruckControllerPL(domain);
    }

    @Test
    void addAndGetTruck_truckAppearsInList() throws Exception {
        // Arrange
        String plate = "9999";
        int maxWeight = 3000;
        String licenceCode = "C";

        // Act
        controller.addTruck(plate, maxWeight, licenceCode);

        List<TruckDto> trucks = controller.getAllTrucks();

        // Assert
        assertTrue(trucks.stream().anyMatch(t ->
                t.getPlateNumber().equals(plate) && t.getLiceenceReq().equals(licenceCode) &&
                        t.getMaxWeight() == maxWeight
        ));

    }

    @Test
    void deleteTruck_removesTruckFromList() throws Exception {
        String plate = "8888";
        controller.addTruck(plate, 3500, "B");

        // Verify truck was added
        assertTrue(controller.getAllTrucks().stream()
                .anyMatch(t -> t.getPlateNumber().equals(plate)));

        // Act
        controller.deleteTruck(plate);

        // Verify truck no longer exists
        assertFalse(controller.getAllTrucks().stream()
                .anyMatch(t -> t.getPlateNumber().equals(plate)));
    }
}
