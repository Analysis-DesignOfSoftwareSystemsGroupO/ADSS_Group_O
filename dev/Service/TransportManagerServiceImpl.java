package Service;

import Transport_Module_Exceptions.*;
import transport_module.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

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

    @Override
    public void addNewTransport(String dateStr, String timeStr, String siteName) throws ATransportModuleException {
        if (trucks == null || transports == null || sites == null || transportsPerDate == null)
            throw new InvalidInputException();

        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException();
        }

        Truck availableTruck = trucks.values().stream()
                .filter(t -> t.getAvailablity(parsedDate))
                .findFirst()
                .orElseThrow(UnAvailableTruckException::new);

        Site site = sites.get(siteName);
        if (site == null) {
            throw new InvalidInputException("Site " + siteName + " not found.");
        }

        Transport transport = new Transport(dateStr, timeStr, availableTruck, site);
        transports.put(transport.getId(), transport);
        transportsPerDate.computeIfAbsent(parsedDate, k -> new ArrayList<>()).add(transport);
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
