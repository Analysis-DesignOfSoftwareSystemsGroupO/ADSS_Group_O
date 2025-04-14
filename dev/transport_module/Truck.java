package transport_module;

import java.util.HashMap;

// import Pritim

/**
 * multiTone class, holsds all it's instances in a static HashMap. To create an instance outside the class, has to use instance()
 */
public class Truck {
    static private HashMap<String, Truck> trucks;
    private int weight;
    private int maxWeight;
    private boolean available;
    private DrivingLicence liceenceReq;
    private String plateNumber;


    private Truck(DrivingLicence dl, int maxWeight, String pn ){
        this.maxWeight = maxWeight;
        this.liceenceReq = dl;
        this.available =true; // inited as true
        this.plateNumber = pn;
    }

    /**
     * If a truck with the pn already exsists, return this truck. If not create a new truck based on the arguments and store it in the HAsh map.
     * @param dl Driving Licence needed for the truck
     * @param maxWeight max weight of the truck
     * @param pn Unique plate number of the truck
     * @return truck instance
     */
    public Truck instance( DrivingLicence dl, int maxWeight, String pn ){
        if(trucks.containsKey(pn)){ // trucks already exsist in the System, returns it
            return trucks.get(pn);
        }
        Truck t = new Truck( dl,  maxWeight, pn ); //create a new truck
        trucks.put(pn, t); // add the new truck to the HashMap
        return t; //return the new truck
    }

    /**
     *
     * @return the static map of all trucks.
     */
    public static HashMap<String, Truck> getTrucks(){return trucks;}

    /**
     * @return weight of the truck
     */
    public int getWeight(){return weight;}

    /**
     * @return Max weight of truck
     */
    public int getMaxWeight(){return maxWeight;}

    /**
     * @return  Truck Avilablity
     */
    public boolean getAvailablity(){return available;}

    /**
     * @param add_w  The weight to add to the Truck. Pay attention that the paramater is not The new
     *               weight of the truck but the sum.
     * @return if the adding is legal (not over weight) , return True, nither return false.
     */
    public boolean addWeight(int add_w){
        if(weight + add_w > maxWeight){return false;}
        weight = weight - add_w;
        if(weight < 0){ weight =0; } //todo : Think if it os better to set the value to 0 or to provoke an Error
        return true;
    }

    /**
     *
     * @param d Driver instance, check his driving licence and use it's equals() function to check
     *          if the driver can ride the truck
     * @return true or false
     */
    public boolean confirmDriver(Driver d){
        for(DrivingLicence dl : d.getLicencs()) {
            if (dl.equals(liceenceReq)) { // using the equal function of Driving licence
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder(); // build new string
        str.append("Truck num: ").append(plateNumber).append("\n");
        str.append("Licence: ").append(liceenceReq.toString()).append("\n");
        if(!available)
            str.append("currently not available, truck in transit\n");
        else
            str.append("truck is available to transit\n");
        str.append("Maximum weight: ").append(maxWeight).append("\n");
        str.append("Current weight: ").append(weight).append("\n");
        return str.toString();
    }
    public void clear(){
        weight = 0;
        available = true;
    }
    public boolean isOverWeight(){
        return maxWeight>weight;
    }

}

