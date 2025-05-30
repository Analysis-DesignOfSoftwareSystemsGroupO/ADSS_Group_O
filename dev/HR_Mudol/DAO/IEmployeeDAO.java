package HR_Mudol.DAO;

import HR_Mudol.DTO.EmployeeDTO;

import java.sql.SQLException;
import java.util.List;

public interface IEmployeeDAO {

    EmployeeDTO getById(int employeeId);
    boolean exists(int employeeId);
    void insert(EmployeeDTO employee) throws SQLException;
    void update(EmployeeDTO employee) throws SQLException;
    List<EmployeeDTO> getAll() throws SQLException;
    void archive(int empId) throws SQLException;
    void updateBankAccount(int empId, String newBankAccount) throws SQLException;
    void updateSalary(int empId, int newSalary) throws SQLException;
    void updateMinDayShift(int empId, int newMinDayShift) throws SQLException;
    void updateMinEveningShift(int empId, int newMinEveningShift) throws SQLException;
    void updateSickDays(int empId, int newSickDays) throws SQLException;
    void updateDaysOff(int empId, int daysOff) throws SQLException;
    void updatePassword(int empId, String newPassword);
    List<EmployeeDTO> getAllByBranch(int branchId) throws SQLException;

}
