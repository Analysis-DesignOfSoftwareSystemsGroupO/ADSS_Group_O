package TransportModule.Presentation;

import TransportModule.transport_module.TransportContorollerDomain;

public class TransportManagerControllerPL {

    private final TransportContorollerDomain domainController;

    public TransportManagerControllerPL() throws Exception{
        this.domainController = new TransportContorollerDomain();
    }

}
