package TransportModule.Presentation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import TransportModule.DTO.ProductDTO;
import TransportModule.DTO.TransportDTO;
import TransportModule.transport_module.TransportContorollerDomain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static TransportModule.TransportTestUtils.*;

class BookingControllerPLTest {

    private TransportContorollerDomain domainMock;
    private BookingControllerPL controller;

    @BeforeEach
    void setUp() {
        domainMock = mock(TransportContorollerDomain.class);
        controller = new BookingControllerPL(domainMock);
    }

    @Test
    void createTransport_callsDomainWithCorrectDTO() throws Exception {
        int inputId = 101;
        LocalDate date = LocalDate.of(2025, 6, 10);
        LocalTime time = LocalTime.of(9, 30);
        String source = "Tel Aviv";
        int weight = 2500;

        controller.createTransport(inputId, date, source, weight, time);

        verify(domainMock).createTransport(argThat(dto ->
                dto.getId() == inputId &&
                        dto.getDate().equals(date) &&
                        dto.getMaxWeight() == weight &&
                        dto.getSiteName().equals(source) &&
                        dto.getDepartureTime().equals(time) &&
                        "-1".equals(dto.getDriverID()) &&
                        "-1".equals(dto.getTruckPN())
        ));
    }


    @Test
    void createProductListDocument_callsDomainWithCorrectDTOAndReturnsId() throws Exception {
        int expectedId = 501;
        int transportId = 101;
        String dest = "Jerusalem";
        List<ProductDTO> products = List.of(mock(ProductDTO.class), mock(ProductDTO.class));
        int weight = 1200;
        LocalDate date = LocalDate.of(2025, 6, 11);
        LocalTime time = LocalTime.of(13, 45);

        when(domainMock.getValidID()).thenReturn(expectedId);

        int resultId = controller.createProductListDocument(transportId, dest, products, weight, date, time);

        assertEquals(expectedId, resultId);

        verify(domainMock).createProductListDocument(argThat(dto ->
                dto.getId() == expectedId &&
                        dto.getSiteDes().equals(dest) &&
                        dto.getProducts().equals(products) &&
                        dto.getWeight() == weight &&
                        dto.getDate().equals(date) &&
                        dto.getApproximatedArrivalTime().equals(time)
        ));
    }

    @Test
    void attachProductListDocumentsToTransport_callsDomainCorrectly() throws Exception {
        List<Integer> pldIds = List.of(1, 2, 3);
        int transportId = 10;

        controller.attachProductListDocumentsToTransport(pldIds, transportId);

        verify(domainMock).attachProductListDocumentsToTransport(pldIds, transportId);
    }

    @Test
    void getWeeklyTransportsRequests_returnsResultFromDomain() throws Exception {
        List<TransportDTO> transports = List.of(createDTO(1), createDTO(2));

        when(domainMock.getTransportNextWeek()).thenReturn(transports);

        List<TransportDTO> result = controller.getWeeklyTransportsRequests();

        assertEquals(transports, result);
    }
}