package transport_module;

import Transport_Module_Exceptions.ATransportModuleException;
import Transport_Module_Exceptions.InvalidDateException;
import Transport_Module_Exceptions.InvalidDateFormatException;
import Transport_Module_Exceptions.InvalidInputException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TransportRequest {

    private static int staticTransportRequestID = 0;
    private int id;



    private LocalDate date; // field for date of the transport
    private LocalTime departure_time; // the hour of departure time
    private Site source; // the source site the transport is start
    private Site destination; // the source site the transport is start
    private ProductListDocument productListDocument;


    public TransportRequest(String d, LocalTime departure_time, Site source, Site destination)throws ATransportModuleException
    {
        if(d.isEmpty() || departure_time == null || source == null)
            throw new InvalidInputException();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(d, formatter);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException();
        }


        if (parsedDate.isAfter(LocalDate.now())) {
            date = parsedDate;
        } else {
            throw new InvalidDateException("the input date is older than now"); // throw exception invalid date
        }
        this.source = new Site(source);
        this.destination = new Site(destination);
        this.departure_time = departure_time;
        this.id = ++staticTransportRequestID;
        this.productListDocument  = new ProductListDocument(destination,d);

    }
//********************************************************************************************************************** get functions
    public int getId() {
        return id;
    }

    public Site getDestination() {
        return destination;
    }

    public LocalDate getDate() {
        return date;
    }


    public LocalTime getDeparture_time() {
        return departure_time;
    }


    public Site getSource() {
        return source;
    }
    //********************************************************************************************************************** set functions
    public void setSource(Site source) {
        this.source = source;
    }

    public void setDeparture_time(LocalTime departure_time) {
        this.departure_time = departure_time;
    }

    public void setDestination(Site destination) {
        this.destination = destination;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}
