package DataAccess;

import DTO.TransportDTO;
import DataLayer.DataBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class jdbcTransportDAO implements ITransportDAO {
    private static final Logger log = LogManager.getLogger(DataBase.class);

    @Override
    public Optional<TransportDTO> getTransportByid(int id) throws SQLException {
        log.info("jdbcTransportDAO :: getTransportByid( " + id + " ) ");
        String sql = "SELECT id, Date , is_sent , maximum_weight  , TruckPN , DriverID, departure_time, Sorce_site_name FROM  Transports WHERE id = ?";
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
    public List<TransportDTO> getTransports() throws SQLException {
        log.info("jdbcTransportDAO :: getTransports");
        String sql = "SELECT * FROM Tranports ORDER BY id ASC";
        List<TransportDTO> transports = new ArrayList<>();

        try(Statement st = DataBase.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()){
                //Adding tansport DTO to the List
                transports.add(new TransportDTO(rs.getInt("id"), rs.getDate("Date").toLocalDate(), rs.getBoolean("is_sent"), rs.getInt("maximum_weight"), rs.getString("DriverID"), rs.getString("TruckPN"), rs.getString("Source_site_name"), rs.getTime("departure_time").toLocalTime()));
            }
        }
        catch (SQLException e) {
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transports;
    }


    public int getHieghestTransportID() throws  SQLException{
        log.info("jdbcTransportDAO :: getHieghestTransportID ()");
        String sql = "SELECT id FROM Transports ORDER BY id DESC LIMIT 1";

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

    @Override
    public void save(TransportDTO transportDTO) throws SQLException {
        log.info("jdbcTransportsDAO :: save() ");
        String sql = "INSERT INTO Transports (id, Date, is_sent, maximum_weight, Truck_PN, DriverID, departure_time, Source_site_name ) VALUES (?,?,?,?,?,?,?,?)";
        if(transportDTO != null){
            try (PreparedStatement ps = DataBase.getConnection().prepareStatement(sql)) {
                ps.setInt(1,transportDTO.getId());
                ps.setTime(2, Time.valueOf(transportDTO.getDepartureTime()) );
                ps.setBoolean(3, transportDTO.isSent());
                ps.setInt(4,transportDTO.getMaxWeight());
                ps.setString(5, transportDTO.getSiteName());
                ps.setString(6,transportDTO.getTruckPN());
                ps.setDate(7, Date.valueOf(transportDTO.getDate()));
                ps.setString(8, ((Integer)transportDTO.getId()).toString());

            }
    }

    @Override
    public void deleteTransport(int id) throws SQLException {

    }
}
