package TransportModule.Presentation;

import TransportModule.DTO.TransportDTO;
import TransportModule.transport_module.TransportContorollerDomain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static TransportModule.TransportTestUtils.*;


class TransportManagerControllerPLTest {
    private TransportContorollerDomain domainMock;
    private TransportManagerControllerPL controller;

    private List<TransportDTO> DTOs;

    @BeforeEach
    void setUp() {

        //countRegular: 13
        //countNoTruck: 19
        //countNoDriver: 13
        DTOs = Arrays.asList(createDTOWithoutDriver(1), createDTO(2), createDTO(3), createDTOWithoutTruck(4), createDTOWithoutDriver(5), createDTOWithoutTruck(6), createDTOWithoutTruck(7), createDTO(8), createDTOWithoutTruck(9), createDTOWithoutDriver(10), createDTOWithoutTruck(11), createDTO(12), createDTO(13), createDTOWithoutTruck(14), createDTOWithoutDriver(15), createDTOWithoutTruck(16), createDTO(17), createDTO(18), createDTO(19), createDTOWithoutDriver(20), createDTOWithoutDriver(21), createDTO(22), createDTOWithoutDriver(23), createDTO(24), createDTOWithoutTruck(25), createDTOWithoutDriver(26), createDTOWithoutTruck(27), createDTOWithoutTruck(28), createDTOWithoutDriver(29), createDTOWithoutDriver(30), createDTOWithoutTruck(31), createDTOWithoutTruck(32), createDTOWithoutDriver(33), createDTO(34), createDTO(35), createDTOWithoutTruck(36), createDTOWithoutTruck(37), createDTOWithoutTruck(38), createDTOWithoutDriver(39), createDTOWithoutDriver(40), createDTOWithoutTruck(41), createDTOWithoutTruck(42), createDTO(43), createDTO(44), createDTO(45), createDTO(46), createDTOWithoutTruck(47), createDTO(48), createDTO(49), createDTOWithoutTruck(50));

        domainMock = mock(TransportContorollerDomain.class);
        controller = new TransportManagerControllerPL(domainMock);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getNextWeekTransports_returnsExpectedList() throws Exception {
        when(domainMock.getTransportNextWeek()).thenReturn(DTOs);

        List<TransportDTO> result = controller.getNextWeekTransports();

        assertEquals(50, result.size());
    }


    @Test
    void getNextWeekTransportsWithNoTrucks_returnsOnlyThoseWithoutTruck() throws Exception {
        List<TransportDTO> withoutTruck = DTOs.stream()
                .filter(dto -> "-1".equals(dto.getTruckPN()))
                .toList();

        when(domainMock.getNextWeekTransportsWithNoTrucks()).thenReturn(withoutTruck);

        List<TransportDTO> result = controller.getNextWeekTransportsWithNoTrucks();

        assertEquals(withoutTruck.size(), result.size());
        assertTrue(result.stream().allMatch(dto -> "-1".equals(dto.getTruckPN())));
    }

    @Test
    void getNextWeekTransportsWithNoDrivers_returnsOnlyThoseWithoutDriver() throws Exception {
        List<TransportDTO> withoutDriver = DTOs.stream()
                .filter(dto -> "-1".equals(dto.getDriverID()))
                .toList();

        when(domainMock.getNextWeekTransportsWithNoDrivers()).thenReturn(withoutDriver);

        List<TransportDTO> result = controller.getNextWeekTransportsWithNoDrivers();

        assertEquals(withoutDriver.size(), result.size());
        assertTrue(result.stream().allMatch(dto -> "-1".equals(dto.getDriverID())));
    }

    @Test
    void removeTransportById_shouldCallDomainMethod() throws Exception {
        String id = "5"; // נניח קלט מ־Scanner
        controller.removeTransportById(id);
        verify(domainMock).removeTransportById(id);
    }
}