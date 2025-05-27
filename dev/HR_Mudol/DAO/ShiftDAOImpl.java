package HR_Mudol.DAO;

import HR_Mudol.DTO.ShiftDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShiftDAOImpl implements IShiftDAO {
    private final Connection conn;

    public ShiftDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(ShiftDTO shift) throws SQLException {
        String sql = "INSERT INTO shifts (shiftID, day, type, status, shiftManager) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shift.getShiftID());
            stmt.setString(2, shift.getDay());
            stmt.setString(3, shift.getType());
            stmt.setString(4, shift.getStatus());
            stmt.setInt(5, shift.getShiftManagerId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(ShiftDTO shift) throws SQLException {
        String sql = "UPDATE shifts SET day = ?, type = ?, status = ?, shiftManager = ? WHERE shiftID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, shift.getDay());
            stmt.setString(2, shift.getType());
            stmt.setString(3, shift.getStatus());
            stmt.setInt(4, shift.getShiftManagerId());
            stmt.setInt(5, shift.getShiftID());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int shiftID) throws SQLException {
        String sql = "DELETE FROM shifts WHERE shiftID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shiftID);
            stmt.executeUpdate();
        }
    }

    @Override
    public ShiftDTO get(int shiftID) throws SQLException {
        String sql = "SELECT * FROM shifts WHERE shiftID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shiftID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ShiftDTO(
                        rs.getInt("shiftID"),
                        rs.getString("day"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getInt("shiftManager")
                );
            }
        }
        return null;
    }

    @Override
    public List<ShiftDTO> getAll() throws SQLException {
        List<ShiftDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM shifts";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new ShiftDTO(
                        rs.getInt("shiftID"),
                        rs.getString("day"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getInt("shiftManager")
                ));
            }
        }
        return list;
    }

    @Override
    public void insertEmpToShift(int brunchID, int empNum, int shiftID, int roleNumber) {
        String sql = "INSERT INTO ShiftAssignments (brunchID, shiftID, empNum, roleNumber) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, brunchID);
            stmt.setInt(2, shiftID);
            stmt.setInt(3, empNum);
            stmt.setInt(4, roleNumber);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert employee to shift", e);
        }
    }

    @Override
    public void removeEmpFromShift(int shiftID, int empNum) {
        String sql = "DELETE FROM ShiftAssignments WHERE shiftID = ? AND empNum = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shiftID);
            stmt.setInt(2, empNum);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove employee from shift", e);
        }
    }

    @Override
    public void decrementOrRemove(int branchID, int shiftID, int roleNumber) {
        String getSql = "SELECT counter FROM RequiredRoles WHERE branchID = ? AND shiftID = ? AND roleNumber = ?";
        String updateSql = "UPDATE RequiredRoles SET counter = counter - 1 WHERE branchID = ? AND shiftID = ? AND roleNumber = ?";
        String deleteSql = "DELETE FROM RequiredRoles WHERE branchID = ? AND shiftID = ? AND roleNumber = ?";

        try (PreparedStatement stmt = conn.prepareStatement(getSql)) {
            stmt.setInt(1, branchID);
            stmt.setInt(2, shiftID);
            stmt.setInt(3, roleNumber);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("counter");
                rs.close();

                if (count > 1) {
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, branchID);
                        updateStmt.setInt(2, shiftID);
                        updateStmt.setInt(3, roleNumber);
                        updateStmt.executeUpdate();
                    }
                } else {
                    try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                        deleteStmt.setInt(1, branchID);
                        deleteStmt.setInt(2, shiftID);
                        deleteStmt.setInt(3, roleNumber);
                        deleteStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update RequiredRoles", e);
        }
    }

    @Override
    public void insertOrIncrementRequiredRole(int branchID, int shiftID, int roleNumber, int count) {
        String selectSql = "SELECT counter FROM RequiredRoles WHERE branchID = ? AND shiftID = ? AND roleNumber = ?";
        String updateSql = "UPDATE RequiredRoles SET counter = counter + ? WHERE branchID = ? AND shiftID = ? AND roleNumber = ?";
        String insertSql = "INSERT INTO RequiredRoles (branchID, shiftID, roleNumber, counter) VALUES (?, ?, ?, ?)";

        try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
            selectStmt.setInt(1, branchID);
            selectStmt.setInt(2, shiftID);
            selectStmt.setInt(3, roleNumber);

            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                // already exists → increment
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, count);
                    updateStmt.setInt(2, branchID);
                    updateStmt.setInt(3, shiftID);
                    updateStmt.setInt(4, roleNumber);
                    updateStmt.executeUpdate();
                }
            } else {
                // doesn't exist → insert
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, branchID);
                    insertStmt.setInt(2, shiftID);
                    insertStmt.setInt(3, roleNumber);
                    insertStmt.setInt(4, count);
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert/increment required role", e);
        }
    }

}
