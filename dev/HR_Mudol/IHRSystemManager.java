package HR_Mudol;

public interface IHRSystemManager extends IEmployeeManager, IRoleManager, IShiftManager, IWeekManager, IReportGenerator {

    void displayDashboard(User caller);
}
