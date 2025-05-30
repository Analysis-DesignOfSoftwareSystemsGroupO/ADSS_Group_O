package transport_module;


import DTO.ProductListDocumentDto;
import DTO.TransportReqDTO;

import java.util.List;

public interface ItransperController {

    List<TransportReqDTO> getTransportNextWeek();

    List<ProductListDocumentDto> getPLDbyTransportID(String transportID);

    void SetDriverToTruck(String truckID, String driverID);

    void assignDriverTransport(String driverID, String transportID);
}
