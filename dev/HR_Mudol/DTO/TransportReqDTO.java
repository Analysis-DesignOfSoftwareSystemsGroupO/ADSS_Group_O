package HR_Mudol.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class TransportReqDTO {
    private final String id;
    private final String source;
    private final String destination;
    private final LocalDate date;
    private final LocalTime time;

    public TransportReqDTO(String id, String source, String destination, LocalDate date, LocalTime time) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.date = date;
        this.time = time;
    }

    public String getId() { return id; }
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
}
