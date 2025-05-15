package Service;

import Transport_Module_Exceptions.*;
import transport_module.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

/**
 * Implementation of BookingService for handling new transport requests.
 */
public class BookingServiceImpl implements BookingService {

    private final Map<String, Site> sites;
    private final Map<Integer, ProductListDocument> documents;
    private final Map<Integer, Transport> transports;  // stores all transports

    public BookingServiceImpl(Map<String, Site> sites,
                              Map<Integer, ProductListDocument> documents,
                              Map<Integer, Transport> transports) {
        this.sites = sites;
        this.documents = documents;
        this.transports = transports;
    }

    @Override
    public void requestTransport(String dateStr, String timeStr, String sourceSiteName, String destinationSiteName)
            throws ATransportModuleException {
        Site source = sites.get(sourceSiteName);
        if (source == null)
            throw new InvalidInputException("Source site not found");

        Site destination = sites.get(destinationSiteName);
        if (destination == null)
            throw new InvalidInputException("Destination site not found");

        // Create transport with auto-attached empty document
        Transport transport = new Transport(dateStr, timeStr, source, destination);
        transports.put(transport.getId(), transport);

        ProductListDocument doc = transport.getDocument(destination); // Get the auto-created document
        documents.put(doc.getId(), doc);  // Add to system's document map

        System.out.println("Transport created with ID: " + transport.getId());
        System.out.println("Empty document created with ID: " + doc.getId());
    }

}
