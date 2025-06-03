package TransportModule.transport_module;

import HR_Mudol.DTO.PLDDTO;
import TransportModule.DTO.ProductListDocumentDto;
import TransportModule.Transport_Module_Exceptions.ATransportModuleException;
import TransportModule.Transport_Module_Exceptions.InvalidATransportException;
import TransportModule.Transport_Module_Exceptions.TransportMismatchException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class PLDRepositoryIMPTest {
    private static IProductListDocumentRepository rep;
    static {
        try {
            rep = PLDRepositoryIMP.getInstance();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InvalidATransportException e) {
            throw new RuntimeException(e);
        } catch (TransportMismatchException e) {
            throw new RuntimeException(e);
        }
    }
    private LocalDate date;
    private LocalTime time ;
    private ProductListDocumentDto plddto;
    @BeforeEach
    void setUp() throws SQLException, ATransportModuleException {
        rep.deleteAll();
        date = LocalDate.of(2030,12,30);
        time = LocalTime.of(16,16);
        int id = rep.getValidID();
        plddto = new ProductListDocumentDto(id,-1,"OsherAd_Yavne", null,0,date, time);
        rep.saveProductListDocument(plddto);
    }

    @AfterEach
    void tearDown() throws SQLException {
        rep.deleteAll();
    }

    @Test
    void getValidID() {

        assertEquals(rep.getValidID(),plddto.getId() + 1 );
    }


    @Test
    void getProductListDocumentByid() throws SQLException, InvalidATransportException, TransportMismatchException {
        assertEquals("OsherAd_Yavne", rep.getProductListDocumentByid(2).getDestination().getName());
    }


    @Test
    void getPLDByTransportID() throws InvalidATransportException, TransportMismatchException {
        assertEquals("OsherAd_Yavne", rep.getPLDByTransportID(-1).get(0).getDestination().getName());
    }
}