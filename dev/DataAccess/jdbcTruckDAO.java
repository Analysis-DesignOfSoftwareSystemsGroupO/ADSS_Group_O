package DataAccess;

import DTO.TruckDto;
import DataLayer.DataBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class jdbcTruckDAO  implements ITruckDAO{

    @Override
    public TruckDto save(TruckDto Truckdto) throws SQLException {
        return null;
    }

    @Override
    public Optional<TruckDto> findByTruckPN(String plateNumber) throws SQLException {
        String sql = "Select PlateNumber, maxWeight, LicenceReq From Trucks WHERE PlateNumber = ?";
        try(PreparedStatement ps = DataBase.getConnection().prepareStatement(sql)){
            ps.setString(1, plateNumber);
            try(ResultSet rs = ps.executeQuery()){
                return rs.next() ? Optional.of()
            }
        }
    }

    @Override
    public List<TruckDto> findAllTrucks() throws SQLException {
        return List.of();
    }
}
