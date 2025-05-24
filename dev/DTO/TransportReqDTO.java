package DTO;

import java.time.LocalTime;
import java.util.Date;

//Its like a transport Dto but without the truck field and driver field because the driver and the Truck are not yet assigned
public record TransportReqDTO (
    int transportID,
    Date date,
    int maxWeight,
    String site,
    LocalTime departureTime
){}
