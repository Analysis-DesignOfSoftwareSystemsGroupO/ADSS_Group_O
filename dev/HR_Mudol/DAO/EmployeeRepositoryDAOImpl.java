package HR_Mudol.DAO;
import HR_Mudol.DTO.EmployeeDTO;

import java.sql.SQLException;
import java.util.List;

public class EmployeeRepositoryDAOImpl implements IEmployeeRepositoryDAO {

    private final IEmployeeDAO dao;
    private int currentMaxID;

    public EmployeeRepositoryDAOImpl(IEmployeeDAO dao) throws SQLException {
        this.dao = dao;
        this.currentMaxID = dao.getAll().stream()
                .mapToInt(EmployeeDTO::getEmployeeId)
                .max()
                .orElse(0);
    }

    @Override
    public void add(EmployeeDTO employee) throws SQLException {
        employee = new EmployeeDTO(
                ++currentMaxID,
                employee.getFullName(),
                employee.getPassword(),
                employee.getBankAccount(),
                employee.getSalary(),
                employee.getStartDate(),
                employee.getMinDayShift(),
                employee.getMinEveningShift(),
                employee.getSickDays(),
                employee.getDaysOff()
        );
        dao.insert(employee);
    }

    @Override
    public void update(EmployeeDTO employee) throws SQLException {
        dao.update(employee);
    }

    @Override
    public void delete(int empID) throws SQLException {
        dao.delete(empID);
    }

    @Override
    public EmployeeDTO get(int empID) throws SQLException {
        return dao.get(empID);
    }

    @Override
    public List<EmployeeDTO> getAll() throws SQLException {
        return dao.getAll();
    }
}
