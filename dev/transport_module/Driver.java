package transport_module;

import java.util.ArrayList;

public class Driver extends Employee{
    private ArrayList<DrivingLicence> licencs;

    //todo : constructor
    //public Driver(args){
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
    public void assignToMission(){

    }
    public void release(){

    }
    public boolean isavailable(){
        return true;
    }
    @Override
    public String toString(){
        //todo - write this with details.
        StringBuilder str = new StringBuilder(); // build new string
        return str.toString();    }

}
