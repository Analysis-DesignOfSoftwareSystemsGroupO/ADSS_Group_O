package HR_Mudol.domain.repository;

import HR_Mudol.domain.Objects.Employee;
import HR_Mudol.DTO.EmployeeDTO;

import java.util.*;

public class EmployeeRepository {
    private final Map<Integer, Employee> employeesById = new HashMap<>();
    private final List<Employee> oldEmployees = new LinkedList<>();

    public void addFromDTO(EmployeeDTO dto) {
        Employee employee = new Employee(
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
        employeesById.put(employee.getEmpId(), employee);
    }

    public void archive(int id) {
        Employee removed = employeesById.remove(id);
        if (removed != null) oldEmployees.add(removed);
    }

    public boolean exists(int id) {
        return employeesById.containsKey(id);
    }

    public Employee getById(int id) {
        return employeesById.get(id);
    }

    public List<Employee> getAll() {
        return new ArrayList<>(employeesById.values());
    }

    public List<Employee> getArchived() {
        return new ArrayList<>(oldEmployees);
    }

    public void remove(){}

    public void archived(){}
}
