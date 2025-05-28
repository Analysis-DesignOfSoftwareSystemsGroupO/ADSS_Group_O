package transport_module;

import DTO.TruckDto;
import DataAccess.ITruckDAO;
import Transport_Module_Exceptions.ATransportModuleException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class TruckRepositoryIMP implements  ITruckRepository{

    private static final Logger log =  LogManager.getLogger(TruckRepositoryIMP.class);

    private HashMap<String , Truck> trucksHashMap ;
    private ITruckDAO truckDAO;

    @Override
    public void addTruck(TruckDto truck) throws ATransportModuleException {
        //Adding track to data base
        // if succesed :Add Truck to truckHashMap
        if(trucksHashMap.get(truck.getPlateNumber()) != null) return;
        try {
            truckDAO.save(truck);
            log.info("Added Succesfuly Truck : " + truck.getPlateNumber());
        }catch (SQLException e){
            log.error("Failed To add the truck to the system due to SQL Exception ");
            return;
        }


    }

    @Override
    public Truck getTruckBYPlateNumber(int pn) throws ATransportModuleException {
        if(trucksHashMap.get(pn ) != null) return trucksHashMap.get(pn);
        //if pn not found in the mapper
        try {
            Optional<TruckDto> truckDto = truckDAO.findByTruckPN(Integer.toString(pn)); //get Optional of truckDto from data base
            if(truckDto.isPresent()){
                return DTOtoTruck(truckDto.get());
            }
            return null;
        }catch (SQLException e){
            log.error("SQLException in getTruckByPlateNumber");
            return null;
        }
    }

    @Override
    public void deleteTruck(String pn ) throws ATransportModuleException {
        trucksHashMap.remove(pn);
        try {
            truckDAO.deleteTruck(pn);
        }
        catch (SQLException e){
            log.error("Failed to delete Truck: " + pn + "  due to SQL Exception ");
        }
    }

    /**
     * @param dto
     * @return A Truck instance, try to get it from the Mapper, if failes, from data base.
     * @throws ATransportModuleException
     */
    @Override
    public Truck DTOtoTruck(TruckDto dto) throws ATransportModuleException {
        if(trucksHashMap.get(dto.getPlateNumber()) != null) return trucksHashMap.get(dto.getPlateNumber()); // get the Ttuck from the mapper.
        HashMap<LocalDate, Boolean> availability = new HashMap<>();
        try {
            List<LocalDate> dates = truckDAO.getListofOccupiedDates(dto.getPlateNumber()); // load the dates of that the truck is occupied
            Truck t = new Truck(new DrivingLicence(dto.getLiceenceReq()), dto.getMaxWeight(),dto.getPlateNumber() , dates);
            trucksHashMap.put(t.getPlateNumber(), t);
            return  t;
        }
        catch (SQLException e){
            log.error("SQLException while trying to get the Trucks dates ,returning the trucks without the dates. ");

        }
        Truck t = new Truck(new DrivingLicence(dto.getLiceenceReq()), dto.getMaxWeight(),dto.getPlateNumber());
        return t;
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
    public void AssignDateToTruck(LocalDate date, String plateNumber) throws ATransportModuleException {
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
            if(t == null) return;
            truckDAO.assignTruckToDate(plateNumber, date);
            t.setDate(date); //set the date as unavailable
        }
        catch (SQLException e){
            log.error("Failed to set the date if the truck as unavailable");
        }

    }
}
