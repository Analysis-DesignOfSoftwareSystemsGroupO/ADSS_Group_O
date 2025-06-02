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
    @BeforeEach
    void setUp() throws SQLException, ATransportModuleException {
        rep.deleteAll();
        LocalDate date = LocalDate.of(2030,12,30);
        LocalTime time = LocalTime.of(16,16);
        ProductListDocumentDto plddto = new ProductListDocumentDto(123,-1,"OsherAd_Yavne", null,0,date, time);
        rep.saveProductListDocument(plddto);
    }

    @AfterEach
    void tearDown() throws SQLException {
        rep.deleteAll();
    }

    @Test
    void getValidID() {
        assertEquals(rep.getValidID(), 124);
    }

    @Test
    void getPLDwithOutTransport() {

    }

    @Test
    void getProductListDocumentByid() {
    }

    @Test
    void saveProductListDocument() {
    }

    @Test
    void deleteProductListDocument() {
    }

    @Test
    void getPLDByTransportID() {
    }
}