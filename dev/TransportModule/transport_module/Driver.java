package TransportModule.transport_module;

import HR_Mudol.domain.Objects.Employee;

import java.time.LocalDate;
import java.util.ArrayList;

public class Driver extends Employee{
    private final ArrayList<DrivingLicence> licencs;

    /***
     * Constructor - creates a Driver with given name, ID and a copy of provided licenses.
     */
    public Driver( String empName, int empId, String empPassword, String empBankAccount, int empSalary, LocalDate empStartDate, int minDayShift, int minEveninigShift, int sickDays, int daysOff, ArrayList<DrivingLicence> licencs) {
       super( empName, empId, empPassword, empBankAccount, empSalary, empStartDate, minDayShift, minEveninigShift, sickDays, daysOff);
        this.licencs = new ArrayList<>();
        for (DrivingLicence licence : licencs) {
            this.licencs.add(new DrivingLicence(licence));
        }
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
        return "sa";
//        return Integer.toString(this.getEmpId());
    }

//*********************************************************************************************************************** Set functions





//*********************************************************************************************************************** print functions

    /***
     * @return String representation of Driver's details
     */
    @Override
    public String toString() {
        return super.toString() +", licenses= " + licencs;
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
        return this.getEmpId() == other.getEmpId();
    }

    /***
     * @return Hash code of the driver's ID
     */
    @Override
    public final int hashCode() {
//        return  this.getEmpId();
        return 1;
    }

}
