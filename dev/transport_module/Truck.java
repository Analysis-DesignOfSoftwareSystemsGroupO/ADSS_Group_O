package transport_module;

import Transport_Module_Exceptions.InvalidInputException;
import Transport_Module_Exceptions.ATransportModuleException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

// import Pritim

/**
 * multiTone class, holsds all it's instances in a static HashMap. To create an instance outside the class, has to use instance()
 */
public class Truck {
    private int weight;
    private final int maxWeight;
    private final DrivingLicence liceenceReq;
    private final String plateNumber;
    private final Map<LocalDate, Boolean> availablityCalander;


    public Truck(DrivingLicence dl, int maxWeight, String pn) throws ATransportModuleException {
        if(dl == null || maxWeight<1 || pn.isEmpty())
            throw new InvalidInputException();
        this.maxWeight = maxWeight;
        this.liceenceReq = dl;
        this.plateNumber = pn;
        this.availablityCalander = new HashMap<>();
    }


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

    public String getPlateNumber(){
        return plateNumber;
    }

    /**
     * @return Truck Avilablity
     */
    public boolean getAvailablity(LocalDate date) {
        if (date != null) {
            return this.availablityCalander.get(date) == null;
        }
        return false;

    }

    public DrivingLicence getDrivingLicence() {
        return liceenceReq;
    }

    public void releaseTruck(LocalDate date) {
        if (availablityCalander.get(date) != null) {
            availablityCalander.remove(date);
        }
    }

    public void setDate(LocalDate date) {
        if (date != null) {
            availablityCalander.put(date, true);
        }
    }

    /**
     * @param add_w The weight to add to the Truck. Pay attention that the paramater is not The new
     *              weight of the truck but the sum.
     * @return if the adding is legal (not over weight) , return True, nither return false.
     */
    public boolean addWeight(int add_w) {
        if (weight + add_w > maxWeight) {
            return false;
        }
        weight = weight + add_w;
        if (weight < 0) {
            weight = 0;
        } //todo : Think if it os better to set the value to 0 or to provoke an Error
        return true;
    }

    /**
     * @param d Driver instance, check his driving licence and use it's equals() function to check
     *          if the driver can ride the truck
     * @return true or false
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

    public void clear() {
        weight = 0;
    }

    public boolean isOverWeight() {
        return maxWeight < weight;
    }

    @Override
    public final boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Truck that = (Truck) other;

        return this.getPlateNumber().equals(that.getPlateNumber());
    }

    @Override
    public final int hashCode() {
        return this.getPlateNumber()!=null ? this.getPlateNumber().hashCode() : 0;
    }

}

