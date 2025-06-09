package HR_Mudol.DAO;

import HR_Mudol.DTO.EmployeeDTO;

import java.sql.SQLException;
import java.util.List;

public interface IEmployeeDAO {

    EmployeeDTO getById(long employeeId);
    boolean exists(long employeeId);
    void insert(EmployeeDTO dto, int brunchID) throws SQLException;
    void update(EmployeeDTO employee) throws SQLException;
    List<EmployeeDTO> getAll(int branchID) throws SQLException;
    void archive(int empId) throws SQLException;
    void updateBankAccount(int empId, String newBankAccount) throws SQLException;
    void updateSalary(int empId, int newSalary) throws SQLException;
    void updateMinDayShift(int empId, int newMinDayShift) throws SQLException;
    void updateMinEveningShift(int empId, int newMinEveningShift) throws SQLException;
    void updateSickDays(int empId, int newSickDays) throws SQLException;
    void updateDaysOff(int empId, int daysOff) throws SQLException;
    void updatePassword(long empId, String newPassword);
    List<EmployeeDTO> getAllByBranch(int branchId) throws SQLException;
    boolean isEmployeeInBranch(int empId, int branchId) throws SQLException;


}
