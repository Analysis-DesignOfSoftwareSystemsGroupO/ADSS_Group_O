package HR_Mudol;
import HR_Mudol.AbstractEmployee;
public class Employee extends AbstractEmployee{
    private int minDayShift;
    private int minEveninigShift;
    private int sickDays;
    private int daysOff;

    public Employee(int empNum, String empName, int empId, String empPassword, String empBankAccount, int empSalary, String empStartDate, int minDayShift, int minEveninigShift, int sickDays, int daysOff) {
        super(empNum, empName, empId, empPassword, empBankAccount, empSalary, empStartDate);
        this.minDayShift = minDayShift;
        this.minEveninigShift = minEveninigShift;
        this.sickDays = sickDays;
        this.daysOff = daysOff;
    }

    public int getMinDayShift() {
        return minDayShift;
    }

    public void setMinDayShift(int minDayShift) {
        this.minDayShift = minDayShift;
    }

    public int getMinEveninigShift() {
        return minEveninigShift;
    }

    public void setMinEveninigShift(int minEveninigShift) {
        this.minEveninigShift = minEveninigShift;
    }

    public int getSickDays() {
        return sickDays;
    }

    public void setSickDays(int sickDays) {
        this.sickDays = sickDays;
    }

    public int getDaysOff() {
        return daysOff;
    }

    public void setDaysOff(int daysOff) {
        this.daysOff = daysOff;
    }
}
