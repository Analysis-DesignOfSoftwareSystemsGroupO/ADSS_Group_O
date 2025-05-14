package transport_module;

import DataAccess.DriverDto;
import Transport_Module_Exceptions.ATransportModuleException;

import java.util.ArrayList;

public abstract class DomainController {

    public static Driver CreateDriverFromDto(DriverDto dto ){}

    public static Truck CreateTruckFromDto(TruckDto truckDTO){}

    public static ProductListDocument createPLDFromDTO(ProductListDocumentDTO dto){}

    /**
     *
     * @param dto recives Dto from Presentation Layer and add it to the system
     */
    public static void addNewDriver(DriverDto dto)throws ATransportModuleException { //todo also throws Data accsess Exception
        ArrayList<DrivingLicence>
        Driver driver = new Driver(dto.getName(),)
    }
}
