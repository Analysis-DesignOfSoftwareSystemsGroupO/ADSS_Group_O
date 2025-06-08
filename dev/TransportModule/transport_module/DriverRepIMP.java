package TransportModule.transport_module;

import TransportModule.DTO.DriverDto;
import TransportModule.DataAccess.IDriverDAO;
import TransportModule.DataAccess.jdbcDriverDAO;
import TransportModule.DataAccess.jdbcTransportDAO;
import TransportModule.Transport_Module_Exceptions.ATransportModuleException;
import TransportModule.Transport_Module_Exceptions.DriverMismatchException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.*;

public class DriverRepIMP implements IDriverRep{
    private static DriverRepIMP instance;
    private Map<String , Driver> map;
    private static int counter = 0;
    private IDriverDAO dao;
    private static final Logger log = LogManager.getLogger(DriverRepIMP.class);

    private DriverRepIMP() throws SQLException {
        this.map = new HashMap<>();
        this.dao = new jdbcDriverDAO();
        initAllDrivers();
    }

    public static DriverRepIMP  getInstance() throws SQLException {
        if(counter ==0 ){
            instance = new DriverRepIMP();
            counter++;
        }
        return  instance;
    }

    private void initAllDrivers() throws SQLException {
        List<String> driversIDs = dao.getAllDriversID();
        for (String id: driversIDs){
            getDriverByID(id);
        }
    }

    @Override
    public List<Driver> getAllDrivers() throws SQLException {
        List<String> driversIDs = dao.getAllDriversID(); //get all the ids of the drivers
        List<Driver> drivers = new ArrayList<>(); //init a list
        for (String id: driversIDs){ //for each id
            Driver d = getDriverByID(id);// get the driver
            drivers.add(d);
        }
        return drivers; //return the list
    }

    @Override
    public void save(DriverDto driverDto) throws SQLException , ATransportModuleException {
        Driver d = getDriverByID(driverDto.id());
        if(d != null) {
            throw new DriverMismatchException("Driver Already exsists");
        }
        dao.save(driverDto);
        getDriverByID(driverDto.id()); //will also put it on the mapper
    }

    @Override
    public void deleteDriver(String driverid) throws SQLException {
        dao.deleteDriver(driverid);
    }

    @Override
    public Driver getDriverByID(String id )throws SQLException{
        if(map.get(id)!= null) return map.get(id); // if Driver is presnented on the mapper
        else{ //try to look for it on the data base , if exsists, load to mapper
            DriverDto dto = dao.getDriverByID(id);
            if(dto == null)return null;
            ArrayList<DrivingLicence> licence = new ArrayList<>();
            for(String s : dto.drivingLicenceList()) // create Array list of Licences
                licence.add(new DrivingLicence(s));
            Driver d = new Driver(id, licence);
            map.put(d.getId(),d);
            return d;
        }
    }

    @Override
    public Driver convertDTOtoDriver(DriverDto dto) throws SQLException, ATransportModuleException {
        Driver d = getDriverByID(dto.id()); //check that both the driver and the dto has the same Driving licence
        List<String> codes = new ArrayList<>(); //create list of data licences codes
        for(DrivingLicence dl : d.getLicencs()){
            codes.add(dl.getCode());
        }
        boolean isEqual = new HashSet<>(codes).equals(new HashSet<>(dto.drivingLicenceList())); // check that both equals
        if (isEqual == false) throw new DriverMismatchException("Already exsists this driver with diffrent variables");
        return d;
    }

    @Override
    public void addLicenceToDriver(String id, String licence) throws SQLException, ATransportModuleException{
        //check that driver exsists
        Driver d = getDriverByID(id);
        if(d == null) throw new DriverMismatchException("Adding licence to not exsists driver ");
        dao.addLicenceToDriver(id, licence);
        d.addLicence("licence");
    }
}