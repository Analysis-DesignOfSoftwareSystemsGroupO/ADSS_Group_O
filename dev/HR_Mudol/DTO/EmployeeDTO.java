package HR_Mudol.DTO;

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

    public EmployeeDTO(int employeeId, String fullName, String password, String bankAccount, int salary,
                       LocalDate startDate, int minDayShift, int minEveningShift, int sickDays, int daysOff,
                       List<Integer> relevantRoleIds, List<ConstraintDTO> weeklyConstraints) {
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
        this.relevantRoleIds = relevantRoleIds;
        this.weeklyConstraints = weeklyConstraints;
    }
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
        this.relevantRoleIds = new ArrayList<>();        // אתחול ריק
        this.weeklyConstraints = new ArrayList<>();      // אתחול ריק
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
}
