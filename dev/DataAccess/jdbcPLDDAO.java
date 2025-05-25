package DataAccess;

import DTO.ProductListDocumentDto;
import DataLayer.DataBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class jdbcPLDDAO implements IPLDDAO{
    private static final Logger log = LogManager.getLogger(jdbcPLDDAO.class);


    @Override
    public void save(ProductListDocumentDto dto) throws SQLException {
        log.info("jdbcPLDDAO ::deletePLD(DTO)");
        String sql = "INSERT INTO ProductListDocument (ProductListDocumentID, DestinationSiteName, TransportID, totalweight, Date) VALUES (?,?,?,?,?)";
        if(dto != null){
            try (PreparedStatement ps = DataBase.getConnection().prepareStatement(sql)) {
                ps.setInt(1,dto.getId());
                ps.setString(2, dto.getSiteDes());
                ps.setInt(3,dto.getTransportID());
                ps.setInt(4, dto.ge);
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
}
