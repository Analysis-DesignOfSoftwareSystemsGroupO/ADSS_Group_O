package TransportModule.transport_module;

import TransportModule.DTO.ProductDTO;
import TransportModule.DTO.ProductListDocumentDto;
import TransportModule.DataAccess.IPLDDAO;
import TransportModule.DataAccess.jdbcPLDDAO;
import TransportModule.Transport_Module_Exceptions.ATransportModuleException;
import TransportModule.Transport_Module_Exceptions.InvalidATransportException;
import TransportModule.Transport_Module_Exceptions.InvalidPLDException;
import TransportModule.Transport_Module_Exceptions.TransportMismatchException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PLDRepositoryIMP implements IProductListDocumentRepository {

    private Map<Integer , ProductListDocument> mapper;
    private static IPLDDAO dao = new jdbcPLDDAO();
    private static final Logger log = LogManager.getLogger(PLDRepositoryIMP.class);
    private  ITransportRepository transportRep;
    private static PLDRepositoryIMP instance;
    private static int counter =0;



    private int availableid;

    private PLDRepositoryIMP() throws SQLException, ATransportModuleException {
        this.availableid = initValidid(); //init the availableID field
        this.mapper = new HashMap<>();


    }
    public void initRep() throws SQLException, InvalidATransportException, TransportMismatchException {
        //fill the mapper with pld instances:
        List<Integer> pldIDs = dao.getPLDsID(); //get id of plds from the data base
        for (int id : pldIDs){ //for each id: get the ProductListDocument instance and add it to the mapper
            ProductListDocument pld = getProductListDocumentByid(id); // This function will add the PLD to the mapper.
        }
    }

    public static PLDRepositoryIMP getInstance() throws SQLException, ATransportModuleException {
        if(counter ==0 ) {
            counter++;
            instance = new PLDRepositoryIMP( );
        }
        return instance;
    }

    public void injectTransportRepository(ITransportRepository rep){
        this.transportRep = rep;
    }

    public int getValidID(){
        availableid ++;
        return availableid;}

    @Override
    public List<ProductListDocument> getPLDwithOutTransport() throws SQLException, InvalidATransportException, TransportMismatchException {
        return getPLDByTransportID(-1);
    }

    int initValidid()throws SQLException{
        availableid = dao.getHieghestPLDID() + 1;
        return availableid;
    }


    @Override
    public ProductListDocument getProductListDocumentByid(int id) throws SQLException, TransportMismatchException, InvalidATransportException { //todo
        if (mapper.get(id) != null)
            return mapper.get(id); //return PLD if exsists in the mapper. else, Look for it in the data base
        ProductListDocument pld = null;
        try {
            Optional<ProductListDocumentDto> optPLD = dao.findByPLDID(id);
            if(!optPLD.isPresent())return null; // if did not find the PLD in data Base nor in the mapper
            //Creating the ProductList instance and add it to the repository mapper.
            ProductListDocumentDto dto = optPLD.get();
            Site site = new Site(dto.getSiteDes().trim(), "Default Area "); // todo : This feature of the area is posposed and will be implemented later. Meanwhile the Area is Deafault
            LocalDate date = dto.getDate();
            DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalTime time = dto.getApproximatedArrivalTime();
            DateTimeFormatter df2 = DateTimeFormatter.ofPattern("HH:mm");
            pld = new ProductListDocument(id,site, dateformatter.format(date), df2.format(time));
            if(dto.getTransportID() != -1) { // -1 is the deafault TransportID in the data base. means that this PLD is not attached to any of the transports
                Transport t = transportRep.getTransportByid(dto.getTransportID());
                if (t != null) pld.attachTransportToDocument(t);
                else {
                    throw new InvalidATransportException("The transport id of this PLD is not set to -1, and there is no Transport with this id. ");
                }
            }
            List<ProductDTO > productsdtos = dao.getListOfProductsByPLDID(id); //get all the products of this PLD
            for(ProductDTO pdto : productsdtos){
                //todo: Integration with Products is posposed. later will get the products from Products moudle. right now it is presented like this
                Product p = productDTOtoProduct(pdto);
                pld.addProduct(p, pdto.quantity()); //Add the product with the quantity to the ProductListDocument
            }
        }
        catch (SQLException e ) {
            log.error("SQL Exception in findByPLDID");
            throw e;
        } catch (InvalidATransportException e) {
            log.error("Failed to find the transport of the PLD. InvalidATransportException catched with message: \" " + e.getMessage() + "\" . Deleting PLD from Data base.");
            deleteProductListDocument(id); //Delete this PLD
            throw e;
        } catch (TransportMismatchException e) {
            log.error("In getProductListDocumentByid, Thrown TransportMissmatchException. Deleting ProductListDocument with id: "+ id);
            deleteProductListDocument(id);
            throw e;
        } catch (ATransportModuleException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        mapper.put(pld.getId(), pld);// add pld to the mapper
        return pld;
    }

    @Override
    public void saveProductListDocument(ProductListDocumentDto pld) throws ATransportModuleException, SQLException {
        //check that the ProductListDocument is already exsists:
        if(mapper.get(pld.getId()) != null){
            throw new InvalidPLDException("Didn't added the ProductList Document to the system, A ProsuctLIstDocument with this id already exsists.");
        }try {
            dao.attachTransport(pld.getId(),pld.getTransportID());
            dao.save(pld);
        }
        catch (Exception e){
            dao.deletePLD(pld.getId());
            throw e;
        }
        mapper.put(pld.getId(), PLDdtoTOPLD(pld)); //COnvert the DTO to a ProductListDocument instance and puts in the mapper

    }

    /**
     * delete ProductListDocument by the id of the document.
     * @param pld
     * @throws SQLException
     */
    @Override
    public void deleteProductListDocument(int pld) throws SQLException {
        mapper.remove(pld); //delete PLD from mapper
        dao.deletePLD(pld);
    }

    /**
     * @param dto
     * @return A productListDocument
     * @throws InvalidPLDException
     */
    @Override
    public ProductListDocument PLDdtoTOPLD(ProductListDocumentDto dto) throws ATransportModuleException, SQLException {
        ProductListDocument pld= getProductListDocumentByid(dto.getId());
        if(pld.getDate().equals( dto.getDate() ) && pld.getDestination().getName().equals(dto.getSiteDes() )&& pld.getTransportId() == dto.getTransportID()){
            return pld;
        }
        throw new InvalidPLDException("Product List Document doesnt match with exsists pld ") ;
    }

    /**
     *  recive a ProductListDocument and return a ProductListDocumentDTO
     * @param pld
     * @return
     */
    @Override
    public ProductListDocumentDto pldToDTO(ProductListDocument pld) {
        List<ProductDTO> products = new ArrayList<>(); //get the Products -> quantety map of ProductListDocument
        Map<Product, Integer >pMap = pld.getProducts();
        for(Product p : pMap.keySet()){
            products.add(new ProductDTO(p.getCode(), p.getWeight(), pMap.get(p)));
        }
        return new ProductListDocumentDto(pld.getId(), pld.getTransportId(), pld.getDestination().getName(), products, pld.getTotalWeight(),pld.getDate(),pld.getApproximatedArriavaleTime() );
    }

    //todo : THis is a basic implementation. will be improved with integration with Products Moudule
    public Product productDTOtoProduct(ProductDTO dto){
        Product p = new Product(dto.serialNumber(), "defaultName", dto.weight());
        return p;
    }

    @Override
    public List<ProductListDocument> getPLDByTransportID(int id) throws InvalidATransportException, TransportMismatchException {
        List<ProductListDocument> plds= new ArrayList<>();
        try {
            List<Integer> pldIDs = dao.findByTransport(id);
            for(Integer pldID : pldIDs){
                ProductListDocument pld = getProductListDocumentByid(pldID);
                plds.add(pld);
            }
        }
        catch (SQLException e){
            log.error("Failed to get PLds because of SQL exception");
            return null;
        }
        return plds;
    }

    @Override
    public void setArriavleTime(int pldID, LocalTime time) throws SQLException , ATransportModuleException{
        ProductListDocument p = getProductListDocumentByid(pldID);
        p.setArriavleTime(time);
        dao.setArriavleTime(pldID,time);
    }

    @Override
    public void deleteAll() throws SQLException {
        dao.deleteAll();
        availableid = initValidid();
    }

    @Override
    public void attachTransport(int pID, int tID) throws SQLException, ATransportModuleException {
        ProductListDocument p = getProductListDocumentByid(pID);
        Transport t = transportRep.getTransportByid(tID);
        p.attachTransportToDocument(t);
        dao.attachTransport(pID, tID);

    }
}