package HR_Mudol.DAO;

import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DTO.FilledRoleDTO;
import HR_Mudol.DTO.RoleDTO;
import HR_Mudol.DTO.ShiftDTO;
import HR_Mudol.domain.ShiftType;
import HR_Mudol.domain.Status;
import HR_Mudol.domain.WeekDay;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ShiftDAOImpl extends BaseDAO implements IShiftDAO {

    public ShiftDAOImpl() throws SQLException {
        super();
    }

    @Override
    public void insert(ShiftDTO shift) throws SQLException {
        String sql = "INSERT INTO shifts (shiftID, day, type, status, shiftManager) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shift.getShiftID());
            stmt.setString(2, shift.getDay());
            stmt.setString(3, shift.getType());
            stmt.setString(4, shift.getStatus());
            stmt.setLong(5, shift.getShiftManagerId());
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
            stmt.setLong(4, shift.getShiftManagerId());
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

    public void insertEmpToShift(int branchID, long empID, int shiftID, int roleNumber) {
        String sql = "INSERT INTO ShiftAssignments (branchID, shiftID, empID, roleNumber) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, branchID);
            stmt.setInt(2, shiftID);
            stmt.setLong(3, empID);
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
    public void insertOrIncrementRequiredRole(int branchID, WeekDay day, ShiftType type, int roleNumber, int count) {
        String selectShiftIDs = "SELECT shiftID FROM Shifts WHERE branchID = ? AND day = ? AND type = ? AND deadline >= ?";
        String select = "SELECT counter FROM RequiredRoles WHERE branchID = ? AND shiftID = ? AND roleNumber = ?";
        String update = "UPDATE RequiredRoles SET counter = counter + ? WHERE branchID = ? AND shiftID = ? AND roleNumber = ?";
        String insert = "INSERT INTO RequiredRoles (branchID, shiftID, roleNumber, counter) " +
                "VALUES (?, ?, ?, ?) ON CONFLICT (branchID, shiftID, roleNumber) DO NOTHING";

        try (PreparedStatement shiftStmt = conn.prepareStatement(selectShiftIDs)) {
            shiftStmt.setInt(1, branchID);
            shiftStmt.setString(2, day.name());
            shiftStmt.setString(3, type.name());
            shiftStmt.setDate(4, Date.valueOf(LocalDate.now()));

            ResultSet shiftRs = shiftStmt.executeQuery();

            while (shiftRs.next()) {
                int shiftID = shiftRs.getInt("shiftID");

                // Check if role already exists for this shift
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
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert/increment required role for matching shifts", e);
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

    public boolean isEmployeeAssignedToShift(long empId, int shiftId) {
        String sql = "SELECT 1 FROM ShiftAssignments WHERE empID = ? AND shiftID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, empId);
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
                return Status.valueOf(rs.getString("status"));
            } else {
                throw new RuntimeException("Shift not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get shift status", e);
        }
    }

    @Override// מביא את המשמרות שיש לעובד שבוע קדימה (כבר שובצו)
    public List<ShiftDTO> getCurShiftsByBranch(int branchId) {
        String sql = "SELECT * FROM Shifts WHERE branchID = ? AND deadline >= ? AND deadline < ?";
        List<ShiftDTO> shifts = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // חישוב תחילת השבוע - יום ראשון
            LocalDate today = LocalDate.now();
            DayOfWeek dow = today.getDayOfWeek();
            LocalDate sunday = today.minusDays(dow.getValue() % 7); // ראשון

            // חישוב סיום השבוע - שבת בבוקר, כלומר לפני ראשון הבא
            LocalDate nextSaturdayNight = sunday.plusDays(6).plusDays(1); // ראשון הבא

            stmt.setInt(1, branchId);
            stmt.setDate(2, java.sql.Date.valueOf(sunday));
            stmt.setDate(3, java.sql.Date.valueOf(nextSaturdayNight));

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int shiftId = rs.getInt("shiftID");

                ShiftDTO shift = new ShiftDTO(
                        shiftId,
                        rs.getString("day"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getInt("shiftmanager")
                );

                // שיבוצים בפועל
                shift.setFilledRoles(getFilledRoles(shiftId));

                // תפקידים דרושים
                shift.setNecessaryRoles(getNecessaryRoles(shiftId));

                // עובדים בשיבוץ
                shift.setEmployeeIds(getEmployeesInShift(shiftId));

                shifts.add(shift);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch shifts", e);
        }

        return shifts;
    }

    @Override //מחזיר את המשמרות של שבוע הבא- אלו שעוד לא שובצו
    public List<ShiftDTO> getNextShiftsByBranch(int branchId) {
        String sql = "SELECT * FROM Shifts WHERE branchID = ? AND deadline >= ? AND deadline <= ?";
        List<ShiftDTO> shifts = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // מחשבים את טווח התאריכים של השבוע הבא: ראשון עד שישי
            LocalDate today = LocalDate.now();
            DayOfWeek currentDow = today.getDayOfWeek();
            LocalDate thisSunday = today.minusDays(currentDow.getValue() % 7);
            LocalDate nextSunday = thisSunday.plusWeeks(1); // ראשון הבא
            LocalDate nextFriday = nextSunday.plusDays(5);  // שישי הבא

            stmt.setInt(1, branchId);
            stmt.setDate(2, java.sql.Date.valueOf(nextSunday));
            stmt.setDate(3, java.sql.Date.valueOf(nextFriday));

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int shiftId = rs.getInt("shiftID");

                ShiftDTO shift = new ShiftDTO(
                        shiftId,
                        rs.getString("day"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getInt("shiftmanager")
                );

                shift.setFilledRoles(getFilledRoles(shiftId));
                shift.setNecessaryRoles(getNecessaryRoles(shiftId));
                shift.setEmployeeIds(getEmployeesInShift(shiftId));

                shifts.add(shift);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch next week's shifts", e);
        }

        return shifts;
    }



    private List<FilledRoleDTO> getFilledRoles(int shiftId) throws SQLException {
        String sql = "SELECT * FROM shiftassignments WHERE shiftID = ?";
        List<FilledRoleDTO> result = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shiftId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(new FilledRoleDTO(
                        shiftId,
                        rs.getInt("empID"),
                        rs.getInt("roleNumber")
                ));
            }
        }
        return result;
    }

    private List<RoleDTO> getNecessaryRoles(int shiftId) throws SQLException {
        String sql = "SELECT rr.roleNumber, r.description FROM requiredroles rr JOIN roles r ON rr.roleNumber = r.roleNumber WHERE rr.shiftID = ?";
        List<RoleDTO> result = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shiftId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(new RoleDTO(
                        rs.getInt("roleNumber"),
                        rs.getString("description"),
                        null // לא טוענים כרגע עובדים מתאימים
                ));
            }
        }
        return result;
    }

    private List<EmployeeDTO> getEmployeesInShift(int shiftId) throws SQLException {
        List<EmployeeDTO> employees = new ArrayList<>();

        String sql = """
        SELECT e.*
        FROM shiftassignments sa
        JOIN employees e ON sa.empID = e.empID
        WHERE sa.shiftID = ?
    """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shiftId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                EmployeeDTO employee = new EmployeeDTO(
                        rs.getInt("empID"),
                        rs.getString("empName"),
                        rs.getString("empPassword"),
                        rs.getString("empBankAccount"),
                        rs.getInt("empSalary"),
                        rs.getDate("empStartDate") != null ? rs.getDate("empStartDate").toLocalDate() : null,
                        rs.getInt("minDayShift"),
                        rs.getInt("minEveningShift"),
                        rs.getInt("sickDays"),
                        rs.getInt("daysOff")
                );
                employees.add(employee);
            }
        }

        return employees;
    }

    @Override // יצירת משמרות חדשות לשבוע הבא (ראשון עד שישי)
    public void insertShift(ShiftDTO shift, int branchId) {
        String sql = "INSERT INTO Shifts (shiftID, branchID, deadline, day, type, status, shiftManager) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (deadline, type, branchID) DO NOTHING";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shift.getShiftID());
            stmt.setInt(2, branchId);

            // חישוב תחילת השבוע הבא (ראשון)
            LocalDate today = LocalDate.now();
            DayOfWeek currentDay = today.getDayOfWeek();
            LocalDate nextSunday = today.minusDays(currentDay.getValue() % 7).plusWeeks(1);

            // מחשבים כמה ימים מראשון הבא עד ליום הרצוי
            DayOfWeek targetDay = DayOfWeek.valueOf(shift.getDay().toUpperCase());
            int daysToAdd = (targetDay.getValue() - DayOfWeek.SUNDAY.getValue() + 7) % 7;
            LocalDate deadline = nextSunday.plusDays(daysToAdd);

            stmt.setDate(3, Date.valueOf(deadline));
            stmt.setString(4, shift.getDay());
            stmt.setString(5, shift.getType());
            stmt.setString(6, shift.getStatus());

            if (shift.getShiftManagerId() == -1) {
                stmt.setNull(7, Types.BIGINT);
            } else {
                stmt.setLong(7, shift.getShiftManagerId());
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert shift", e);
        }
    }

    @Override
    public void insertShiftsForNextWeek(int branchId) {
        String sql = "INSERT INTO Shifts (shiftID, branchID, deadline, day, type, status, shiftManager) " +
                "VALUES (?, ?, ?, ?, ?, 'Empty', NULL) " +
                "ON CONFLICT (shiftID) DO NOTHING";

        LocalDate today = LocalDate.now();
        DayOfWeek currentDow = today.getDayOfWeek();
        LocalDate thisSunday = today.minusDays(currentDow.getValue() % 7);
        LocalDate nextSunday = thisSunday.plusWeeks(1); // ראשון הבא

        String[] days = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"};
        int shiftId = getMaxShiftIdFromDB() + 1;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < days.length; i++) {
                LocalDate deadline = nextSunday.plusDays(i);
                for (String type : new String[]{"MORNING", "EVENING"}) {
                    stmt.setInt(1, shiftId++);
                    stmt.setInt(2, branchId);
                    stmt.setDate(3, Date.valueOf(deadline));
                    stmt.setString(4, days[i]);
                    stmt.setString(5, type);
                    stmt.addBatch();
                }
            }
            stmt.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert shifts for branch " + branchId + " for next week", e);
        }
    }

    private int getMaxShiftIdFromDB() {
        String sql = "SELECT COALESCE(MAX(shiftID), 0) FROM Shifts";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get max shiftID", e);
        }
        return 0;
    }

    public void assignedShiftM(long empId, int shiftId) {
        String sql = "UPDATE Shifts SET shiftmanager = ? WHERE shiftid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, empId);
            stmt.setInt(2, shiftId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to assign shift manager", e);
        }
    }

}
