package TransportModule.DataAccess;

import TransportModule.DTO.ProductDTO;
import TransportModule.DTO.ProductListDocumentDto;
import TransportModule.DataLayer.DataBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class jdbcPLDDAO implements IPLDDAO{
    private static final Logger log = LogManager.getLogger(jdbcPLDDAO.class);

    /**
     * @param dto DataTransportObject holds the data to store in the data base
     * @throws SQLException
     */
    @Override
    public void save(ProductListDocumentDto dto) throws SQLException {
        log.info("jdbcPLDDAO ::deletePLD(DTO)");
        String sql = "INSERT INTO \"ProductListDocument\" (\"ProductListDocumentID\", \"TransportID\", \"totalweight\", \"AproximatedArrivaleTime\",\"DestinationSiteName\") VALUES (?,?,?,?,?)";
        if(dto != null){
            try (PreparedStatement ps = DataBase.getConnection().prepareStatement(sql)) {
                ps.setInt(1,dto.getId());
                ps.setInt(2,dto.getTransportID());
                ps.setInt(3, dto.getWeight());
                ps.setTime(4, Time.valueOf(dto.getApproximatedArrivalTime()));
                ps.setString(5, dto.getSiteDes());
                ps.executeUpdate();//run query
            }
            catch (SQLException e){
                log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
                throw e;
            }
        }
    }

    //delete a PLD from DataBase
    @Override
    public void deletePLD(int serialNumber) throws SQLException {
        log.info("jdbcPLDDAO ::deletePLD( serialNumber= " + serialNumber + ")") ;
        String sql = "DELETE FROM \"ProductListDocument\" WHERE \"ProductListDocumentID\" = ?";
        try (Connection conn = DataBase.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, serialNumber);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @return the heighst product list document id in the data base, return 0 if failed
     * @throws SQLException
     */
    @Override
    public int getHieghestPLDID() throws  SQLException{
        log.info("jdbcPLDDAO :: getHieghestTransportID ()");
        String sql = "SELECT ProductListDocumentID FROM ProductListDocument ORDER BY ProductListDocumentID DESC LIMIT 1";

        try(Statement st = DataBase.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql)){
            if(rs.next()){
                return   rs.getInt(1);
            }
        }
        catch (SQLException e){
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;
        }
        return 0 ;
    }

    /**
     *
     * @param id ProductListDocumentID
     * @return Optional PLDDTO, all data necesary to Create PLD
     * @throws SQLException
     */
    @Override
    public Optional<ProductListDocumentDto> findByPLDID(int id) throws SQLException {
        log.info("jdbc:: findByPLDID( " + id+ ")");
        String sql = "SELECT * FROM ProductListDocument WHERE ProductListDocumentID = ?";
        try (PreparedStatement ps =  DataBase.getConnection().prepareStatement(sql)){
            List<ProductDTO> products = getListOfProductsByPLDID(id); //get the ProductsDto for this PLD
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            return rs.next()
                    ? Optional.of(new ProductListDocumentDto(id, rs.getInt("TransportID"), rs.getString("DestinationSiteName"), products, getWeightOfProducts(products),rs.getDate("Date").toLocalDate(), rs.getTime("aproximatedArrivaleTime").toLocalTime() ))
                    :Optional.empty();// return ProductDTo , if failed to find return empty Optional

        }
        catch (SQLException e) {
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;
        }

    }

    /**
     *
     * @param pldID
     * @return list of productDTO related to the productListDocumentID provided
     * @throws SQLException
     */
    public List<ProductDTO> getListOfProductsByPLDID(int pldID)throws  SQLException{
        log.info("jdbc::getListOfProductsByPLDID( " + pldID + ")");
        String sql = "SELECT ProductQuantety, WeightPerUnit, ProductSerialNumber FROM ProductListDocument_Products WHERE ProductListDocumentId = ?";
        List<ProductDTO> products = new ArrayList<>();
        try(PreparedStatement ps = DataBase.getConnection().prepareStatement(sql)){
            ps.setInt(1, pldID);
            ResultSet rs = ps.executeQuery(sql) ;
            while (rs.next()) {
                //Adding ProductDTO to the List
                products.add(new ProductDTO(rs.getString("ProductSerialNumber"), rs.getInt("WeightPerUnit"), rs.getInt("ProductQuantety")));
            }
        }catch (SQLException e){
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;
        }
        return products;
    }

    /**
     * calculate the weight of list of productsDTo
     */
    private int getWeightOfProducts(List<ProductDTO> products){
        if(products == null) return 0;
        if(products.isEmpty()) return  0;
        int sum = 0;
        for( ProductDTO p : products){
            sum += p.weight();
        }
        return sum;
    }

    @Override
    public List<Integer> findByTransport(int Tid) throws SQLException {
        log.info("jdbcPLDDAO ::findByTransport( " + Tid + ") ");
        String sql = "SELECT ProductListDocumentId FROM Transports_ProductListDocument WHERE TransportId = ? ;";
        List<Integer> PLDids = new ArrayList<>();
        try(PreparedStatement ps = DataBase.getConnection().prepareStatement(sql)) {
            ps.setInt(1, Tid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                //AddingProductID to the list
                PLDids.add(rs.getInt("ProductListDocumentId"));
            }
        }catch (SQLException e){
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;
        }
        return PLDids;

    }
    @Override
    public void setArriavleTime(int pldID, LocalTime time) throws SQLException{
        log.info("jdbcPLDDAO::setArrivaleTime( + " + pldID + " , " + time + " ) ");
        String sql = "UPDATE ProductListDocument SET aproximatedArrivaleTime = ? WHERE ProductListDocumentID = ? ;";
        try (PreparedStatement ps = DataBase.getConnection().prepareStatement(sql)){
            ps.setTime(1,Time.valueOf(time));
            ps.setInt(2, pldID);
        }
        catch (SQLException e){
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;
        }
    }

    /**
     *
     * @return list of id PLD in the data base
     * @throws SQLException
     */
    @Override
    public List<Integer> getPLDsID() throws SQLException {
        log.info("jdbcPLDDAO::getPLDs()");
        String sql = "SELECT ProductListDocumentId FROM ProductListDocument;"; //SQL statement
        List<Integer> idList = new ArrayList<>();
        try(Statement st = DataBase.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                //add PLD to list
                idList.add(rs.getInt("ProductListDocumentId"));
            }
        }catch (SQLException e) {
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw  e;
        }
        return idList;
    }
}
