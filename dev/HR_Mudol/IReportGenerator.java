package HR_Mudol;

public interface IReportGenerator {

    void generateWeeklyReport(User caller, int weekId);

    void generateEmployeeReport(User caller, int empId);

    void generateShiftReport(User caller, int shiftId);

    void generateCustomReport(User caller, String filter);
}