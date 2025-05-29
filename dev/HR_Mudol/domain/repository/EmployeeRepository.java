package HR_Mudol.domain.repository;

import HR_Mudol.DAO.*;
import HR_Mudol.domain.Objects.*;
import HR_Mudol.domain.*;
import HR_Mudol.DTO.*;

import java.sql.SQLException;
import java.util.*;

public class EmployeeRepository {
    private final Map<Integer, Employee> employeesById = new HashMap<>();
    private final List<Employee> oldEmployees = new LinkedList<>();
    private final IEmployeeDAO employeeDAO;
    private final IConstraintDAO constraintDAO;

    public EmployeeRepository(IEmployeeDAO edao, IConstraintDAO cdao) {
        this.employeeDAO = edao;
        this.constraintDAO=cdao;
    }

    public Employee addFromDTO(EmployeeDTO dto) throws SQLException {
        //RAM
        Employee employee = mapFromDTO(dto);
        employeesById.put(employee.getEmpId(), employee);

        // DB
        employeeDAO.insert(dto);
        return employee;
    }

    private Employee mapFromDTO(EmployeeDTO dto) {
        return new Employee(
                dto.getFullName(),
                dto.getEmployeeId(),
                dto.getPassword(),
                dto.getBankAccount(),
                dto.getSalary(),
                dto.getStartDate(),
                dto.getMinDayShift(),
                dto.getMinEveningShift(),
                dto.getSickDays(),
                dto.getDaysOff()
        );
    }

    public void archive(int empId) throws SQLException {
        // remove from RAM
        Employee removed = employeesById.remove(empId);
        if (removed != null) {
            oldEmployees.add(removed);
        }

        // archive it on DB
        employeeDAO.archive(empId);
    }


    public boolean exists(int empId) {
        if (employeesById.containsKey(empId)) return true;
        return employeeDAO.exists(empId);
    }


    public Employee getById(int empId) {
        if (employeesById.containsKey(empId))
            return employeesById.get(empId);

        EmployeeDTO dto = employeeDAO.getById(empId);
        if (dto == null) return null;

        Employee e = mapFromDTO(dto);
        employeesById.put(empId, e);
        return e;
    }


    public List<Employee> getAll() {
        return new ArrayList<>(employeesById.values());
    }

    public void updateBankAccount(User caller, int empId, String newBankAccount) throws SQLException {
        Employee e = getById(empId);
        if (e == null) throw new IllegalArgumentException("Employee not found");

        e.setEmpBankAccount(caller, newBankAccount); //RAM

        employeeDAO.updateBankAccount(empId, newBankAccount); //DB
    }

    public void updateSalary(User caller, int empId, int newSalary) throws SQLException {
        Employee e = getById(empId);
        if (e == null) throw new IllegalArgumentException("Employee not found");

        e.setEmpSalary(caller, newSalary); // RAM
        employeeDAO.updateSalary(empId, newSalary); // DB
    }

    public void updateMinDayShift(User caller, int empId, int newMinDay) throws SQLException {
        Employee e = getById(empId);
        if (e == null) throw new IllegalArgumentException("Employee not found");

        e.setMinDayShift(caller, newMinDay); // RAM
        employeeDAO.updateMinDayShift(empId, newMinDay); // DB
    }

    public void updateMinEveningShift(User caller, int empId, int newMinEvening) throws SQLException {
        Employee e = getById(empId);
        if (e == null) throw new IllegalArgumentException("Employee not found");

        e.setMinEveninigShift(caller, newMinEvening); // RAM
        employeeDAO.updateMinEveningShift(empId, newMinEvening); // DB
    }

    public void updateSickDays(User caller, int empId, int newSickDays) throws SQLException {
        Employee e = getById(empId);
        if (e == null) throw new IllegalArgumentException("Employee not found");

        e.setSickDays(caller, newSickDays); // בזיכרון
        employeeDAO.updateSickDays(empId, newSickDays); // בבסיס הנתונים
    }

    public void updateDaysOff(int empId, int daysOff) throws SQLException {
        Employee e = getById(empId); // נטען מהזיכרון או DB
        if (e != null) {
            e.setDaysOff(null, daysOff); // null עבור caller כי זה רק לעדכון פנימי
            employeeDAO.updateDaysOff(empId, daysOff); // עדכון ב־DB
        }
    }






}
