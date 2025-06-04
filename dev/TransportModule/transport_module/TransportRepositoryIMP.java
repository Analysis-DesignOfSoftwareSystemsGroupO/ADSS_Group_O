package TransportModule.transport_module;

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
    private static IProductListDocumentRepository pldRep;
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
                    Site s = new Site(dto.getSiteName(), "DefaultArea"); // Area feature is posposed
                    //Get the time by String
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                    String time = timeFormatter.format(dto.getDepartureTime());
                    Transport t = new Transport(dto.getId(),dateformatter.format(dto.getDate()), time ,s);
                    t.addDriver(new Driver(,t.getDriver().getEmpId()));  //todo integration with driver Table
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
        if(t.getDate().equals( dto.getDate()) && t.getSource().getName() == dto.getSiteName() && t.getMaxWeight() == dto.getMaxWeight()){
            if((t.getDriver() == null && dto.getDriverID() ==null )|| t.getDriver().getId() == dto.getDriverID()){
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
        this.availableId = dao.getHieghestTransportID() + 1;
        truckRepository = TruckRepositoryIMP.getInstance();
        pldRep = PLDRepositoryIMP.getInstance();
        //set the mapper and fill it with transports:
        this.transports = new HashMap<>();
        List<TransportDTO> transportDTOS = dao.getTransports();
        List<Transport> transportsList = new ArrayList<>();
        for (TransportDTO dto : transportDTOS){ //for each transport dto
            Transport t = TransportDTOtoTransport(dto); // convert dto to Transport Instance , also put on the mapper

        }
        //Add Transport with id -1 if not exsists
        if(getTransportByid(-1) ==null) {
            TransportDTO tdto0 = new TransportDTO(-1, LocalDate.of(9999, 12, 31), false, 0, null, null, null, LocalTime.of(23, 59));
            saveTransport(tdto0);
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
    public void deleteAll()throws SQLException{
        dao.deleteAll();
    }
}
