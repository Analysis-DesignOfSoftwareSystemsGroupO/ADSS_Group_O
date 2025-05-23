package transport_module;

import DataAccess.DriverDto;
import Transport_Module_Exceptions.ATransportModuleException;
import Transport_Module_Exceptions.InvalidInputException;

import java.util.ArrayList;

public class DomainDriverController {


    public static Driver getDriverFromDto(DriverDto dto) throws ATransportModuleException {
        if (dto == null)
            throw new InvalidInputException();

        // Get all information from DTO
        String name = dto.getName();
        String id = dto.getId();
        String[] licenceStrings = dto.getLicence();

        // Create new list of Driving licence
        ArrayList<DrivingLicence> licences = new ArrayList<>();
        for (String licenceCode : licenceStrings) {
            licences.add(new DrivingLicence(licenceCode));
        }

        return new Driver(name, id, licences);
    }


    //create a DriverDto from Driver instance
    public static DriverDto makeDtoFromDriver(Driver driver) {

        int numOfDrivingLicncs = driver.getNumberOfDrivingLicencs();
        String[] drivinglicencsArray = new String[numOfDrivingLicncs];
        ArrayList<DrivingLicence> drivinglicencsArrayList = driver.getLicencs();
        for (int i = 0; i < numOfDrivingLicncs; i++) {
            drivinglicencsArray[i] = drivinglicencsArrayList.get(i).getCode();

        }

        return new DriverDto(driver.getName(), driver.getId(), drivinglicencsArray);
    }

    /**
     * @param dto recives Dto from Presentation Layer and add it to the system
     */
    public static void addNewDriver(DriverDto dto) throws ATransportModuleException { //todo also throws Data accsess Exception
        //todo : check if the Driver is already in the System
        //todo : maybe sent to data Base directly
        ArrayList<DrivingLicence> licences = new ArrayList<DrivingLicence>();
        for (String s : dto.getLicence()) {
            licences.add(new DrivingLicence(s));
        }
        Driver driver = new Driver(dto.getName(), dto.getId(), licences);
        //todo : Add to System
    }
}
