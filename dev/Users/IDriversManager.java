/**
package Users;

import transport_module.Driver;
import transport_module.DrivingLicence;
import java.util.ArrayList;
import java.util.Date;

public interface IDriversManager {

    /**
     *
     * @param drivers todo : change it so it will add the driver to the data base
     * @param name
     * @param id
     * @param licencs
     */
/**
    default void addNewDriver(Driver[] drivers, String name, String id, ArrayList<DrivingLicence> licencs){
        //todo : add driver to data base

        Driver driver = new Driver( name, id, licencs);
        drivers[drivers.length-1] = driver;
    };

    default ArrayList<Driver> getAllDrivers(){
        //todo: make a data base and return all drivers from the data base
        return null;
    }

    default  Driver getAvailableDriver(Date date){
    //todo
    }

    default Driver getDriver(String id){
        //todo
    }

    




}
**/
