package TransportModule;

import TransportModule.DTO.ProductDTO;
import TransportModule.DTO.ProductListDocumentDto;
import TransportModule.Presentation.BookingControllerPL;
import TransportModule.transport_module.TransportContorollerDomain;
import TransportModule.transport_module.TransportRepositoryIMP;
import TransportModule.transport_module.PLDRepositoryIMP;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingControllerPLIntegrationTest {

    private BookingControllerPL bookingController;

    @BeforeEach
    void setUp() throws Exception {
//        TransportRepositoryIMP.getInstance().clear();
//        PLDRepositoryIMP.getInstance().clear();
        bookingController = new BookingControllerPL();
    }

    @Test
    void integration_createTransport_attachPLD_verifyConnection() throws Exception {
        LocalDate date = LocalDate.of(2025, 6, 10);
        LocalTime time = LocalTime.of(10, 0);
        String source = "Beer Sheva";
        int maxWeight = 3000;

        int transportId = bookingController.createTransport(date, source, maxWeight, time);

        List<ProductDTO> products = List.of(
                new ProductDTO("Apples", 10,3),
                new ProductDTO("Bananas", 5,4)
        );
        String destination = "Tel Aviv";
        int pldId = bookingController.createProductListDocument(destination, products, 15, date, time);

        // attach PLD to transport
        bookingController.attachProductListDocumentsToTransport(List.of(pldId), transportId);

        // check if there is PLD attached to Transport
        TransportContorollerDomain domainController = new TransportContorollerDomain();
        List<ProductListDocumentDto> attachedPLDs = domainController.getPLDbyTransportID(String.valueOf(transportId));

        assertEquals(1, attachedPLDs.size());
        ProductListDocumentDto attached = attachedPLDs.getFirst();
        assertEquals(pldId, attached.getId());
        assertEquals(destination, attached.getSiteDes());
    }
}
