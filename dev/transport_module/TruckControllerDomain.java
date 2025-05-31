package transport_module;

import DTO.*;

import Transport_Module_Exceptions.*;


import java.util.List;



public class TruckControllerDomain {

    private static ITruckRepository truckRepository= new TruckRepositoryIMP();
    private static ITransportRepository transportRepository = new TransportRepositoryIMP() {
    };

    public TruckControllerDomain(){

    }
    /** A function that creates truck instance from Truck DTO*/
    public static Truck getTruckFromDto(TruckDto truckDto) throws Exception{
        // send DTO to TruckRepositoryIMP

        if( truckDto == null)
            throw new InvalidInputException();

        // Get all information from DTO
        int weight = truckDto.getWeight();
        int maxWeight = truckDto.getMaxWeight();
        String liceenceReq = truckDto.getLiceenceReq();
        String plateNumber = truckDto.getPlateNumber();

        return truckRepository.getTruckBYPlateNumber(plateNumber);

    }
    public static void addTruck(TruckDto truckDto) throws Exception{
        // send DTO to TruckRepositoryIMP
        if (truckDto == null)
            throw new InvalidInputException();
        truckRepository.addTruck(truckDto);


    }
    public static TruckDto[] getAllTrucks() throws Exception{
        List<Truck> trucks =  truckRepository.getAllTrucks();

        return turnTruckListToTruckDTOArray(trucks);
    }

    private static TruckDto[] turnTruckListToTruckDTOArray(List<Truck> trucks) throws Exception{
        TruckDto[] truckDtos = new TruckDto[trucks.size()];
        int i=0;
        for(Truck truck: trucks){
            truckDtos[i++] = makeDtoFromTruck(truck);
        }
        return truckDtos;
    }

    /** A function that creates DTO from truck*/
    public static TruckDto makeDtoFromTruck(Truck truck) throws Exception{
        if (truck == null )
            throw new InvalidInputException();
        // Create and return a DTO with trucks arguments
        return new TruckDto(truck.getMaxWeight(),truck.getDrivingLicence().getCode(), truck.getPlateNumber());
    }



    public static void deleteTruck(String plate) throws Exception{
        if (plate.isEmpty())
            throw new InvalidInputException("Plate is empty");

        truckRepository.deleteTruck(plate);
    }

    public static void assignTruckToTransport(int transportId, int plate) throws Exception {

        Transport transport = transportRepository.getTransportByid(transportId); // create a transport
        Truck truck = truckRepository.getTruckBYPlateNumber(plate); // create a truck
        transport.assignTruck(truck); // try to assign truck - if failed throw exception. otherwise, continue
        TransportDTO transportDTO = new  TransportDTO(transport.getId(),transport.getDate().toString(), transport.isSent(), transport.getMaxWeight(),-1,Integer.parseInt(truck.getPlateNumber()),transport.getSource().getName());
        transportRepository.saveTransport(transportDTO);


    }
}
