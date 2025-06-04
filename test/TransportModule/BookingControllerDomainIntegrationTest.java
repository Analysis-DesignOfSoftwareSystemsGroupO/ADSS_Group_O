package TransportModule;

import TransportModule.DTO.ProductDTO;
import TransportModule.DTO.TransportDTO;
import TransportModule.Presentation.BookingControllerPL;
import TransportModule.transport_module.*;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookingControllerDomainIntegrationTest {

    private BookingControllerPL bookingController;

    @BeforeEach
    void setUp() throws Exception {
        bookingController = new BookingControllerPL(
                new TransportContorollerDomain(
                        TransportRepositoryIMP.getInstance(),
                        PLDRepositoryIMP.getInstance(),
                        new DriverControllerDomain()
                )
        );


//        TransportRepositoryIMP.getInstance().clear();
//        PLDRepositoryIMP.getInstance().clear();
    }

    @Test
    void fullTransportCreationAndAttachPLD_flowShouldSucceed() throws Exception {

        // arrange
        LocalDate date = LocalDate.now().plusDays(2);
        LocalTime hour = LocalTime.of(10, 0);

        // create transport
        int transportId = bookingController.createTransport(date, "Tel Aviv", 1500, hour);

        // create PLD with products in it
        ProductDTO p1 = new ProductDTO("Milk", 100,20);
        ProductDTO p2 = new ProductDTO("Bread", 200,100);
        List<ProductDTO> products = List.of(p1, p2);

        int pldId = bookingController.createProductListDocument("Haifa", products, 300, date, hour);

        //
        bookingController.attachProductListDocumentsToTransport(List.of(pldId), transportId);

        // act
        List<TransportDTO> result = bookingController.getWeeklyTransportsRequests();

        // assert
        assertEquals(1, result.size());
        TransportDTO dto = result.getFirst();
        assertEquals(transportId, dto.getId());
        assertEquals("Tel Aviv", dto.getSiteName());
        assertEquals("-1", dto.getDriverID());
        assertEquals("-1", dto.getTruckPN());
    }
}
