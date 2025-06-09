package TransportModule.DataAccess;

import TransportModule.DTO.DriverDto;
import TransportModule.DataLayer.DataBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class jdbcDriverDAO implements IDriverDAO{
    private static final Logger log = LogManager.getLogger(jdbcDriverDAO.class);


    @Override
    public DriverDto getDriverByID(String id) throws SQLException {
        log.info("jdblcDriverDAO::getDriverByID( "+ id + " )");
        List<String > licences = new ArrayList<>();
        String sql = "SELECT \"DriverID\" , \"Licence\" FROM \"Driveres_Licenece\" WHERE \"DriverID\" = ? ;";
        try(PreparedStatement ps = DataBase.getConnection().prepareStatement(sql)){
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            boolean found = false;
            while (rs.next()){
                found = true;
                licences.add(rs.getString("Driveres_Licenece"));
            }
            if(!found)return null;
        }
        catch (SQLException e){
            log.error("Failed to load all licences of the driver ");
            throw e;
        }
        DriverDto d = new DriverDto(id, licences);
        return d;
    }

    @Override
    public void deleteDriver(String id) throws SQLException {
        log.info("jdbcDriverDAO::deleteDriver( "+ id + " )");
        String sql = "DELETE FROM \"Drivers\" WHERE \"DriverID\" = ? ;";
        try (PreparedStatement ps = DataBase.getConnection().prepareStatement(sql)){
            ps.setString(1,id);
            ps.executeUpdate();
        }
        catch (SQLException e){
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;
        }
    }

    @Override
    public void save(DriverDto dto) throws SQLException {
        log.info("jdbcDriverDAO::save()");
        Connection conn = DataBase.getConnection();
        try {
            conn.setAutoCommit(false); // begin transaction

            String sql = "INSERT INTO \"Drivers\" (\"id\" ) VALUES(?) ;";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, dto.id());
                ps.executeUpdate();
            }

            String sql2 = "INSERT INTO \"Driveres_Licenece\" (\"DriverID\" , \"Licence\" ) VALUES ( ?, ?) ;";
            try (PreparedStatement ps2 = conn.prepareStatement(sql2)) {
                for (String licence : dto.drivingLicenceList()) {
                    ps2.setString(1, dto.id());
                    ps2.setString(2, licence);
                    ps2.addBatch();
                }
                ps2.executeBatch();
            }

            conn.commit(); // commit transaction
        } catch (SQLException e) {
            log.error("SQL State: {}\n{}", e.getSQLState(), e.getMessage());
            try {
                conn.rollback(); // rollback transaction on error
            } catch (SQLException rollbackEx) {
                log.error("Rollback failed: {}\n{}", rollbackEx.getSQLState(), rollbackEx.getMessage());
            }
            throw e;
        } finally {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException closeEx) {
                log.error("Connection close failed: {}\n{}", closeEx.getSQLState(), closeEx.getMessage());
            }
        }
    }

    @Override
    public List<String> getAllDriversID() throws SQLException {
        log.info("jdbcDriverDAO::getALlDriversID()");
        String sql = "SELECT * FROM \"Drivers\" ;";
        List<String > list = new ArrayList<>();
        try(Statement st = DataBase.getConnection().createStatement();
        ResultSet rs = st.executeQuery(sql)){
            while (rs.next()){
                list.add(rs.getString("id").trim());
            }
            return list;
        }catch (SQLException e){
            log.error("failed to find all distinct Drivers id ");
            throw e;
        }
    }

    @Override
    public void addLicenceToDriver(String id, String licence) throws SQLException {
        log.info("jdbcDriverDAO::getALlDriversID(" + id + " , " + licence + " ) ");
        //Assume that driver exsists in System
        String sql  = "INSERT INTO \"Driveres_Licenece\" (\"DriverID\" ,  \"Licence\") VALUES( ? , ? ) ; ";
        try(PreparedStatement ps = DataBase.getConnection().prepareStatement(sql)){
            ps.setString(1, id);
            ps.setString(2,licence);
            ps.executeUpdate();
        }
        catch(SQLException e){
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;
        }

    }
}
