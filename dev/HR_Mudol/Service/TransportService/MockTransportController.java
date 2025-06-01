package HR_Mudol.Service.TransportService;


import HR_Mudol.DTO.PLDDTO;
import HR_Mudol.DTO.TransportReqDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class MockTransportController implements ITransportController {

    private final Map<String, TransportReqDTO> transportMap = new HashMap<>();
    private final Map<String, List<PLDDTO>> pldMap = new HashMap<>();

    public MockTransportController() {
        // הוספת הובלה לדוגמה
        TransportReqDTO t1 = new TransportReqDTO("T123", "BeerSheva", "TelAviv",
                LocalDate.now().plusDays(2), LocalTime.of(10, 0));

        transportMap.put(t1.getId(), t1);
        pldMap.put(t1.getId(), List.of(
                new PLDDTO("PLD1", "TelAviv", Map.of("ProductA", 20, "ProductB", 10), 300)
        ));
    }

    @Override
    public List<TransportReqDTO> getTransportNextWeek() {
        return new ArrayList<>(transportMap.values());
    }

    @Override
    public List<PLDDTO> getPLDbyTransportID(String transportID) {
        return pldMap.getOrDefault(transportID, Collections.emptyList());
    }

    @Override
    public void setDriverToTruck(String driverID, String transportID) {
        System.out.printf("🚚 Assigned driver %s to truck of transport %s%n", driverID, transportID);
    }

    @Override
    public void assignDriverTransport(String driverID, String transportID) {
        System.out.printf("🧑‍✈️ Assigned driver %s to transport task %s%n", driverID, transportID);
    }
}
