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
    public boolean setWeight(int add_w){
        if(weight + add_w > maxWeight){return false;}
        weight = weight - add_w;
        if(weight < 0){ weight =0; } //todo : Think if it os better to set the value to 0 or to provoke an Error
        return true;
    }

    /**
     *  after importing the Item Class from the inventory module
     * @param i the item to add to the truck
     * @return true if the item added validly.
     */
    public boolean addItem(Item i){
        return setWeight(i.getWieght());
    }
    /**
     *  after importing the Item Class from the inventory module
     * @param i the item to drop from the trcuk
     * @return true if the item drop successfuly.
     */
    public boolean dropItem(Item i){
        return setWeight(-i.getWeight());
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

}

