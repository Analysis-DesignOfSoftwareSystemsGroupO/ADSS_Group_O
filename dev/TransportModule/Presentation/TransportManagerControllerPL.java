package TransportModule.Presentation;

import TransportModule.DTO.ProductListDocumentDto;
import TransportModule.DTO.TransportDTO;
import TransportModule.transport_module.TransportContorollerDomain;

import java.util.List;

public class TransportManagerControllerPL {

    private final TransportContorollerDomain domainController;

    public TransportManagerControllerPL() throws Exception{
        this.domainController = new TransportContorollerDomain();
    }

    public TransportManagerControllerPL(TransportContorollerDomain domainController) { // For test section
        this.domainController = domainController;
    }


    public List<TransportDTO> getNextWeekTransports() throws Exception{
        return domainController.getTransportNextWeek();
    }

    public List<TransportDTO> getNextWeekTransportsWithNoTrucks() throws Exception{
        return domainController.getNextWeekTransportsWithNoTrucks();
    }

    public List<TransportDTO> getNextWeekTransportsWithNoDrivers() throws Exception{
        return domainController.getNextWeekTransportsWithNoDrivers();
    }

    public void removeTransportById(String transportId) throws Exception{
        domainController.removeTransportById(transportId);
    }

    public List<ProductListDocumentDto> getAllPLDSByTransportId(int transportId) throws Exception{
        return domainController.getPLDbyTransportID(Integer.toString(transportId));
    }
}