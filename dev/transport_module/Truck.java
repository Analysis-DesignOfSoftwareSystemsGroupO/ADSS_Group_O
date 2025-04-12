package transport_module;
// import Pritim
public class Truck {
    private int weight;
    private int maxWeight;
    private boolean available;
    private DrivingLicence liceenceReq;
    private String plateNumber;

    public Truck(DrivingLicence dl, int maxWeight, String pn ){
        this.maxWeight = maxWeight;
        this.liceenceReq = dl;
        this.available =true; // inited as true
        this.plateNumber = pn;
    }
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

}

