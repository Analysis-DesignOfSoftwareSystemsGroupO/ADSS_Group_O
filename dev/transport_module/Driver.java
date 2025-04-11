package transport_module;

import java.util.ArrayList;

public class Driver extends Employee{
    private ArrayList<DrivingLicence> licencs;

    //todo : constructor
    //public Diver(args){
      //  super();
   // }

    /**
     * @return Copy of the Driving licence list of the driver
     */
    public ArrayList<DrivingLicence> getLicencs(){ //return copy of the list
        ArrayList<DrivingLicence> cpy = new ArrayList<DrivingLicence>();
        for(DrivingLicence dl : licencs){
            cpy.add(new DrivingLicence(dl));
        }
        return cpy;
    }

}
