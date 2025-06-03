package TransportModule.Presentation;

import TransportModule.DTO.DriverDto;


import TransportModule.transport_module.DriverControllerDomain;

public class DriverControllerPL {

    private final DriverControllerDomain controller;

    public DriverControllerPL(){
        this.controller = new DriverControllerDomain();
    }


    public void addDriver(DriverDto driverDto) throws Exception{
        controller.addDriver(driverDto);

    }

    public DriverDto getDriverById(String id) throws Exception{
        return controller.getDriverById(id);
    }

    public void deleteDriverById(String id) throws Exception{
        controller.deleteDriverById(id);
    }

}
