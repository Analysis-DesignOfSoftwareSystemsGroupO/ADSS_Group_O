package TransportModule.transport_module;


import TransportModule.DTO.TransportDTO;
import TransportModule.DTO.TruckDto;
import TransportModule.Transport_Module_Exceptions.InvalidInputException;
import TransportModule.Transport_Module_Exceptions.TruckNotFoundException;


import java.util.ArrayList;
import java.util.List;



public class TruckControllerDomain  {

    private final ITruckRepository truckRepository;
    private  final ITransportRepository transportRepository;


    public TruckControllerDomain() throws Exception{
        truckRepository= TruckRepositoryIMP.getInstance();
        transportRepository = TransportRepositoryIMP.getInstance();
    }

    // For test section
    public TruckControllerDomain(ITruckRepository truckRepository, ITransportRepository transportRepository) {
        this.truckRepository = truckRepository;
        this.transportRepository = transportRepository;
    }

    public void addTruck(TruckDto truckDto) throws Exception{
        // send DTO to TruckRepositoryIMP
        if (truckDto == null)
            throw new InvalidInputException();
        truckRepository.addTruck(truckDto);


    }
    public List<TruckDto> getAllTrucks() throws Exception{
        List<Truck> trucks =  truckRepository.getAllTrucks();
        if(trucks == null)
            throw new TruckNotFoundException("No trucks to show");
        List<TruckDto> trucksDTOs = new ArrayList<>();

        for(Truck truck: trucks){
            trucksDTOs.add( truckRepository.truckToDTO(truck));
        }
        return trucksDTOs;

    }


    public void deleteTruck(String plate) throws Exception{
        if (plate.isEmpty())
            throw new InvalidInputException("Plate is empty");

        truckRepository.deleteTruck(plate);
    }

    public void assignTruckToTransport(int transportId, String plate) throws Exception {

        Transport transport = transportRepository.getTransportByid(transportId); // create a transport
        Truck truck = truckRepository.getTruckBYPlateNumber(plate); // create a truck
        transport.assignTruck(truck); // try to assign truck - if failed throw exception. otherwise, continue
        transportRepository.attachTrucktoTransport(transportId,plate);


    }
}