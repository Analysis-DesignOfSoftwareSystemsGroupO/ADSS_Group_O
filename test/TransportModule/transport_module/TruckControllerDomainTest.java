package TransportModule.transport_module;

import TransportModule.DTO.TransportDTO;
import TransportModule.DTO.TruckDto;
import TransportModule.Transport_Module_Exceptions.InvalidInputException;
import TransportModule.Transport_Module_Exceptions.TruckNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static TransportModule.TransportTestUtils.*;
class TruckControllerDomainTest {

    private ITruckRepository truckRepoMock;
    private ITransportRepository transportRepoMock;
    private TruckControllerDomain controller;

    @BeforeEach
    void setUp() {
        truckRepoMock = mock(ITruckRepository.class);
        transportRepoMock = mock(ITransportRepository.class);
        controller = new TruckControllerDomain(truckRepoMock, transportRepoMock);
    }

    @Test
    void addTruck_addsTruckToRepo() throws Exception {
        TruckDto dto = new TruckDto(2000, "C", "1234");
        controller.addTruck(dto);
        verify(truckRepoMock).addTruck(dto);
    }

    @Test
    void addTruck_throwsExceptionWhenDtoIsNull() {
        assertThrows(InvalidInputException.class, () -> controller.addTruck(null));
    }


    @Test
    void getAllTrucks_returnsDtosFromRepo() throws Exception {
        Truck truck1 = mock(Truck.class);
        Truck truck2 = mock(Truck.class);
        TruckDto dto1 = new TruckDto(2000, "C", "1111");
        TruckDto dto2 = new TruckDto(2500, "B", "2222");

        when(truckRepoMock.getAllTrucks()).thenReturn(List.of(truck1, truck2));
        when(truckRepoMock.truckToDTO(truck1)).thenReturn(dto1);
        when(truckRepoMock.truckToDTO(truck2)).thenReturn(dto2);

        List<TruckDto> result = controller.getAllTrucks();

        assertEquals(2, result.size());
        assertEquals("1111", result.getFirst().getPlateNumber());
    }

    @Test
    void getAllTrucks_throwsExceptionWhenNullReturned() {
        when(truckRepoMock.getAllTrucks()).thenReturn(null);
        assertThrows(TruckNotFoundException.class, () -> controller.getAllTrucks());
    }

    @Test
    void deleteTruck_deletesByPlate() throws Exception {
        controller.deleteTruck("1234");
        verify(truckRepoMock).deleteTruck("1234");
    }

    @Test
    void deleteTruck_throwsWhenPlateEmpty() {
        assertThrows(InvalidInputException.class, () -> controller.deleteTruck(""));
    }

    @Test
    void assignTruckToTransport_successfulAssignment() throws Exception {
        Transport transportMock = mock(Transport.class);
        Truck truckMock = mock(Truck.class);
        TransportDTO dto = createDTO(999);

        when(transportRepoMock.getTransportByid(10)).thenReturn(transportMock);
        when(truckRepoMock.getTruckBYPlateNumber(1234)).thenReturn(truckMock);
        when(transportRepoMock.transportToTransportDTO(transportMock)).thenReturn(dto);

        controller.assignTruckToTransport(10, 1234);

        verify(transportMock).assignTruck(truckMock);
        verify(transportRepoMock).saveTransport(dto);
    }


}