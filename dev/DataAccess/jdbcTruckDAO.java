package DataAccess;

import DTO.TruckDto;
import DataLayer.DataBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class jdbcTruckDAO  implements ITruckDAO{
    private static final Logger log = LogManager.getLogger(DataBase.class);

    @Override
    public void save(TruckDto dto) throws SQLException {
        log.info("jdbcTrucakDAO:: save() ");
        if (dto.getPlateNumber() != null) {
            String sql = "INSERT INTO Trucks (MaxWeight, LicenceReq, PlateNumber) VALUES (?,?,?)";
            try (PreparedStatement ps = DataBase.getConnection().prepareStatement(sql)) {
                ps.setInt(1, dto.getMaxWeight());
                ps.setString(2, dto.getLiceenceReq());
                ps.setString(2, dto.getPlateNumber());
                ps.executeUpdate();
            } catch (SQLException e) {
                log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
                throw e;
            }
        }
    }

    @Override
    public Optional<TruckDto> findByTruckPN(String pn) throws SQLException{
        log.info("jdbcTruckDAO :: findByTruckPN(" + pn + " )");
        String sql = "Select PlateNumber, maxWeight, LicenceReq From Trucks WHERE PlateNumber = ?";
        try(PreparedStatement ps = DataBase.getConnection().prepareStatement(sql)){
            ps.setString(1, pn);
            try(ResultSet rs = ps.executeQuery()){
                return rs.next()
                        ? Optional.of(new TruckDto(rs.getInt("maxWeight"), rs.getString("LicenceReq"),rs.getString("PlateNumber") ))
                        :Optional.empty(); //Create DTo by the query
            }
        }


    }


    @Override
    public List<TruckDto> findAllTrucks() throws SQLException {
        log.info("jdbcTruckDAO :: findAllTrucks()");
        String sql = "SELECT * FROM Trucks ORDER BY PlateNumber ASC"; //get all rows
        List<TruckDto> list = new ArrayList<>();

        try (Statement st = DataBase.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) { //run query
            while (rs.next()) {
                list.add(new TruckDto(rs.getInt("maxWeight"), rs.getString("LicenceReq"),rs.getString("PlateNumber"))); //create DTO by row
            }

        }
        catch (SQLException e) {
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void deleteTruck(String pn) throws SQLException {
        log.info("jdbcTruckDAO :: deleteTruck()");
        String sql = "DELETE FROM Trucks WHERE PlateNumber = ?";
        try (Connection conn = DataBase.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, pn);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
