package Service;
import Transport_Module_Exceptions.ATransportModuleException;

import java.time.LocalDate;

/**
 * Interface for transport management operations.
 * Defines all business logic actions related to transports.
 */
public interface TransportManagerService {

    /**
     * Assigns a truck to an existing transport.
     *
     * @param plateNumber the truck's plate number
     * @param transportId the ID of the transport
     * @throws ATransportModuleException if truck or transport not found
     */
    void assignTruckToTransport(String plateNumber, int transportId) throws ATransportModuleException;

    /**
     * Assigns a driver to a transport.
     *
     * @param driverId     the driver's ID
     * @param transportId  the transport's ID
     * @throws ATransportModuleException if driver or transport is not found
     */
    void assignDriverToTransport(String driverId, int transportId) throws ATransportModuleException;

    /**
     * Attaches a document to a transport.
     *
     * @param documentId   the document's ID
     * @param transportId  the transport's ID
     * @throws ATransportModuleException if document or transport not found
     */
    void attachDocumentToTransport(int documentId, int transportId) throws ATransportModuleException;

    /**
     * Manually sends a transport (only if it is delayed).
     *
     * @param transportId  the transport's ID
     * @throws ATransportModuleException if transport not found or can't be sent
     */
    void sendTransportManually(int transportId) throws ATransportModuleException;

    /**
     * Prints all transports that are delayed.
     *
     * @throws ATransportModuleException if transports map is not initialized
     */
    void printAllDelayedTransports() throws ATransportModuleException;
}