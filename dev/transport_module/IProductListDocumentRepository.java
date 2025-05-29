package transport_module;

import DTO.ProductDTO;
import DTO.ProductListDocumentDto;
import Transport_Module_Exceptions.ATransportModuleException;
import Transport_Module_Exceptions.InvalidPLDException;

import java.sql.SQLException;
import java.util.List;

public interface IProductListDocumentRepository {

    ProductListDocument getProductListDocumentByid(int id) ;

    void saveProductListDocument(ProductListDocumentDto pld) throws ATransportModuleException  ;

    void deleteProductListDocument(int id ) ;

    ProductListDocument PLDdtoTOPLD(ProductListDocumentDto dto) throws InvalidPLDException;

    ProductListDocumentDto pldToDTO(ProductListDocument pld);

    List<ProductListDocument> getPLDByTransportID(int id);
}
