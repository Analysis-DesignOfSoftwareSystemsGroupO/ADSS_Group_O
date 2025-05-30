package transport_module;

import DTO.TransportDTO;
import DataAccess.ITransportDAO;
import DataAccess.jdbcTransportDAO;
import DataAccess.jdbcTruckDAO;
import Transport_Module_Exceptions.ATransportModuleException;
import Transport_Module_Exceptions.InvalidATransportException;
import Transport_Module_Exceptions.TransportMismatchException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;


public class TransportRepositoryIMP implements ITransportRepository{
    private static final Logger log =  LogManager.getLogger(TransportRepositoryIMP.class);
    private HashMap<Integer, Transport> transports;
    private static ITransportDAO dao = new jdbcTransportDAO();
    private int availableId;

    /**
     *
     * @param id
     * @return transport object or null if not exsists
     * @throws ATransportModuleException
     */
    @Override
    public Transport getTransportByid(int id) throws SQLException {
        if( transports.get(id ) == null){ // if the transport is not in the mapper, look for it in the data base
            try {
                Optional<TransportDTO> transportDTO = dao.getTransportByid(id);
                if(transportDTO.isPresent()){
                    //todo : Posposed because need to update Transport
                    Transport t = new Transport()
                    transports.put(t.getId(), t);
                    return t;
                }
                else {return null;}
            }
            catch (SQLException e){
                log.error("SQL exception in getTransportById()");
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
    public List<Transport> getTransportsByDate(LocalDate date) throws SQLException {
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
    public void saveTransport(TransportDTO transport) throws ATransportModuleException ,SQLException{
        if(getTransportByid(transport.getId()) != null ) throw new InvalidATransportException("This Transport already exsists");
        dao.save(transport); // save this in the Data Base
        transports.put(transport.getId(), TransportDTOtoTransport(transport)); //put the new transport in the mapper
    }

    @Override
    public void deleteTransport(int  transportID) throws SQLException {
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
    public Transport TransportDTOtoTransport(TransportDTO dto) throws SQLException, TransportMismatchException {
        Transport t = getTransportByid(dto.getId());
        if(t.getDate() == dto.getDate() && t.getSourceSiteName() == dto.getSiteName() && t.getmaxWeight() == dto.getMaxWeight()){
            if((t.getDriver() == null && Integer.valueOf(dto.getDriverID()) != -1 )|| t.getDriver().getId() == dto.getDriverID()){
                throw new TransportMismatchException("Miss match data");
            }
            return t;
        }
        throw new TransportMismatchException("Miss match data");
    }

    @Override
    public TransportDTO transportToTransportDTO(Transport transport) {
        return new TransportDTO(transport.getId(), transport.getDate(), transport.isSent(), transport.getmaxWeight(),transport.getDriver().getId(), transport.getTruck().getPlateNumber(),transport.getSourceSiteName(), transport.getDeparture_time() );
    }



    public TransportRepositoryIMP() throws SQLException, TransportMismatchException {
        this.availableId = dao.getHieghestTransportID() + 1;
        //set the mapper and fill it with transports:
        this.transports = new HashMap<>();
        List<TransportDTO> transportDTOS = dao.getTransports();
        List<Transport> transportsList = new ArrayList<>();
        for (TransportDTO dto : transportDTOS){ //for each transport dto
            Transport t = TransportDTOtoTransport(dto); // convert dto to Transport Instance
            transports.put(t.getId(), t); //put in the mapper
        }

    }

}
