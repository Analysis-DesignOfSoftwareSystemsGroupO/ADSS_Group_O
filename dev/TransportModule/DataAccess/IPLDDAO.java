package TransportModule.DataAccess;

import TransportModule.DTO.ProductDTO;
import TransportModule.DTO.ProductListDocumentDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IPLDDAO {
    void save(ProductListDocumentDto dto ) throws SQLException;
    void deletePLD(int serialNumber ) throws  SQLException;
    int getHieghestTransportID() throws  SQLException;
    Optional<ProductListDocumentDto> findByPLDID(int id) throws  SQLException;
    List<ProductListDocumentDto> findByTransport(int Tid) throws SQLException;
    List<ProductDTO> getListOfProductsByPLDID(int pldID)throws  SQLException;
    List<Integer> quantetyOfProductsbyPLDID(int pldID)throws SQLException;

}
