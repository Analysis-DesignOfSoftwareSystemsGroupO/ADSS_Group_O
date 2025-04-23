package HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.ShiftType;
import HR_Mudol.domain.User;
import HR_Mudol.domain.Week;
import HR_Mudol.domain.WeekDay;

import java.time.LocalDateTime;
import java.util.List;

public interface IReportGenerator {

    void generateWeeklyReport(User caller, List<Week> weeks);

    void generateEmployeeReport(User caller, int empId, Week week);

    void generateShiftReport(User caller, Week curWeek);

}