package TransportModule.transport_module;

import HR_Mudol.domain.Objects.Employee;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Driver extends Employee{
    private ArrayList<DrivingLicence> licencs;

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

    public int getNumberOfDrivingLicencs(){
        return licencs.size();
    }

    public int getId(){
        return this.getEmpId();
    }

//*********************************************************************************************************************** Set functions





//*********************************************************************************************************************** print functions

    /***
     * @return String representation of Driver's details
     */
    @Override
    public String toString() {
        return super() +", licenses=" + licencs;
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
        return  this.getEmpId();
    }

}
