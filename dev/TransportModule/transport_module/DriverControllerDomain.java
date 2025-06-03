package TransportModule.transport_module;

import TransportModule.DTO.DriverDto;

public class DriverControllerDomain {

    private IDriverRepository driverRepository;

    public DriverControllerDomain(){
        driverRepository = new DriverRepositoryIMP();

    }

    public void addDriver(DriverDto driverDto) throws Exception{
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
