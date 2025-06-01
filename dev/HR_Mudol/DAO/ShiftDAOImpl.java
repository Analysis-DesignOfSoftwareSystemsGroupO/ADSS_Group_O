package HR_Mudol.DAO;

import HR_Mudol.DTO.ShiftDTO;
import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DTO.FilledRoleDTO;
import HR_Mudol.DTO.RoleDTO;
import HR_Mudol.DataBase.PostgresConnection;
import HR_Mudol.domain.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShiftDAOImpl implements IShiftDAO {
    private final Connection conn;

    public ShiftDAOImpl() throws SQLException {
        this.conn = PostgresConnection.getConnection();
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
    public void delete(int shiftId) {
        String deleteAssignments = "DELETE FROM ShiftAssignments WHERE shiftId = ?";
        String deleteRequired = "DELETE FROM RequiredRoles WHERE shiftId = ?";
        String deleteShift = "DELETE FROM Shifts WHERE shiftId = ?";

        try (
                PreparedStatement stmt1 = conn.prepareStatement(deleteAssignments);
                PreparedStatement stmt2 = conn.prepareStatement(deleteRequired);
                PreparedStatement stmt3 = conn.prepareStatement(deleteShift)
        ) {
            stmt1.setInt(1, shiftId);
            stmt1.executeUpdate();

            stmt2.setInt(1, shiftId);
            stmt2.executeUpdate();

            stmt3.setInt(1, shiftId);
            stmt3.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete shift with ID: " + shiftId, e);
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
                        rs.getInt("shiftManager"),
                        null, // employees
                        null, // necessaryRoles
                        null  // filledRoles
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
                        rs.getInt("shiftManager"),
                        null,
                        null,
                        null
                ));
            }
        }
        return list;
    }

    @Override
    public void insertEmpToShift(int branchID, int empID, int shiftID, int roleNumber) {
        String sql = "INSERT INTO ShiftAssignments (branchID, shiftID, empID, roleNumber) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, branchID);
            stmt.setInt(2, shiftID);
            stmt.setInt(3, empID);
            stmt.setInt(4, roleNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to assign employee to shift", e);
        }
    }

    @Override
    public void removeEmpFromShift(int shiftID, int empID) {
        String sql = "DELETE FROM ShiftAssignments WHERE shiftID = ? AND empID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shiftID);
            stmt.setInt(2, empID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove employee from shift", e);
        }
    }

    @Override
    public void decrementOrRemove(int branchID, int shiftID, int roleNumber) {
        String select = "SELECT counter FROM RequiredRoles WHERE branchID = ? AND shiftID = ? AND roleNumber = ?";
        String update = "UPDATE RequiredRoles SET counter = counter - 1 WHERE branchID = ? AND shiftID = ? AND roleNumber = ?";
        String delete = "DELETE FROM RequiredRoles WHERE branchID = ? AND shiftID = ? AND roleNumber = ?";

        try (PreparedStatement stmt = conn.prepareStatement(select)) {
            stmt.setInt(1, branchID);
            stmt.setInt(2, shiftID);
            stmt.setInt(3, roleNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("counter");
                rs.close();

                if (count > 1) {
                    try (PreparedStatement updateStmt = conn.prepareStatement(update)) {
                        updateStmt.setInt(1, branchID);
                        updateStmt.setInt(2, shiftID);
                        updateStmt.setInt(3, roleNumber);
                        updateStmt.executeUpdate();
                    }
                } else {
                    try (PreparedStatement deleteStmt = conn.prepareStatement(delete)) {
                        deleteStmt.setInt(1, branchID);
                        deleteStmt.setInt(2, shiftID);
                        deleteStmt.setInt(3, roleNumber);
                        deleteStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update/remove required role", e);
        }
    }

    @Override
    public void insertOrIncrementRequiredRole(int branchID, int shiftID, int roleNumber, int count) {
        String select = "SELECT counter FROM RequiredRoles WHERE branchID = ? AND shiftID = ? AND roleNumber = ?";
        String update = "UPDATE RequiredRoles SET counter = counter + ? WHERE branchID = ? AND shiftID = ? AND roleNumber = ?";
        String insert = "INSERT INTO RequiredRoles (branchID, shiftID, roleNumber, counter) VALUES (?, ?, ?, ?)";

        try (PreparedStatement selectStmt = conn.prepareStatement(select)) {
            selectStmt.setInt(1, branchID);
            selectStmt.setInt(2, shiftID);
            selectStmt.setInt(3, roleNumber);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                try (PreparedStatement updateStmt = conn.prepareStatement(update)) {
                    updateStmt.setInt(1, count);
                    updateStmt.setInt(2, branchID);
                    updateStmt.setInt(3, shiftID);
                    updateStmt.setInt(4, roleNumber);
                    updateStmt.executeUpdate();
                }
            } else {
                try (PreparedStatement insertStmt = conn.prepareStatement(insert)) {
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

    @Override
    public void updateStatus(int shiftId, String newStatus) {
        String sql = "UPDATE Shifts SET status = ? WHERE shiftID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, shiftId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update shift status", e);
        }
    }

    @Override
    public boolean isEmployeeAssignedToShift(int empId, int shiftId) {
        String sql = "SELECT 1 FROM ShiftAssignments WHERE empID = ? AND shiftID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empId);
            stmt.setInt(2, shiftId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check assignment", e);
        }
    }

    @Override
    public Status getShiftStatus(int shiftId) {
        String sql = "SELECT status FROM Shifts WHERE shiftId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shiftId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Status.valueOf(rs.getString("status").toUpperCase());
            } else {
                throw new RuntimeException("Shift not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get shift status", e);
        }
    }
}
