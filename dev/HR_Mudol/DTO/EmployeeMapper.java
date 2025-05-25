package HR_Mudol.DTO;


import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.domain.Employee;

public class EmployeeMapper {
    public static EmployeeDTO toDTO(Employee emp) {
        return new EmployeeDTO(
                emp.getEmpId(),
                emp.getEmpName(),
                emp.getEmpPassword(),
                emp.getEmpBankAccount(),
                emp.getEmpSalary(),
                emp.getEmpStartDate(),
                emp.getMinDayShift(null),          // נדרש caller — אתה יכול לעדכן
                emp.getMinEveninigShift(null),
                emp.getSickDays(null),
                emp.getDaysOff(null)
        );
    }

    public static Employee fromDTO(EmployeeDTO dto) {
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
}
