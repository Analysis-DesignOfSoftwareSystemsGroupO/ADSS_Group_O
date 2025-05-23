package DataAccess;

import DTO.ProductListDocumentDto;

import java.sql.SQLException;

public class jdbcPLDDAO implements IPLDDAO{
    @Override
    public ProductListDocumentDto save(ProductListDocumentDto dto) throws SQLException {
        return null;
    }

    @Override
    public void deletePLD(int serialNumber) throws SQLException {

    }
}
