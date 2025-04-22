package HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.Employee;
import HR_Mudol.domain.Role;
import HR_Mudol.domain.User;

import java.util.List;

public interface IRoleManager {

    //Create new role
    void createRole(User caller);

    //Update role description
    void updateRoleDescription(User caller);

    //Assign employee to role
    void assignEmployeeToRole(User caller);

    //Assign employee to be a shift manager
    void assignEmployeeToShiftManager(User caller);

    //Stop employee from doing a role
    void removeEmployeeFromRole(User caller);

    void removeEmployeeFromRole(User caller, int roleNumber, Employee e);

    //Find all the Employees that were assigned to a role
    List<Employee> getRelevantEmployees(User caller);

    //Get all roles
    List<Role> getAllRoles(User caller);

    //printing method:
    void printAllRoles(User caller);

    Role getRoleByNumber(int roleNumber);
}
