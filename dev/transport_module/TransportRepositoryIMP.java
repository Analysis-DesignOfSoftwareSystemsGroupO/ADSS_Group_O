package transport_module;

import DTO.TransportDTO;
import DataAccess.ITransportDAO;
import DataAccess.jdbcTruckDAO;
import Transport_Module_Exceptions.ATransportModuleException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;


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
    public Transport getTransportByid(int id) throws ATransportModuleException {
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
            }
        }
        return transports.get(id);
    }

    @Override
    public Transport[] getTransportsByDate(Date date) {
        return new Transport[0];
    }

    @Override
    public void saveTransport(TransportDTO transport) throws ATransportModuleException {

    }

    @Override
    public void deleteTransport(int  transportID) throws ATransportModuleException {

    }

    @Override
    public Transport TransportDTOtoTransport(TransportDTO dto) {
        return null;
    }

    @Override
    public TransportDTO transportToTransportDTO(Transport transport) {
        return new TransportDTO(transport.getId(), transport.getDate(), transport.isSent(), transport.getmaxWeight(),transport.getDriver().getId(), transport.getTruck().getPlateNumber(),transport.getSourceSiteName(), transport.getDeparture_time() );
    }

    public TransportRepositoryIMP(){
        this.transports = new HashMap<>();
    }


}
