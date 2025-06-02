package TransportModule.DataAccess;

import TransportModule.DTO.ProductDTO;
import TransportModule.DTO.ProductListDocumentDto;
import TransportModule.transport_module.ProductListDocument;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface IPLDDAO {
    void save(ProductListDocumentDto dto ) throws SQLException;
    void deletePLD(int serialNumber ) throws  SQLException;
    int getHieghestPLDID() throws  SQLException;
    Optional<ProductListDocumentDto> findByPLDID(int id) throws  SQLException;
    List<Integer> findByTransport(int Tid) throws SQLException;
    List<ProductDTO> getListOfProductsByPLDID(int pldID)throws  SQLException;
    void setArriavleTime(int pldID, LocalTime time) throws SQLException;
    List<Integer> getPLDsID() throws SQLException;
    void deleteAll() throws SQLException;
}
