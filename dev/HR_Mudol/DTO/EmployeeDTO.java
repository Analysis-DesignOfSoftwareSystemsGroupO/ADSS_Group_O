package HR_Mudol.DTO;

import HR_Mudol.domain.Controllers.DTOToDomainMapper;
import HR_Mudol.domain.Objects.Constraint;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDTO {
    private int employeeId;
    private String fullName;
    private String password;
    private String bankAccount;
    private int salary;
    private LocalDate startDate;
    private int minDayShift;
    private int minEveningShift;
    private int sickDays;
    private int daysOff;


    private List<Integer> relevantRoleIds;
    private List<ConstraintDTO> weeklyConstraints;
    private List<ConstraintDTO> morningConstraints;
    private List<ConstraintDTO> eveningConstraints;
    private List<ConstraintDTO> lockedConstraints;

    public EmployeeDTO(int employeeId, String fullName, String password, String bankAccount, int salary,
                       LocalDate startDate, int minDayShift, int minEveningShift, int sickDays, int daysOff) {
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.password = password;
        this.bankAccount = bankAccount;
        this.salary = salary;
        this.startDate = startDate;
        this.minDayShift = minDayShift;
        this.minEveningShift = minEveningShift;
        this.sickDays = sickDays;
        this.daysOff = daysOff;
        this.relevantRoleIds = List.of();;
        this.weeklyConstraints = List.of();;
        this.morningConstraints=List.of();;
        this.eveningConstraints=List.of();;
        this.lockedConstraints=List.of();;
    }

    public EmployeeDTO(int employeeId, String fullName, String password, String bankAccount, int salary,
                       LocalDate startDate, int minDayShift, int minEveningShift, int sickDays, int daysOff,
                       List<Integer> relevantRoleIds, List<ConstraintDTO> weeklyConstraints,List<ConstraintDTO> eveningConstraints,List<ConstraintDTO> lockedConstraints , List<ConstraintDTO> morningConstraints) {
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.password = password;
        this.bankAccount = bankAccount;
        this.salary = salary;
        this.startDate = startDate;
        this.minDayShift = minDayShift;
        this.minEveningShift = minEveningShift;
        this.sickDays = sickDays;
        this.daysOff = daysOff;
        this.relevantRoleIds=relevantRoleIds;
        this.lockedConstraints=lockedConstraints;
        this.morningConstraints=morningConstraints;
        this.eveningConstraints=eveningConstraints;
        this.weeklyConstraints=weeklyConstraints;

    }

    public int getEmployeeId() { return employeeId; }
    public String getFullName() { return fullName; }
    public String getPassword() { return password; }
    public String getBankAccount() { return bankAccount; }
    public int getSalary() { return salary; }
    public LocalDate getStartDate() { return startDate; }
    public int getMinDayShift() { return minDayShift; }
    public int getMinEveningShift() { return minEveningShift; }
    public int getSickDays() { return sickDays; }
    public int getDaysOff() { return daysOff; }

    public List<Integer> getRelevantRoleIds() { return relevantRoleIds; }
    public List<ConstraintDTO> getWeeklyConstraints() { return weeklyConstraints; }

    public String toString(){

        return DTOToDomainMapper.fromDTO(this).toString();
    }
}
