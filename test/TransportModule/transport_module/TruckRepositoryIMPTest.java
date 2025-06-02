package TransportModule.transport_module;

import TransportModule.DTO.TruckDto;
import TransportModule.Transport_Module_Exceptions.ATransportModuleException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class TruckRepositoryIMPTest {

    private  static TruckRepositoryIMP rep;

    static {
        try {
            rep =  TruckRepositoryIMP.getInstance();
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
    void tearDown() throws SQLException {
        rep.deleteTruck("12345");
    }

    @Test
    void addTruck() throws SQLException, ATransportModuleException {
        TruckDto t = new TruckDto(100, "c4", "12345");
        rep.addTruck(t);
        List<Truck> trucks = rep.getAllTrucks();
        for (Truck truck : trucks){
            System.out.println(truck);
        }
    }

    @Test
    void assignDateToTruck() throws SQLException, ATransportModuleException {
        TruckDto t = new TruckDto(100, "c4", "12345");
        rep.addTruck(t);
        LocalDate date = LocalDate.of(2025,12,31);
        Truck truck = rep.getAllTrucks().get(0);
        rep.AssignDateToTruck(date,"12345");
        assertFalse(truck.getAvailablity(date) );
    }
}