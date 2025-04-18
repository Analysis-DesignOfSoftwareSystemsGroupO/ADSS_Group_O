package HR_Mudol;

import java.util.List;

public interface IRoleManager {

    //Create new role
    void createRole(User caller, String description);

    //Update role description
    void updateRoleDescription(User caller, int roleNumber, String newDesc);

    //Assign employee to role
    void assignEmployeeToRole(User caller, int roleNumber, Employee employee);

    //Stop employee from doing a role
    void removeEmployeeFromRole(User caller, int roleNumber, Employee employee);

    //Find all the Employees that were assigned to a role
    List<Employee> getRelevantEmployees(User caller, int roleNumber);

    //printing method:
    void printAllRoles(User caller);
}
