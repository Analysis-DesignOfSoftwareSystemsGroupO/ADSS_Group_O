package TransportModule.transport_module;

import TransportModule.DTO.DriverDto;

import java.util.List;

public class DriverControllerDomain {

    private IDriverRepository driverRepository;

    public DriverControllerDomain(){
        driverRepository = new DriverRepositoryIMP();

    }

    public void addDriver(String id, List<String> drivinglincesCodes) throws Exception{
        addDriverFromDto(new DriverDto(id,drivinglincesCodes));
    }

    public void addDriverFromDto(DriverDto driverDto) throws Exception{
        driverRepository.addNewDriver(driverDto);

    }

    public DriverDto getDriverById(String id) throws Exception{
        return driverRepository.getDriverById(id);
    }

    public void deleteDriverById(String id) throws Exception{
        driverRepository.deleteDriverById();
    }

    public Driver getDriverFromDTO(DriverDto dto) throws Exception{
        return driverRepository.getDriverFromDTO(dto);
    }


}
