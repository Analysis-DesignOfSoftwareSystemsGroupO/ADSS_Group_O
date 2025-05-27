package HR_Mudol.Service.ManagerService;

import HR_Mudol.Service.EmployeeService.IEmployeeService;
import HR_Mudol.Service.IReportGenerator;
import HR_Mudol.domain.*;
import HR_Mudol.domain.Objects.Branch;
import HR_Mudol.domain.Objects.User;

/**
 * IHRSystemManager is an interface that defines the core functionality required to manage the HR system for a given branch.
 * This includes managing employees, roles, shifts, weeks, and generating reports.
 * The interface extends multiple other manager interfaces for employee, role, shift, week, and report generation functionality.
 */
public interface IHRControllerService extends IEmployeeService, IRoleController, IShiftController, IWeekController, IReportGenerator
 {

    /**
     * Displays the dashboard with an overview of the current week and employee status.
     * The dashboard provides information such as the current week, required shifts, and employee status.
     *
     * @param caller    The user requesting the dashboard (e.g., an HR manager or admin).
     * @param curBranch The branch for which the dashboard is being displayed.
     */
    void displayDashboard(User caller, Branch curBranch);

     Branch getBranch();
     IRoleController getRoleManager();
}
