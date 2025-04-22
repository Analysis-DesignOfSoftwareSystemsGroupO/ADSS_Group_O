package HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.User;

public interface IHRSystemManager extends IEmployeeManager, IRoleManager, IShiftManager, IWeekManager, IReportGenerator {

    void displayDashboard(User caller);
}
