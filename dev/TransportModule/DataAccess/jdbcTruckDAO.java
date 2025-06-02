package TransportModule.DataAccess;

import TransportModule.DTO.TruckDto;
import TransportModule.DataLayer.DataBase;
import TransportModule.Transport_Module_Exceptions.UnAvailableTruckException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class jdbcTruckDAO  implements ITruckDAO{
    private static final Logger log = LogManager.getLogger(jdbcTruckDAO.class);

    //todo : write a static block or a constructor 
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
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return list;
    }

    @Override
    public void deleteTruck(String pn) throws SQLException {
        log.info("jdbcTruckDAO :: deleteTruck()");
        String sql = "DELETE FROM Trucks WHERE PlateNumber = ? ;";
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

    //assign truck to the date
    @Override
    public boolean checkAvailabilityOfTruck(String truckPN, LocalDate date) throws SQLException  {
        log.info("jdbcTruckDAO :: checkAvailabilityOfTruck( " + truckPN+ " , " + date + " ) ");
        //check that the Truck is not occuppied
        String sql = "SELECT COUNT(TruckPN) AS COUNTER FROM TruckAvailability WHERE EXSISTS (SELECT TruckPN FROM TruckAvailability WHERE TruckPn = ? AND Date = ? );";
        try (PreparedStatement ps = DataBase.getConnection().prepareStatement(sql)){
            ps.setString(1,truckPN);
            ps.setDate(2, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();
            int count  ;
            if(rs.next()) {
                count = rs.getInt("COUNTER");
                if (count != 0) return false;
            }
        } catch (SQLException e) {
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     *
     * @param truckPn
     * @return list of dates that the truck is available
      * @throws SQLException
     */
    public List<LocalDate> getListofOccupiedDates(String truckPn) throws SQLException{
        log.info("jdbcTruckDAO :: checkAvailabilityOfTruck( " + truckPn+ "  ) ");
        String sql = "SELECT Date FROM TruckAvailability WHERE TruckPN = ? ;";
        List<LocalDate> dates = new ArrayList<>();
        try (PreparedStatement ps = DataBase.getConnection().prepareStatement(sql)){
            ps.setString(1, truckPn);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                dates.add(rs.getDate("Date").toLocalDate());
            }
        }
        catch (SQLException e ){
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dates;
        }


    /**
     * Assign truck to a date
     * @param truckPN
     * @param date
     * @throws SQLException
     * @throws UnAvailableTruckException
     */
    @Override
    public void assignTruckToDate(String truckPN, LocalDate date) throws SQLException, UnAvailableTruckException {
        log.info("jdbcTruckDAO::assignTruckToDate ( " + truckPN+ " , " + date + " ) ");
        if(checkAvailabilityOfTruck(truckPN, date )) throw new UnAvailableTruckException();
        String sql = "INSERT INTO TruckAvailability (Date , TruckPN ) VALUES(? , ? )";
        try(PreparedStatement ps = DataBase.getConnection().prepareStatement(sql)){
            ps.setString(1,truckPN);
            ps.setDate(2, Date.valueOf(date));
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            throw e;
        }
    }
}
