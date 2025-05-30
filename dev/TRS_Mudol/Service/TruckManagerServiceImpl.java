package Service;
import Transport_Module_Exceptions.ATransportModuleException;
import Transport_Module_Exceptions.InvalidInputException;
import transport_module.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Implementation of the TruckManagerService interface.
 * Handles business logic for adding, removing, and querying trucks.
 */
public class TruckManagerServiceImpl implements TruckManagerService {
    private final Map<String, Truck> trucks;
    private final Map<String, DrivingLicence> drivingLicences;

    public TruckManagerServiceImpl(Map<String, Truck> trucks, Map<String, DrivingLicence> licences) {
        this.trucks = trucks;
        this.drivingLicences = licences;
    }

    /**
     * Adds a new truck to the system.
     * Validates that the truck does not already exist and the licence is known.
     */
    @Override
    public void addNewTruck(String plateNumber, String licenceCode, int maxWeight) throws ATransportModuleException {
        if (trucks.containsKey(plateNumber)) {
            throw new InvalidInputException("Truck already exists");
        }
        DrivingLicence licence = drivingLicences.get(licenceCode);
        if (licence == null) {
            throw new InvalidInputException("Licence not found");
        }
        if (maxWeight <= 0) {
            throw new InvalidInputException("Weight must be positive");
        }

        Truck truck = new Truck(licence, maxWeight, plateNumber);
        trucks.put(plateNumber, truck); // Add truck to the system
    }

    /**
     * Removes a truck from the system based on its plate number.
     */
    @Override
    public void removeTruck(String plateNumber) throws ATransportModuleException {
        if (!trucks.containsKey(plateNumber)) {
            throw new InvalidInputException("Truck not found");
        }
        trucks.remove(plateNumber);// Remove truck
    }

    /**
     * Returns all trucks currently in the system.
     */
    @Override
    public List<Truck> getAllTrucks() {
        return new ArrayList<>(trucks.values());// Return list of trucks
    }

    /**
     * Returns all trucks available on a specific date.
     */
    @Override
    public List<Truck> getAvailableTrucksOnDate(LocalDate date) {
        return trucks.values().stream()
                .filter(truck -> truck.getAvailablity(date))// Filter available trucks
                .collect(Collectors.toList());
    }
}
