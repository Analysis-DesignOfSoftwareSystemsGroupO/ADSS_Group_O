package transport_module;

import DTO.TransportDTO;
import Transport_Module_Exceptions.ATransportModuleException;

import java.util.Date;
import java.util.HashMap;

public class TransportRepositoryIMP implements ITransportRepository{

    HashMap<Integer, Transport> transports;

    @Override
    public Transport getTransportByid(int id) throws ATransportModuleException {
        return null;
    }

    @Override
    public Transport[] getTransportsByDate(Date date) {
        return new Transport[0];
    }

    @Override
    public void saveTransport(Transport transport) throws ATransportModuleException {

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
