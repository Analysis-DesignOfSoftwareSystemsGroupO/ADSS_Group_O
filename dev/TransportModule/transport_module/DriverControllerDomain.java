package TransportModule.transport_module;

import TransportModule.DTO.DriverDto;

import java.util.ArrayList;
import java.util.List;

public class DriverControllerDomain {

    private final IDriverRep driverRepository;

    public DriverControllerDomain() throws Exception{
        driverRepository = DriverRepIMP.getInstance();

    }

    public void addDriver(String id, List<String> drivinglincesCodes) throws Exception{
        addDriverFromDto(new DriverDto(id,drivinglincesCodes));
    }

    public void addDriverFromDto(DriverDto driverDto) throws Exception{
        driverRepository.save(driverDto);

    }

    public DriverDto getDriverById(String id) throws Exception{
        Driver d = driverRepository.getDriverByID(id);
        ArrayList<DrivingLicence> licences = d.getLicencs();
        ArrayList<String> licencesStr = new ArrayList<>();
        for(DrivingLicence drivingLicence: licences){
            licencesStr.add(drivingLicence.getCode());
        }
        return new DriverDto(d.getId(),licencesStr);
    }

    public void deleteDriverById(String id) throws Exception{
        driverRepository.deleteDriver(id);
    }

    public Driver getDriverFromDTO(DriverDto dto) throws Exception{
        return driverRepository.convertDTOtoDriver(dto);
    }


}