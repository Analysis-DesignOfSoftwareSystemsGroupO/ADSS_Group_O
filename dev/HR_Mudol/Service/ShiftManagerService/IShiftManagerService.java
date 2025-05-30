package HR_Mudol.Service.ShiftManagerService;
import HR_Mudol.DTO.*;

public interface IShiftManagerService {

    /**
     * Remove an employee from a selected shift in the current week.
     */
    void removeEmployeeFromShift(UserDTO theCaller);

    /**
     * Add an employee to a selected shift in the current week.
     */
    void addEmployeeToShift(UserDTO theCaller);

    /**
     * Transfer the cancellation card to the next shift manager.
     */
    void transferCancellationCard(UserDTO theCaller);
}
