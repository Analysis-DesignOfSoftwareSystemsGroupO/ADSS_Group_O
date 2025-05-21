package HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.ShiftType;
import HR_Mudol.domain.User;
import HR_Mudol.domain.Week;
import HR_Mudol.domain.WeekDay;

import java.time.LocalDateTime;
import java.util.List;

/**
 * IReportGenerator is an interface that defines methods for generating various types of reports within the HR system.
 * This includes generating weekly reports, employee-specific reports, and shift reports.
 */
public interface IReportGenerator {


    /**
     * Generates a weekly report based on the provided list of weeks.
     * The report includes a summary of the activities, shifts, and assignments for the given weeks.
     *
     * @param caller The user requesting the report (e.g., HR manager or admin).
     * @param weeks  The list of weeks for which the report is generated.
     */
    void generateWeeklyReport(User caller, List<Week> weeks);

    /**
     * Generates a report for a specific employee for a given week.
     * The report includes the employee's shifts, roles, and performance during the specified week.
     *
     * @param caller The user requesting the report (e.g., HR manager or admin).
     * @param empId  The employee ID for which the report is generated.
     * @param week   The specific week for which the report is generated.
     */
    void generateEmployeeReport(User caller, int empId, Week week);

    /**
     * Generates a shift report for the given week.
     * The report includes details about the shifts, roles, and assignments for the week.
     *
     * @param caller  The user requesting the report (e.g., HR manager or admin).
     * @param curWeek The current week for which the shift report is generated.
     */
    void generateShiftReport(User caller, Week curWeek);

}