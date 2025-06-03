package TransportModule.transport_module;

import TransportModule.DTO.TransportDTO;
import TransportModule.Transport_Module_Exceptions.ATransportModuleException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TransportRepositoryIMPTest {
    private static TransportRepositoryIMP rep;
    static {
        try {
            rep = TransportRepositoryIMP.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ATransportModuleException e) {
            throw new RuntimeException(e);
        }
    }
    private TransportDTO transportDTO;
    @BeforeEach
    void setUp() throws SQLException, ATransportModuleException {
        transportDTO = new TransportDTO(rep.getAvailableid(), LocalDate.of(2025,6,25),false,0,"123456",null, "OsherAd Yavne", LocalTime.of(15,15));
        rep.saveTransport(transportDTO);
    }

    @AfterEach
    void tearDown() throws SQLException {
        rep.deleteTransport(transportDTO.getId());
    }

    @Test
    void getTransportByid() throws SQLException, ATransportModuleException {
        assertEquals("OsherAd_Yavne",rep.getTransportByid(-1).getSource().getName());
    }


}