package Service;

import Transport_Module_Exceptions.ATransportModuleException;
import transport_module.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for managing trucks in the system.
 */
public interface TruckManagerService {

    /**
     * Adds a new truck to the system.
     *
     * @param plateNumber    the truck's license plate number
     * @param licenceCode    the driving licence code required to operate the truck
     * @param maxWeight      the truck's maximum weight capacity
     * @throws ATransportModuleException if the input is invalid or truck already exists
     */
    void addNewTruck(String plateNumber, String licenceCode, int maxWeight) throws ATransportModuleException;

    /**
     * Removes a truck from the system.
     *
     * @param plateNumber    the truck's plate number to remove
     * @throws ATransportModuleException if the truck is not found
     */
    void removeTruck(String plateNumber) throws ATransportModuleException;

    /**
     * Retrieves a list of all trucks in the system.
     *
     * @return a list of all trucks
     */
    List<Truck> getAllTrucks();

    /**
     * Retrieves a list of trucks that are available on a specific date.
     *
     * @param date the date to check availability
     * @return list of available trucks
     * @throws ATransportModuleException if input is invalid
     */
    List<Truck> getAvailableTrucksOnDate(LocalDate date) throws ATransportModuleException;
}
