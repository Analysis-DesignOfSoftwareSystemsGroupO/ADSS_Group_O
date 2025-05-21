package transport_module;

import DataAccess.ProductListDocumentDto;
import Transport_Module_Exceptions.ATransportModuleException;

public class PLDRepositoryIMP implements IProductListDocumentRepository {


    @Override
    public ProductListDocument getProductListDocumentByid(int id) throws ATransportModuleException {
        return null;
    }

    @Override
    public void saveProductListDocument(ProductListDocumentDto pldDTO) throws ATransportModuleException {

    }

    @Override
    public void deleteProductListDocument(ProductListDocument pldDTO) throws ATransportModuleException {

    }
}
