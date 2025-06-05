package TransportModule.transport_module;

import java.util.ArrayList;
import java.util.Objects;

public class Driver {

    private final ArrayList<DrivingLicence> licencs;
    private String id;

    /***
     * Constructor - creates a Driver with given name, ID and a copy of provided licenses.
     */
    public Driver( String id, ArrayList<DrivingLicence> licencs) {
        this.licencs = new ArrayList<>();
        for (DrivingLicence licence : licencs) {
            this.licencs.add(new DrivingLicence(licence));
        }
        this.id = id;
    }



    //********************************************************************************************************************** Get functions

    /**
     * @return Copy the Driving license list of the driver
     */
    public ArrayList<DrivingLicence> getLicencs() { //return copy of the list
        ArrayList<DrivingLicence> cpy = new ArrayList<>();
        for (DrivingLicence dl : licencs) {
            cpy.add(new DrivingLicence(dl));
        }
        return cpy;
    }


    public void addLicence(String Lincence){
        licencs.add(new DrivingLicence(Lincence));
    }

    /***
     * Checks if the driver has a specific driving license.
     * @param licence license to check
     * @return true if driver has the license, false otherwise
     */
    public boolean hasLicencs(DrivingLicence licence) {
        for (DrivingLicence dl : licencs) {
            if (dl.equals(licence))
                return true;
        }
        return false;
    }

    public int getNumberOfDrivingLicencs(){
        return licencs.size();
    }

    public String getId(){
        return id;
    }

//*********************************************************************************************************************** Set functions


    public void setId(String id) {
        this.id = id;
    }


//*********************************************************************************************************************** print functions

    /***
     * @return String representation of Driver's details
     */
    @Override
    public String toString() {
        return id +", licenses= " + licencs;
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
        return Objects.equals(other.id, this.id);
    }

    /***
     * @return Hash code of the driver's ID
     */
    @Override
    public final int hashCode() {
        return  this.id.hashCode();
    }

}
