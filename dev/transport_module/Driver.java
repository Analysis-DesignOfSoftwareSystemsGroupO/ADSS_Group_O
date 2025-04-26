package transport_module;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Driver {
    private ArrayList<DrivingLicence> licencs;
    private final String name;
    private final String id;
    private Map<LocalDate, Boolean> availablityCalander;

    /***
     * Constructor - creates a Driver with given name, ID and a copy of provided licenses.
     */
    public Driver(String name, String id, ArrayList<DrivingLicence> licencs) {
        this.name = name;
        this.id = id;
        this.licencs = new ArrayList<>();
        for (DrivingLicence licence : licencs) {
            this.licencs.add(new DrivingLicence(licence));
        }
        availablityCalander = new HashMap<>();
    }



    //********************************************************************************************************************** Get functions

    /**
     * @return Copy of the Driving licence list of the driver
     */
    public ArrayList<DrivingLicence> getLicencs() { //return copy of the list
        ArrayList<DrivingLicence> cpy = new ArrayList<DrivingLicence>();
        for (DrivingLicence dl : licencs) {
            cpy.add(new DrivingLicence(dl));
        }
        return cpy;
    }

    /***
     * @return Driver's ID
     */
    public String getId() {
        return id;
    }

    /***
     * @return Driver's name
     */
    public String getName() {
        return name;
    }

    /***
     * Checks if the driver is available on a specific date.
     * @param date Date to check
     * @return true if available, false if already assigned
     */
    public boolean isavailable(LocalDate date) {
        return availablityCalander.get(date) == null;
    }


    /***
     * Checks if the driver has a specific driving licence.
     * @param licence Licence to check
     * @return true if driver has the licence, false otherwise
     */
    public boolean hasLicencs(DrivingLicence licence) {
        for (DrivingLicence dl : licencs) {
            if (dl.equals(licence))
                return true;
        }
        return false;
    }

//*********************************************************************************************************************** Set functions

    /***
     * Assigns the driver to a mission on a specific date (makes him unavailable).
     * @param date Date to assign
     */
    public void assignToMission(LocalDate date) {
        availablityCalander.put(date, true);

    }

    /***
     * Releases the driver from a mission on a specific date (makes him available again).
     * @param date Date to release
     */
    public void release(LocalDate date) {
        availablityCalander.remove(date);

    }

//*********************************************************************************************************************** print functions

    /***
     * @return String representation of Driver's details
     */
    @Override
    public String toString() {
        return "Driver{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", licenses=" + licencs +
                '}';
    }

    /***
     * Compares this driver to another object based on ID.
     * @param obj Other object to compare
     * @return true if IDs match, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Driver other = (Driver) obj;
        return this.id.equals(other.id);
    }

    /***
     * @return Hash code of the driver's ID
     */
    @Override
    public final int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }

}
