package TransportModule.transport_module;

import TransportModule.Transport_Module_Exceptions.ATransportModuleException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class TruckRepositoryIMPTest {

    private  static TruckRepositoryIMP rep;

    static {
        try {
            rep = new TruckRepositoryIMP();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ATransportModuleException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addTruck() {
    }
}