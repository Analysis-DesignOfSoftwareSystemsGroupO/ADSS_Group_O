package TransportModule.DataAccess;

import TransportModule.DTO.TransportDTO;
import TransportModule.DataLayer.DataBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class jdbcTransportDAO implements ITransportDAO {
    private static final Logger log = LogManager.getLogger(jdbcTransportDAO.class);

    @Override
    public Optional<TransportDTO> getTransportByid(int id) throws SQLException {
        log.info("jdbcTransportDAO :: getTransportByid( " + id + " ) ");
        String sql = "SELECT \"id\", \"Date\" , \"is_sent\" , \"maximum_weight\"  , \"TruckPN\" , \"DriverID\", \"departure_time\", \"Source_site_name\" FROM  \"Transports\" WHERE \"id\" = ?";
        try (PreparedStatement ps = DataBase.getConnection().prepareStatement(sql)){
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()){
                return rs.next()
                        ? Optional.of(new TransportDTO(rs.getInt("id"), rs.getDate("Date").toLocalDate(), rs.getBoolean("is_sent"), rs.getInt("maximum_weight"), rs.getString("DriverID"), rs.getString("TruckPN"), rs.getString("Source_site_name"), rs.getTime("departure_time").toLocalTime()))
                        :Optional.empty(); //return TransportDTo , if failed to find return empty Optional
            }

        }
    }

    @Override
    public List<TransportDTO> getTransportsByDate(LocalDate date) throws SQLException {
        log.info("jdbcTransportDAO :: getTransportsByDate( " + date + " )");
        String sql = "SELECT \"id\" FROM \"Transports\" WHERE \"Date\" = ? ;";
        List<Integer> idList = new ArrayList<>();
        try(PreparedStatement ps = DataBase.getConnection().prepareStatement(sql)){
            ps.setDate(1, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                idList.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;
        }
        List<TransportDTO> transports = new ArrayList<>();
        for(int id : idList){ //for each id found in the query, addd to the Transports
            Optional<TransportDTO> optDTO = getTransportByid(id);
            if(optDTO.isPresent())transports.add(optDTO.get());
        }
        return transports;
    }

    @Override
    public List<TransportDTO> getTransports() throws SQLException {
        log.info("jdbcTransportDAO :: getTransports");
        String sql = "SELECT * FROM \"Transports\" ORDER BY \"id\" ASC;";
        List<TransportDTO> transports = new ArrayList<>();

        try(Statement st = DataBase.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()){
                //Adding tansport DTO to the List
                String driverId = rs.getString("DriverID");
                if(driverId != null) driverId= driverId.trim();
                String truckPN = rs.getString("TruckPN");
                if(truckPN != null) truckPN = truckPN.trim();
                transports.add(new TransportDTO(rs.getInt("id"), rs.getDate("Date").toLocalDate(), rs.getBoolean("is_sent"), rs.getInt("maximum_weight"), driverId, truckPN, rs.getString("Source_site_name").trim(), rs.getTime("departure_time").toLocalTime()));
            }
        }
        catch (SQLException e) {
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw  e;
        }
        return transports;
    }


    public int getHieghestTransportID() throws  SQLException{
        log.info("jdbcTransportDAO :: getHieghestTransportID ()");
        String sql = "SELECT \"id\" FROM \"Transports\" ORDER BY \"id\" DESC LIMIT 1;";

        try(Statement st = DataBase.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql)){
            if(rs.next()){
                return   rs.getInt("id");
            }
        }
        catch (SQLException e){
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;
        }
        return 0 ;
    }

    //retun list of Transport dto that have no trucks assigned
    @Override
    public List<TransportDTO> getTransportsWithoutTruck() throws SQLException {
        log.info("jdbcTransportDAO:: getTransportsWithoutTruck");
        String sql = "SELECT \"id\" FROM \"Transports\" WHERE \"TruckPN\" IS NULL;";
        List<Integer> tIDs = new ArrayList<>(); // create a list of transport id that has no trucks assigned
        try (Statement st = DataBase.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) { //get all Tranports id without tansports, foreach add the id to the list
                tIDs.add(rs.getInt("id"));
            }
            List<TransportDTO> transportsDTO = new ArrayList<>();
            for (int id : tIDs) { //iterate the transportsID list, for each transport id , get the TransportDTo and add it to the list
                Optional<TransportDTO> optionalTransport = getTransportByid(id);
                if (optionalTransport.isPresent())
                    transportsDTO.add(optionalTransport.get()); // add the Dto to the list if it present
            }
            return transportsDTO;
        }catch (SQLException e ){
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;
        }
    }

    /**
     * Update the truck PN on the record of the transportID
     * @param transportID
     * @param truckPN
     * @throws SQLException
     */
    @Override
    public void assignTruckToTransport(int transportID, String truckPN) throws SQLException {
        log.info("jdbcTransportDAO ::assignTruckToTransport( " + transportID+ " , " +truckPN + " )" );
        String sql = "UPDATE \"Transports\" SET \"TruckPN\" = ? WHERE \"id\" = ?;";
        try(PreparedStatement ps = DataBase.getConnection().prepareStatement(sql)){
            ps.setString(1,truckPN); //set the transportID argument as the first questionMark
            ps.setInt(2, transportID);//set the truckPN  argument as the second questionMark
            ps.executeUpdate(); //run query
        }catch (SQLException e ){
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;
        }
    }

    @Override
    public void save(TransportDTO transportDTO) throws SQLException {
        log.info("jdbcTransportsDAO :: save() ");
        String sql = "INSERT INTO \"Transports\" (\"id\", \"Date\", \"maximum_weight\", \"TruckPN\", \"DriverID\", \"departure_time\", \"Source_site_name\" ,\"is_sent\" ) VALUES (?,?,?,?,?,?,?,?) ;";
        if (transportDTO != null) {
            try (PreparedStatement ps = DataBase.getConnection().prepareStatement(sql)) {
                ps.setInt(1, transportDTO.getId());
                ps.setDate(2, Date.valueOf(transportDTO.getDate()));
                ps.setBoolean(8, transportDTO.isSent());
                ps.setInt(3, transportDTO.getMaxWeight());
                ps.setString(4, transportDTO.getTruckPN());
                ps.setString(5, transportDTO.getDriverID());
                ps.setTime(6, Time.valueOf(transportDTO.getDepartureTime()));
                ps.setString(7,transportDTO.getSiteName());
                ps.executeUpdate();
            } catch (SQLException e) {
                log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
                throw e;
            }
        }
    }
    @Override
    public void deleteTransport ( int id ) throws SQLException{
        log.info("jdbcTransport::deleteTransport( " + id + ")");
        String sql = "DELETE FROM \"Transports\" WHERE \"id\" = ?";
        try (PreparedStatement ps = DataBase.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void deleteAll() throws SQLException{
        String sql = "DELETE FROM \"Transport\" WHERE \"id\" <> -1 ;";
        try(PreparedStatement ps = DataBase.getConnection().prepareStatement(sql)) {
            ps.executeUpdate();

        }catch (SQLException e){
            throw e;
        }
    }

    /**
     *
     * @return list of integers of ids
     * @throws SQLException
     */
    @Override
    public List<Integer> getIDs() throws SQLException {
        log.info("jdbcTransportDAO::getIDs()");
        String sql = "SELECT \"id\" FROM \"Transports\" ; ";
        List<Integer > list = new ArrayList<>();
        try(Statement st = DataBase.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);){
            while (rs.next()){
                list.add(rs.getInt("id"));
            }
        }
        catch (SQLException e) {
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return list;
    }

    @Override
    public void setSent(int id) throws SQLException {
        log.info("jdbcTransportDAO::setSent( " + id + " )");
        String sql = "UPDATE \"Transports\" SET \"is_sent\" = TRUE WHERE \"id\" = ?;";
        try(PreparedStatement ps =DataBase.getConnection().prepareStatement(sql)){
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        catch (SQLException e){
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;
        }
    }

    @Override
    public void setDriver(int id, String driverID) throws SQLException {
        log.info("jdbcTransportDAO::setDriver( " + id + " , "+ driverID +" )");
        String sql = "UPDATE \"Transports\" SET \"DriverID\" = ?  WHERE \"id\" = ?;";
        try(PreparedStatement ps =DataBase.getConnection().prepareStatement(sql)){
            ps.setString(1, driverID);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
        catch (SQLException e){
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;
        }
    }

    @Override
    public void setMaxWeight(int tID, int w) throws SQLException {
        log.info("jdbcTransportDAO::setMaxWeight( " + tID + " , " + w + " )");
        String sql = "UPDATE \"Transports\" SET \"maximum_weight\" = ? WHERE \"id\" = ? ;";
        try (PreparedStatement ps = DataBase.getConnection().prepareStatement(sql)){
            ps.setInt(1, w);
            ps.setInt(2, tID);
            ps.executeUpdate();
        }
        catch (SQLException e){
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;
        }
    }

}
