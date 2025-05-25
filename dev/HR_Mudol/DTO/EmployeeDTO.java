package HR_Mudol.DTO;

// --- EmployeeDTO.java ---
import java.time.LocalDate;
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
}




