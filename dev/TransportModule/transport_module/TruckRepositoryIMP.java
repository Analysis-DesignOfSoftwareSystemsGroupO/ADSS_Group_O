package TransportModule.transport_module;

import TransportModule.DTO.TruckDto;
import TransportModule.DataAccess.ITruckDAO;
import TransportModule.DataAccess.jdbcTruckDAO;
import TransportModule.Transport_Module_Exceptions.ATransportModuleException;
import Transport_Module_Exceptions.TruckNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class TruckRepositoryIMP implements  ITruckRepository{

    private static final Logger log =  LogManager.getLogger(TruckRepositoryIMP.class);

    private Map<String , Truck> mapper ;
    private static ITruckDAO truckDAO = new jdbcTruckDAO();

    public TruckRepositoryIMP() throws SQLException, ATransportModuleException {
        mapper = new HashMap<>();
        //fill mapper with Trucks
        List<TruckDto> truckDTOs = truckDAO.findAllTrucks();
        for(TruckDto tDTO :truckDTOs){
            Truck t = DTOtoTruck(tDTO); // also add this to the mapper/
        }

    }

    @Override
    public void addTruck(TruckDto truck) throws ATransportModuleException, SQLException {
        //Adding track to data base
        // if succesed :Add Truck to truckHashMap
        if(mapper.get(truck.getPlateNumber()) != null) return;
        truckDAO.save(truck);
        try {
            Truck t = getTruckBYPlateNumber(Integer.valueOf(truck.getPlateNumber()));
            mapper.put(t.getPlateNumber(), t);// add truck to mapper
        }
        catch (Exception e){
            truckDAO.deleteTruck(truck.getPlateNumber());
            throw e;
        }
        log.info("Added Succesfuly Truck : " + truck.getPlateNumber());
    }

    @Override
    public Truck getTruckBYPlateNumber(int pn) throws ATransportModuleException {
        if(mapper.get(pn ) != null) return mapper.get(pn);
        //if pn not found in the mapper
        try {
            Optional<TruckDto> truckDto = truckDAO.findByTruckPN(Integer.toString(pn)); //get Optional of truckDto from data base
            if(truckDto.isPresent()){
                Truck t = DTOtoTruck(truckDto.get());
                mapper.put( Integer.toString(pn) , t);
            }
            return null;
        }catch (SQLException e){
            log.error("SQLException in getTruckByPlateNumber");
            return null;
        }
    }

    @Override
    public void deleteTruck(String pn ) throws  SQLException {
        mapper.remove(pn);
        try {
            truckDAO.deleteTruck(pn);
        }
        catch (SQLException e){
            log.error("Failed to delete Truck: " + pn + "  due to SQL Exception ");
            throw e;
        }
    }

    /**
     * @param dto
     * @return A Truck instance, try to get it from the Mapper, if failes, from data base.
     * @throws ATransportModuleException
     */
    @Override
    public Truck DTOtoTruck(TruckDto dto) throws SQLException, ATransportModuleException {
        if(mapper.get(dto.getPlateNumber()) != null) return mapper.get(dto.getPlateNumber()); // get the Ttuck from the mapper.
        try {
            List<LocalDate> dates = truckDAO.getListofOccupiedDates(dto.getPlateNumber()); // load the dates of that the truck is occupied
            Truck t = new Truck(new DrivingLicence(dto.getLiceenceReq()), dto.getMaxWeight(),dto.getPlateNumber() , dates);
            mapper.put(t.getPlateNumber(), t);
            return  t;
        }
        catch (SQLException e){
            log.error("SQLException while trying to get the Trucks dates ,returning the trucks without the dates. ");
            throw e;
        }
    }

    @Override
    public TruckDto truckToDTO(Truck truck) {
        return new TruckDto(truck.getMaxWeight(), truck.getDrivingLicence().getCode(), truck.getPlateNumber()); // return a dto
    }

    /**
     * Set the date of a specific truck to unavialble.
     * @param date
     * @param plateNumber
     * @throws ATransportModuleException
     */
    @Override
    public void AssignDateToTruck(LocalDate date, String plateNumber) throws ATransportModuleException, SQLException {
        int pn = 0;
        try { //check that the plate number is valid
            pn = Integer.valueOf(plateNumber);
        }
        catch (NumberFormatException  e){
            log.error("Invalid number format for valueOf, Plate number should  be numeric");
            return;
        }
        try {
            Truck t = getTruckBYPlateNumber(pn);
            if(t == null) throw new TruckNotFoundException("Did not found Truck with this pn");;
            truckDAO.assignTruckToDate(plateNumber, date);
            t.setDate(date); //set the date as unavailable
        }
        catch (SQLException e){
            log.error("Failed to set the date if the truck as unavailable");
            throw e;
        }

    }
}