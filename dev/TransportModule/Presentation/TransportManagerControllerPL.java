package TransportModule.Presentation;

import TransportModule.transport_module.TransportContorollerDomain;

public class TransportManagerControllerPL {

    private final TransportContorollerDomain domainController;

    public TransportManagerControllerPL() throws Exception{
        this.domainController = new TransportContorollerDomain();
    }

    public void requestDriversFromHR() throws Exception {
        domainController.fetchAvailableDriversFromHR();
    }

    public void assignDriverToTransport(String driverId, int transportId) throws Exception {
        domainController.assignDriver(driverId, transportId);
    }
}
