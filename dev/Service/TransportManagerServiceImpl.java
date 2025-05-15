package Service;

import Transport_Module_Exceptions.*;
import transport_module.*;

import java.time.LocalDate;
import java.util.*;

/**
 * Implementation of the TransportManagerService interface.
 * Handles core transport-related operations such as driver assignment,
 * truck assignment, document attachment, manual sending, and delayed reporting.
 */
public class TransportManagerServiceImpl implements TransportManagerService {

    private final Map<String, Truck> trucks;
    private final Map<Integer, Transport> transports;
    private final Map<String, Site> sites;
    private final Map<LocalDate, List<Transport>> transportsPerDate;
    private final Map<String, Driver> drivers;
    private final Map<Integer, ProductListDocument> documents;

    public TransportManagerServiceImpl(
            Map<String, Truck> trucks,
            Map<Integer, Transport> transports,
            Map<String, Site> sites,
            Map<LocalDate, List<Transport>> transportsPerDate,
            Map<String, Driver> drivers,
            Map<Integer, ProductListDocument> documents) {
        this.trucks = trucks;
        this.transports = transports;
        this.sites = sites;
        this.transportsPerDate = transportsPerDate;
        this.drivers = drivers;
        this.documents = documents;
    }

    /**
     * Assigns a truck to an existing transport by ID.
     *
     * @param plateNumber  license plate of the truck to assign
     * @param transportId  ID of the transport
     * @throws ATransportModuleException if the truck or transport is invalid or unavailable
     */
    @Override
    public void assignTruckToTransport(String plateNumber, int transportId) throws ATransportModuleException {
        Truck truck = trucks.get(plateNumber);
        if (truck == null)
            throw new InvalidInputException("Truck not found");

        Transport transport = transports.get(transportId);
        if (transport == null)
            throw new InvalidInputException("Transport not found");

        transport.assignTruck(truck);

        // Update per-date map
        LocalDate date = transport.getDate();
        transportsPerDate.computeIfAbsent(date, k -> new ArrayList<>());
        if (!transportsPerDate.get(date).contains(transport)) {
            transportsPerDate.get(date).add(transport);
        }
    }

    @Override
    public void assignDriverToTransport(String driverId, int transportId) throws ATransportModuleException {
        Driver driver = drivers.get(driverId);
        if (driver == null)
            throw new InvalidInputException("Driver not found");

        Transport transport = transports.get(transportId);
        if (transport == null)
            throw new InvalidInputException("Transport not found");

        transport.addDriver(driver);
    }

    @Override
    public void attachDocumentToTransport(int documentId, int transportId) throws ATransportModuleException {
        ProductListDocument doc = documents.get(documentId);
        if (doc == null)
            throw new InvalidInputException("Document not found");

        Transport transport = transports.get(transportId);
        if (transport == null)
            throw new InvalidInputException("Transport not found");

        transport.loadByDocument(doc);
    }

    @Override
    public void sendTransportManually(int transportId) throws ATransportModuleException {
        Transport transport = transports.get(transportId);
        if (transport == null)
            throw new InvalidInputException("Transport not found");

        if (transport.getStatus() == Transport.Status.delayed) {
            transport.sendTransport();
        }
    }

    @Override
    public void printAllDelayedTransports() throws ATransportModuleException {
        if (transports == null)
            throw new InvalidInputException("Transport data unavailable");

        transports.values().stream()
                .filter(t -> t.getStatus() == Transport.Status.delayed)
                .forEach(System.out::println);
    }
}
