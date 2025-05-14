package transport_module;

import DataAccess.DriverDto;
import Transport_Module_Exceptions.ATransportModuleException;

import java.util.ArrayList;

public class DomainDriverController {


    public static Driver getDriverFromDto(DriverDto dto ){
        //todo
    }

    //create a DriverDto from Driver instance
    public static DriverDto makeDtoFromDriver(Driver driver){
        return new DriverDto(driver.getName(), driver.getId(), (String[]) driver.getLicencs().toArray());
    }

    /**
     *
     * @param dto recives Dto from Presentation Layer and add it to the system
     */
    public static void addNewDriver(DriverDto dto)throws ATransportModuleException { //todo also throws Data accsess Exception
        //todo : check if the Driver is already in the System
        //todo : maybe sent to data Base directly
        ArrayList<DrivingLicence> licences= new ArrayList<DrivingLicence>();
        for(String s : dto.getLicence()){
            licences.add(new DrivingLicence(s));
        }
        Driver driver = new Driver(dto.getName(), dto.getId(), licences);
        //todo : Add to System
    }
}
