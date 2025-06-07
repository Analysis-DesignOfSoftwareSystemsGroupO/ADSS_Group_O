package TransportModule.transport_module;

import TransportModule.DTO.DriverDto;
import TransportModule.DTO.TransportDTO;
import TransportModule.DataAccess.ITransportDAO;
import TransportModule.DataAccess.jdbcTransportDAO;
import TransportModule.Transport_Module_Exceptions.ATransportModuleException;
import TransportModule.Transport_Module_Exceptions.InvalidATransportException;
import TransportModule.Transport_Module_Exceptions.TransportMismatchException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class TransportRepositoryIMP implements ITransportRepository {
    private static final Logger log =  LogManager.getLogger(TransportRepositoryIMP.class);
    private HashMap<Integer, Transport> transports;
    private static ITransportDAO dao = new jdbcTransportDAO();
    private int availableId;
    private static ITruckRepository truckRepository;
    private static TransportRepositoryIMP instance;
    private static  int counter =0 ;
    private  IProductListDocumentRepository pldRep;
    private IDriverRep driverRep ;

    /**
     *
     * @param id
     * @return transport object or null if not exsists
     * @throws ATransportModuleException
     */
    @Override
    public Transport getTransportByid(int id) throws SQLException, ATransportModuleException {
        if( transports.get(id ) == null){ // if the transport is not in the mapper, look for it in the data base
            try {
                Optional<TransportDTO> transportDTO = dao.getTransportByid(id);
                if(transportDTO.isPresent()){
                    TransportDTO dto = transportDTO.get();
                    DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    Site s = null;
                    if(dto.getSiteName() != null) {
                        s = new Site(dto.getSiteName().trim(), "DefaultArea"); // todo Area feature is posposed
                    }

                    //Get the time by String
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                    String time = timeFormatter.format(dto.getDepartureTime());
                    Transport t = new Transport(dto.getId(),dateformatter.format(dto.getDate()), time ,s);
                    t.setMaxWeight(dto.getMaxWeight());
                    if(dto.getTruckPN() != null){
                        Truck truck = truckRepository.getTruckBYPlateNumber(dto.getTruckPN());
                        t.assignTruck(truck);
                    }
                    if(dto.getDriverID() != null) { //assignDriver to transport
                        Driver driver = driverRep.getDriverByID(dto.getDriverID().trim()); //throw exception if driver not exsists
                        t.addDriver(driver);
                    }
                    List<ProductListDocument> plds = pldRep.getPLDByTransportID(t.getId());
                    for (ProductListDocument pld : plds)
                        t.loadByDocument(pld);
                    transports.put(t.getId(), t);
                    return t;
                }
                else {return null;}
            }
            catch (SQLException e){
                log.error("SQL exception in getTransportById()");
                throw e;
            }
            catch (ATransportModuleException e){
                throw e;
            }
        }
        return transports.get(id);
    }

    @Override
    public int getAvailableid() {
        availableId++;
        return availableId;
    }

    @Override
    public List<Transport> getTransportsByDate(LocalDate date) throws SQLException, ATransportModuleException {
        List<TransportDTO> transportsDTO =  getTransportsDTOByDate(date);
        List<Transport> transports = new ArrayList<>();
        for(TransportDTO tDTO :transportsDTO){ // for each DTO , if finds it ,add to transports list and return
            Transport t = TransportDTOtoTransport(tDTO);
            if(t != null) transports.add(t);
        }
        return transports;
    }
    //
    @Override
    public List<TransportDTO> getTransportsDTOByDate(LocalDate date)throws SQLException{
        List<TransportDTO > transportDTOS = dao.getTransportsByDate(date);//get DTO of all transports that day
        return transportDTOS;
    }

    @Override
    public void attachTrucktoTransport(int transportId, String pn) throws SQLException, ATransportModuleException {
        try {
            Transport t = getTransportByid(transportId); //remove truck from Transport
            dao.assignTruckToTransport(transportId, Integer.parseInt(pn));
            truckRepository.AssignDateToTruck(t.getDate(),pn);
        }
        catch (Exception e){
            Transport t = getTransportByid(transportId); //remove truck from Transport
            t.assignTruck(null);
            throw e ;
        }

    }

    @Override
    public void saveTransport(TransportDTO transport) throws ATransportModuleException ,SQLException{
        if(getTransportByid(transport.getId()) != null ) throw new InvalidATransportException("This Transport already exsists");
        dao.save(transport); // save this in the Data Base
        transports.put(transport.getId(), TransportDTOtoTransport(transport)); //put the new transport in the mapper
    }

    @Override
    public void deleteTransport(int  transportID) throws SQLException {
        if(transportID == -1 )throw new  RuntimeException();
        dao.deleteTransport(transportID); //remove record from data base
        transports.remove(transportID);  //remove transport from mapper
    }

    /**
     *
     * @param dto Dto of Transport
     * @return Transport instance
     * @throws SQLException
     * @throws TransportMismatchException if dto is miss match with the transport instance, throws an exception
     */
    @Override
    public Transport TransportDTOtoTransport(TransportDTO dto) throws SQLException, ATransportModuleException {
        Transport t = getTransportByid(dto.getId());
        if(t.getDate().equals( dto.getDate())   && t.getMaxWeight() == dto.getMaxWeight()){
            if((t.getDriver() == null && dto.getDriverID() ==null )|| t.getDriver().getId() == dto.getDriverID()){
                if((t.getSource().getName() == null && dto.getSiteName() == null) || t.getSource().getName().trim().equals(dto.getSiteName().trim() ))
                return t;
            }
        }
        throw new TransportMismatchException("Miss match data");
    }

    @Override
    public TransportDTO transportToTransportDTO(Transport transport) {
        return new TransportDTO(transport.getId(), transport.getDate(), transport.isSent(), transport.getMaxWeight() ,transport.getDriver().getId(), transport.getTruck().getPlateNumber(),transport.getSource().getName(), transport.getDeparture_time() );
    }



    private TransportRepositoryIMP() throws SQLException, ATransportModuleException {
        this.availableId = dao.getHieghestTransportID();
        truckRepository = TruckRepositoryIMP.getInstance();

        driverRep = DriverRepIMP.getInstance();
        //set the mapper and fill it with transports:
        this.transports = new HashMap<>();
        //Add Transport with id -1 if not exsists


    }

    public void initRep() throws SQLException, ATransportModuleException {
        List<TransportDTO> transportDTOS = dao.getTransports();
        if(getTransportByid(-1) ==null) {
            TransportDTO tdto0 = new TransportDTO(-1, LocalDate.of(9999, 12, 31), false, 0, null, null, null, LocalTime.of(23, 59));
            dao.save(tdto0);
            Transport t = new Transport(-1,"31/12/9999","23:59",null);
            transports.put(-1,t);
        }
        for (TransportDTO dto : transportDTOS){ //for each transport dto
            Transport t = TransportDTOtoTransport(dto); // convert dto to Transport Instance , also put on the mapper and list
        }

    }

    public static TransportRepositoryIMP getInstance() throws SQLException, ATransportModuleException {
        if(counter == 0){
            instance = new TransportRepositoryIMP();
            counter++;
        }
        return instance;
    }

    @Override
    public void injectPLDRepository(IProductListDocumentRepository pldRep) {
        this.pldRep = pldRep;

    }

    @Override
    public void deleteAll()throws SQLException{
        dao.deleteAll();
    }

    @Override
    public List<Transport> getAllTransports() throws SQLException, ATransportModuleException {
        List<Transport> transportsList = new ArrayList<>();
        List<Integer> tIDs = dao.getIDs(); //get all transports Dtos
        for(Integer id : tIDs){
            transportsList.add(getTransportByid(id));
        }
        return transportsList;
    }

    @Override
    public void updateTransport(TransportDTO Tdto) throws SQLException, ATransportModuleException {
        Transport t = getTransportByid(Tdto.getId());
        if(t.isSent() != Tdto.isSent()){ //update is sent
            setSent(Tdto.getId());
        }
        if(Tdto.getDriverID() != null){ //update driver field
            if(t.getDriver() == null){
                setDriver(t.getId(), Tdto.getDriverID());
            }
            else { //t has a driver
                if(!t.getDriver().getId().equals(Tdto.getDriverID())){ //if driver id in dto and in driver are diffrent
                    setDriver(t.getId(), Tdto.getDriverID());
                }
            }
        }
        if(Tdto.getTruckPN() != null){ //update Truck
            if(t.getTruck() == null){
                attachTrucktoTransport(t.getId(), Tdto.getTruckPN());
            }
        }
    }

    @Override
    public void setSent(int transportID ) throws SQLException, ATransportModuleException {
        Transport t = getTransportByid(transportID);
        t.sendTransport();
        dao.setSent(transportID);
    }

    @Override
    public void setDriver(int transportID, String driverID) throws ATransportModuleException, SQLException {
        Transport t = getTransportByid(transportID);
        Driver d = driverRep.getDriverByID(driverID);
        t.addDriver(d);
        dao.setDriver(transportID, driverID);
    }


}