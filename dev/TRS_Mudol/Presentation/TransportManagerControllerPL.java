package Presentation;

import transport_module.TransportContorollerDomain;

public class TransportManagerControllerPL {

    private final TransportContorollerDomain domainController;

    public TransportManagerControllerPL() {
        this.domainController = new TransportContorollerDomain();
    }

    public void requestDriversFromHR() throws Exception {
        domainController.fetchAvailableDriversFromHR(); // פעולה שתעדכן את רשימת הנהגים הזמינים
    }

    public void assignDriverToTransport(String driverId, int transportId) throws Exception {
        domainController.assignDriver(driverId, transportId);
    }
}
