package DataAccess;

import DTO.ProductListDocumentDto;
import DataLayer.DataBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class jdbcPLDDAO implements IPLDDAO{
    private static final Logger log = LogManager.getLogger(jdbcPLDDAO.class);

    /**
     *
     * @param dto DataTransportObject holds the data to store in the data base
     * @throws SQLException
     */
    @Override
    public void save(ProductListDocumentDto dto) throws SQLException {
        log.info("jdbcPLDDAO ::deletePLD(DTO)");
        String sql = "INSERT INTO ProductListDocument (ProductListDocumentID, TransportID, totalweight, AproximatedArrivaleTime, DestinationSiteName) VALUES (?,?,?,?,?)";
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
        String sql = "DELETE FROM ProductListDocument WHERE ProductListDocumentID = ?";
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
     * @return the heighst product list document id in the data base
     * @throws SQLException
     */
    @Override
    public int getHieghestTransportID() throws  SQLException{
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
            ps.setInt(1,id);

        }
        return Optional.empty();
    }
    public List<String> getListOfProductsByPLDID(int pldID)throws  SQLException{
        log.info("jdbc::getListOfProductsByPLDID( " + pldID + ")");

    }
    @Override
    public List<ProductListDocumentDto> findByTransport(int Tid) throws SQLException {
        return List.of();
    }
}
