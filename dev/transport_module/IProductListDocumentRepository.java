package transport_module;

import DataAccess.ProductListDocumentDto;
import Transport_Module_Exceptions.ATransportModuleException;

public interface IProductListDocumentRepository {

    ProductListDocument getProductListDocumentByid(int id) throws ATransportModuleException;

    void saveProductListDocument(ProductListDocument pld) throws ATransportModuleException ;

    void deleteProductListDocument(ProductListDocument pld ) throws  ATransportModuleException;

}
