package TransportModule.transport_module;

import TransportModule.DTO.TransportDTO;
import TransportModule.DTO.ProductListDocumentDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static TransportModule.TransportTestUtils.*;


class TransportContorollerDomainTest {

    private ITransportRepository transportRepoMock;
    private IProductListDocumentRepository productListDocumentRepoMock;
    private TransportContorollerDomain controller;

    @BeforeEach
    void setUp() {
        transportRepoMock = mock(ITransportRepository.class);
        productListDocumentRepoMock = mock(IProductListDocumentRepository.class);
        controller = new TransportContorollerDomain(transportRepoMock, productListDocumentRepoMock);
    }


    @Test
    void getTransportNextWeek_returnsTransportsFromAll7Days() throws Exception {
        List<TransportDTO> sampleList = List.of(createDTO(1));
        when(transportRepoMock.getTransportsDTOByDate(any(LocalDate.class))).thenReturn(sampleList);

        List<TransportDTO> result = controller.getTransportNextWeek();

        assertEquals(7 * sampleList.size(), result.size());
        verify(transportRepoMock, times(7)).getTransportsDTOByDate(any(LocalDate.class));
    }


    @Test
    void getNextWeekTransportsWithNoTrucks_returnsOnlyWithoutTruck() throws Exception {
        TransportDTO withTruck = createDTO(1);
        TransportDTO withoutTruck = createDTOWithoutTruck(2);
        when(transportRepoMock.getTransportsDTOByDate(any(LocalDate.class))).thenReturn(List.of(withTruck, withoutTruck));

        List<TransportDTO> result = controller.getNextWeekTransportsWithNoTrucks();

        assertEquals(7, result.size()); // because one in each day
        assertTrue(result.stream().allMatch(dto -> "-1".equals(dto.getTruckPN())));
    }

    @Test
    void getNextWeekTransportsWithNoDrivers_returnsOnlyWithoutDriver() throws Exception {
        TransportDTO withDriver = createDTO(1);
        TransportDTO withoutDriver = createDTOWithoutDriver(2);
        when(transportRepoMock.getTransportsDTOByDate(any(LocalDate.class))).thenReturn(List.of(withDriver, withoutDriver));

        List<TransportDTO> result = controller.getNextWeekTransportsWithNoDrivers();

        assertEquals(7, result.size());
        assertTrue(result.stream().allMatch(dto -> "-1".equals(dto.getDriverID())));
    }

    @Test
    void removeTransportById_shouldCallDeleteOnRepo() throws Exception {
        controller.removeTransportById("5");
        verify(transportRepoMock).deleteTransport(5);
    }


    @Test
    void getValidID_returnsRepoValue() throws Exception {
        when(productListDocumentRepoMock.getValidID()).thenReturn(42);
        int id = controller.getValidID();
        assertEquals(42, id);
    }

    @Test
    void getNewTransportId_returnsRepoValue() throws Exception {
        when(transportRepoMock.getAvailableid()).thenReturn(99);
        int id = controller.getNewTransportId();
        assertEquals(99, id);
    }

    @Test
    void createTransport_callsTransportDTOtoTransport() throws Exception {
        TransportDTO dto = createDTO(10);
        controller.createTransport(dto);
        verify(transportRepoMock).TransportDTOtoTransport(dto);
    }

    @Test
    void createProductListDocument_callsSaveOnRepo() throws Exception {
        ProductListDocumentDto dto = mock(ProductListDocumentDto.class);
        controller.createProductListDocument(dto);
        verify(productListDocumentRepoMock).saveProductListDocument(dto);
    }

    @Test
    void attachProductListDocumentsToTransport_loadsAndSavesTransport() throws Exception {
        int transportId = 10;
        int docId1 = 1;
        int docId2 = 2;

        Transport mockTransport = mock(Transport.class);
        ProductListDocument pld1 = mock(ProductListDocument.class);
        ProductListDocument pld2 = mock(ProductListDocument.class);
        TransportDTO dtoAfterAttach = createDTO(99);

        when(transportRepoMock.getTransportByid(transportId)).thenReturn(mockTransport);
        when(productListDocumentRepoMock.getProductListDocumentByid(docId1)).thenReturn(pld1);
        when(productListDocumentRepoMock.getProductListDocumentByid(docId2)).thenReturn(pld2);
        when(transportRepoMock.transportToTransportDTO(mockTransport)).thenReturn(dtoAfterAttach);

        controller.attachProductListDocumentsToTransport(List.of(docId1, docId2), transportId);

        verify(mockTransport).loadByDocument(pld1);
        verify(mockTransport).loadByDocument(pld2);
        verify(transportRepoMock).saveTransport(dtoAfterAttach);
    }

    @Test
    void getPLDbyTransportID_returnsProductListDocumentDtoList() throws Exception {
        Transport transport = mock(Transport.class);
        ProductListDocument pld = mock(ProductListDocument.class);
        ProductListDocumentDto dto = mock(ProductListDocumentDto.class);

        when(transportRepoMock.getTransportByid(1)).thenReturn(transport);
        when(transport.getAllPLD()).thenReturn(List.of(pld));
        when(productListDocumentRepoMock.pldToDTO(pld)).thenReturn(dto);

        List<ProductListDocumentDto> result = controller.getPLDbyTransportID("1");

        assertEquals(1, result.size());
        assertSame(dto, result.get(0));
    }


    // todo - will implement soon
//    @Test
//    void assignDriverTransport() {
//    }


}