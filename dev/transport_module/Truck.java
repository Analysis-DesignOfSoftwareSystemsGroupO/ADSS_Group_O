package transport_module;

import Transport_Module_Exceptions.InvalidInputException;
import Transport_Module_Exceptions.ATransportModuleException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


/***
 * A Truck class representing a vehicle used for transport.
 * Each truck has weight limitations, a license requirement, and availability management.
 */
public class Truck {
    private int weight;
    private final int maxWeight;
    private final DrivingLicence liceenceReq;
    private final String plateNumber;
    private final Map<LocalDate, Boolean> availablityCalander;

    /***
     * Constructor - creates a new Truck instance
     * @param dl DrivingLicence required to drive the truck
     * @param maxWeight Maximum load capacity
     * @param pn Plate number
     * @throws ATransportModuleException if input is invalid
     */
    public Truck(DrivingLicence dl, int maxWeight, String pn) throws ATransportModuleException {
        if(dl == null || maxWeight<1 || pn.isEmpty())
            throw new InvalidInputException();
        this.maxWeight = maxWeight;
        this.liceenceReq = dl;
        this.plateNumber = pn;
        this.availablityCalander = new HashMap<>();
    }

//********************************************************************************************************************** Get functions
    /**
     * @return weight of the truck
     */
    public int getWeight() {
        return weight;
    }

    /**
     * @return Max weight of truck
     */
    public int getMaxWeight() {
        return maxWeight;
    }

    /***
     * @return Plate number of the truck
     */
    public String getPlateNumber(){
        return plateNumber;
    }

    /***
     * Checks if the truck is available on a specific date
     * @param date Date to check
     * @return True if available, false if already scheduled
     */
    public boolean getAvailablity(LocalDate date) {
        if (date != null) {
            return this.availablityCalander.get(date) == null;
        }
        return false;

    }

    /***
     * @return Required Driving Licence for the truck
     */
    public DrivingLicence getDrivingLicence() {
        return liceenceReq;
    }

    /***
     * Checks if the current load exceeds maximum weight
     * @return True if overweight, otherwise false
     */
    public boolean isOverWeight() {
        return maxWeight < weight;
    }

    /***
     * Confirms if a driver has a valid license to drive the truck
     * @param d Driver instance
     * @return True if driver's license matches, false otherwise
     * @throws ATransportModuleException if driver is null
     */
    public boolean confirmDriver(Driver d) throws ATransportModuleException {
        if (d == null)
            throw new InvalidInputException();
        for (DrivingLicence dl : d.getLicencs()) {
            if (dl.equals(liceenceReq)) { // using the equal function of Driving licence
                return true;
            }
        }
        return false;
    }



//********************************************************************************************************************** Set functions

    /***
     * Schedules the truck for a specific date (marks it unavailable)
     * @param date Date to reserve
     */
    public void setDate(LocalDate date) {
        if (date != null) {
            availablityCalander.put(date, true);
        }
    }

    /***
     * Releases the truck reservation for a specific date
     * @param date Date to free
     */
    public void releaseTruck(LocalDate date) {
        if (availablityCalander.get(date) != null) {
            availablityCalander.remove(date);
        }
    }

    /***
     * Attempts to add weight to the truck
     * @param add_w Additional weight to add
     * @return True if successful, False if exceeds maximum weight
     */
    public boolean addWeight(int add_w) {
        if (weight + add_w > maxWeight) {
            return false;
        }
        weight = weight + add_w;

        return true;
    }

    /***
     * Clears the current weight on the truck (sets weight to 0)
     */
    public void clear() {
        weight = 0;
    }

//********************************************************************************************************************** Print functions


    /***
     * Prints the full details of the truck
     * @return Formatted string with truck information
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(); // build new string
        str.append("Truck num: ").append(plateNumber).append("\n");
        str.append("Licence: ").append(liceenceReq.toString()).append("\n");
        if (!getAvailablity(LocalDate.now()))
            str.append("currently not available, truck in transit\n");
        else
            str.append("truck is available to transit\n");
        str.append("Maximum weight: ").append(maxWeight).append("\n");
        str.append("Current weight: ").append(weight).append("\n");
        return str.toString();
    }



    /***
     * Compares trucks based on their plate numbers
     */
    @Override
    public final boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Truck that = (Truck) other;

        return this.getPlateNumber().equals(that.getPlateNumber());
    }

    /***
     * @return Hash code based on plate number
     */
    @Override
    public final int hashCode() {
        return this.getPlateNumber()!=null ? this.getPlateNumber().hashCode() : 0;
    }

}

