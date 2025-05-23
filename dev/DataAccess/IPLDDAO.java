package DataAccess;

import DTO.ProductListDocumentDto;
import transport_module.ProductListDocument;

import java.sql.SQLException;

public interface IPLDDAO {
    ProductListDocumentDto save(ProductListDocumentDto dto ) throws SQLException;
    void deletePLD(int serialNumber ) throws  SQLException;

}
