package transport_module;

import DTO.TransportDTO;
import DataAccess.ITransportDAO;
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
    HashMap<Integer, Transport> transports;
    ITransportDAO dao;

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
                    Transport t = TransportDTOtoTransport(transportDTO.get()); //get the transport Object from Dto
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
    public void deleteTransport(int  transportID) throws ATransportModuleException {

    }

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

    public TransportRepositoryIMP(){
        this.transports = new HashMap<>();
    }


}
