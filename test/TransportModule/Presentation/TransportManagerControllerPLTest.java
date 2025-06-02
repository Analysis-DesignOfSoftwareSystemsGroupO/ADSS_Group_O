package TransportModule.Presentation;

import TransportModule.DTO.TransportDTO;
import TransportModule.transport_module.TransportContorollerDomain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


class TransportManagerControllerPLTest {
    private TransportContorollerDomain domainMock;
    private TransportManagerControllerPL controller;

    private  List<TransportDTO> DTOs;

    private final List<LocalDate> dates = Arrays.asList(LocalDate.of(2025, 6, 1), LocalDate.of(2025, 6, 2), LocalDate.of(2025, 6, 3),LocalDate.of(2025, 10, 21), LocalDate.of(2025, 8, 18), LocalDate.of(2025, 7, 13), LocalDate.of(2025, 7, 17), LocalDate.of(2025, 7, 17), LocalDate.of(2025, 12, 18), LocalDate.of(2025, 6, 21), LocalDate.of(2025, 8, 21), LocalDate.of(2025, 12, 16), LocalDate.of(2025, 8, 24));

    private final List<LocalTime> times = Arrays.asList(LocalTime.of(8, 0), LocalTime.of(10, 30), LocalTime.of(14, 45), LocalTime.of(18, 57),  LocalTime.of(16, 9),  LocalTime.of(23, 54),  LocalTime.of(4, 54),  LocalTime.of(22, 12),  LocalTime.of(13, 36),  LocalTime.of(6, 47),  LocalTime.of(15, 37),  LocalTime.of(22, 45),  LocalTime.of(3, 32));

    private final List<String> siteNames = Arrays.asList("Tel Aviv", "Haifa", "Jerusalem","Eilat", "Ramat Gan", "Beer Sheba");

    private final List<String> driverIDs = Arrays.asList("111", "222", "333","444","555","666", "777", "888", "999", "101010", "111111", "121212", "131313", "141414");

    private final List<String> truckPNs = Arrays.asList("TRUCK111", "TRUCK222", "TRUCK333","TRUCK444", "TRUCK555", "TRUCK666", "TRUCK777", "TRUCK888", "TRUCK999", "TRUCK101010", "TRUCK111111", "TRUCK121212", "TRUCK131313", "TRUCK141414");


    private final Random random = new Random();

    /**a method to pick random variable*/
    private <T> T randomFrom(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }
    private TransportDTO createDTO(int id) {
        return new TransportDTO(
                id,
                randomFrom(dates),
                false,
                2000,
                randomFrom(driverIDs),
                randomFrom(truckPNs),
                randomFrom(siteNames),
                randomFrom(times)
        );
    }

    private TransportDTO createDTOWithoutTruck(int id) {
        return new TransportDTO(
                id,
                randomFrom(dates),
                false,
                2000,
                randomFrom(driverIDs),
                "-1", //no Truck
                randomFrom(siteNames),
                randomFrom(times)
        );
    }
    private TransportDTO createDTOWithoutDriver(int id) {
        return new TransportDTO(
                id,
                randomFrom(dates),
                false,
                2000,
                "-1", // no Driver
                randomFrom(truckPNs),
                randomFrom(siteNames),
                randomFrom(times)
        );
    }
    @BeforeEach
    void setUp() {

        //countRegular: 13
        //countNoTruck: 19
        //countNoDriver: 13
        DTOs = Arrays.asList(createDTOWithoutDriver(1),createDTO(2), createDTO(3), createDTOWithoutTruck(4), createDTOWithoutDriver(5),createDTOWithoutTruck(6), createDTOWithoutTruck(7), createDTO(8), createDTOWithoutTruck(9), createDTOWithoutDriver(10),createDTOWithoutTruck(11), createDTO(12), createDTO(13), createDTOWithoutTruck(14), createDTOWithoutDriver(15),createDTOWithoutTruck(16), createDTO(17), createDTO(18), createDTO(19), createDTOWithoutDriver(20),createDTOWithoutDriver(21),createDTO(22), createDTOWithoutDriver(23),createDTO(24), createDTOWithoutTruck(25), createDTOWithoutDriver(26),createDTOWithoutTruck(27), createDTOWithoutTruck(28), createDTOWithoutDriver(29),createDTOWithoutDriver(30),createDTOWithoutTruck(31), createDTOWithoutTruck(32), createDTOWithoutDriver(33),createDTO(34), createDTO(35), createDTOWithoutTruck(36), createDTOWithoutTruck(37), createDTOWithoutTruck(38), createDTOWithoutDriver(39),createDTOWithoutDriver(40),createDTOWithoutTruck(41), createDTOWithoutTruck(42), createDTO(43), createDTO(44), createDTO(45), createDTO(46), createDTOWithoutTruck(47), createDTO(48), createDTO(49), createDTOWithoutTruck(50) );

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

        assertEquals(50, result.size());    }


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