package Service;

import Transport_Module_Exceptions.*;
import transport_module.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the {@link BookingService} interface.
 * Handles customer booking requests by creating new transport entries
 * and assigning an empty product list document to each new transport.
 */
public class BookingServiceImpl implements BookingService {

    // References to shared system maps (injected from external source)
    private final Map<String, Site> sites;
    private final Map<Integer, ProductListDocument> documents;
    private final Map<Integer, Transport> transports;  // stores all transports

    /**
     * Constructs a BookingServiceImpl with the required references to system maps.
     *
     * @param sites map of site names to Site objects
     * @param documents map of document IDs to ProductListDocument objects
     * @param transports map of transport IDs to Transport objects
     */
    public BookingServiceImpl(Map<String, Site> sites,
                              Map<Integer, ProductListDocument> documents,
                              Map<Integer, Transport> transports) {
        this.sites = sites;
        this.documents = documents;
        this.transports = transports;
    }

    /**
     * Creates a new transport request.
     * Validates the source and destination sites, and creates a new Transport instance.
     * An empty ProductListDocument is also attached to the transport automatically.
     *
     * @param dateStr the date of the transport in format dd/MM/yyyy
     * @param timeStr the time of the transport in format HH:mm
     * @param sourceSiteName the name of the source site
     * @param destinationSiteName the name of the destination site
     * @throws ATransportModuleException if site names are invalid or transport creation fails
     */
    @Override
    public void requestTransport(String dateStr, String timeStr, String sourceSiteName, String destinationSiteName)
            throws ATransportModuleException {

        // Look up source site
        Site source = sites.get(sourceSiteName);
        if (source == null)
            throw new InvalidInputException("Source site not found");

        // Look up destination site
        Site destination = sites.get(destinationSiteName);
        if (destination == null)
            throw new InvalidInputException("Destination site not found");

        // Create a new Transport object (constructor handles parsing of date/time)
        Transport transport = new Transport(dateStr, timeStr, source, destination);
        // Store the transport in the system
        transports.put(transport.getId(), transport);

        // Retrieve the auto-created empty document for the destination site
        ProductListDocument doc = transport.getDocument(destination);

        // Store the document in the system
        documents.put(doc.getId(), doc);  // Add to system's document map

        // Feedback to user (should eventually be handled by UI, not here)
        System.out.println("Transport created with ID: " + transport.getId());
        System.out.println("Empty document created with ID: " + doc.getId());
    }

}
