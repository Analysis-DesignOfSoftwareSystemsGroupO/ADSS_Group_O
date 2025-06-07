package TransportModule.Presentation;

import TransportModule.transport_module.TruckControllerDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import TransportModule.DTO.TruckDto;
import TransportModule.Transport_Module_Exceptions.InvalidInputException;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

class TruckControllerPLTest {

    private TruckControllerDomain domainMock;
    private TruckControllerPL controller;

    @BeforeEach
    void setUp() {
        try {
            domainMock = mock(TruckControllerDomain.class);
            controller = new TruckControllerPL(domainMock);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    void addTruck_callsDomainWithCorrectDto() throws Exception {
        controller.addTruck("1234", 5000, "C1");

        verify(domainMock).addTruck(argThat(dto ->
                dto.getPlateNumber().equals("1234") &&
                        dto.getMaxWeight() == 5000 &&
                        dto.getLiceenceReq().equals("C1")
        ));
    }

    @Test
    void deleteTruck_callsDomainWhenInputValid() throws Exception {
        controller.deleteTruck("1234");
        verify(domainMock).deleteTruck("1234");
    }

    @Test
    void deleteTruck_throwsExceptionOnEmptyPlate() {
        Exception exception = assertThrows(InvalidInputException.class, () -> controller.deleteTruck(""));
        assertEquals("plate is Empty String", exception.getMessage());
    }

    @Test
    void attachTruck_parsesInputAndCallsDomain() throws Exception {
        controller.attachTruck("10", "1234");
        verify(domainMock).assignTruckToTransport(10, "1234");

    }

    @Test
    void getAllTrucks_returnsCorrectList() throws Exception {
        List<TruckDto> dummyList = List.of(
                new TruckDto(2000, "B", "111"),
                new TruckDto(3000, "C", "222")
        );

        when(domainMock.getAllTrucks()).thenReturn(dummyList);

        List<TruckDto> result = controller.getAllTrucks();
        assertEquals(2, result.size());
        assertEquals("111", result.getFirst().getPlateNumber());
    }

}