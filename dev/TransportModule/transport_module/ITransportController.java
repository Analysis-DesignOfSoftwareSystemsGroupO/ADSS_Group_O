package TransportModule.transport_module;


import TransportModule.DTO.ProductListDocumentDto;
import TransportModule.DTO.TransportDTO;

import java.util.List;

public interface ITransportController {

    List<TransportDTO> getTransportNextWeek() throws Exception;

    List<ProductListDocumentDto> getPLDbyTransportID(String transportID) throws Exception;

    void assignDriverTransport(String driverID, String transportID) throws Exception;
}