package TransportModule.Service;

import TransportModule.Transport_Module_Exceptions.ATransportModuleException;

/**
 * Interface for booking transport requests.
 */
public interface BookingService {

    /**
     * Books a new transport request.
     *
     * @param dateStr         delivery date (format: dd/MM/yyyy)
     * @param timeStr         delivery time (format: HH:mm)
     * @param sourceSiteName   source site name
     * @param destinationSiteName     product list document for the delivery
     * @throws ATransportModuleException if any input is invalid
     */
    void requestTransport(String dateStr, String timeStr, String sourceSiteName, String destinationSiteName)
            throws ATransportModuleException;

}
