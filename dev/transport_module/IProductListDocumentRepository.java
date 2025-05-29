package transport_module;

import DTO.ProductDTO;
import DTO.ProductListDocumentDto;
import Transport_Module_Exceptions.ATransportModuleException;
import Transport_Module_Exceptions.InvalidATransportException;
import Transport_Module_Exceptions.InvalidPLDException;
import Transport_Module_Exceptions.TransportMismatchException;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

public interface IProductListDocumentRepository {

    ProductListDocument getProductListDocumentByid(int id) throws SQLException, TransportMismatchException, InvalidATransportException;

    void saveProductListDocument(ProductListDocumentDto pld) throws ATransportModuleException, SQLException;

    void deleteProductListDocument(int id ) throws SQLException;

    ProductListDocument PLDdtoTOPLD(ProductListDocumentDto dto) throws InvalidPLDException, SQLException, InvalidATransportException, TransportMismatchException, ATransportModuleException;

    ProductListDocumentDto pldToDTO(ProductListDocument pld);

    List<ProductListDocument> getPLDByTransportID(int id) throws InvalidATransportException, TransportMismatchException;

    void setArriavleTime(int pldID, LocalTime time) throws SQLException , ATransportModuleException;

    int getValidID();
}
