package transport_module;

import Transport_Module_Exceptions.ATransportModuleException;

import java.util.Date;

public class TransportRepositoryIMP implements ITransportRepository{
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
    public void deleteTransport(Transport transport) throws ATransportModuleException {

    }

    public TransportRepositoryIMP(){}


}
