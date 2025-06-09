package HR_Mudol.Service;

import HR_Mudol.DTO.BranchDTO;
import HR_Mudol.DTO.UserDTO;
import HR_Mudol.DTO.WeekDTO;
import HR_Mudol.domain.Objects.User;
import HR_Mudol.domain.Objects.Week;

import java.sql.SQLException;
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
    void generateWeeklyReport(UserDTO caller, List<WeekDTO> weeks);

    /**
     * Generates a report for a specific employee for a given week.
     * The report includes the employee's shifts, roles, and performance during the specified week.
     *
     * @param caller The user requesting the report (e.g., HR manager or admin).
     * @param empId  The employee ID for which the report is generated.
     * @param week   The specific week for which the report is generated.
     */
    void generateEmployeeReport(UserDTO caller, int empId, WeekDTO week) throws SQLException;

    /**
     * Generates a shift report for the given week.
     * The report includes details about the shifts, roles, and assignments for the week.
     *
     * @param caller  The user requesting the report (e.g., HR manager or admin).
     * @param curWeek The current week for which the shift report is generated.
     */
    void generateShiftReport(UserDTO caller, WeekDTO curWeek);

    void generateReports(UserDTO caller, BranchDTO branch, String reportType);
}