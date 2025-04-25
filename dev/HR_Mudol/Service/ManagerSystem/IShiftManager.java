package HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.Employee;
import HR_Mudol.domain.Role;
import HR_Mudol.domain.Shift;
import HR_Mudol.domain.User;

public interface IShiftManager {

    //Assign employee to shift
    void assignEmployeeToShift(User caller, Shift shift, Employee employee, Role role);

    //Remove employee from shift
    void removeEmployeeFromShift(User caller, Shift shift);

    //choosing relevantRole to a shift
    void chooseRelevantRoleForShift(User caller,Shift shift);

    //Printing method
    void printShift(User caller, Shift shift);

    void addEmployeeToShift(User caller, Shift shift, Employee employee, Role role);

    void removeRoleFromShift(User caller, Shift shift);
}
