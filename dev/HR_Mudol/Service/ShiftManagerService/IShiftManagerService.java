package HR_Mudol.Service.ShiftManagerSystem;

import HR_Mudol.domain.User;

public interface IShiftManagerService {

    /**
     * Remove an employee from a selected shift in the current week.
     */
    void removeEmployeeFromShift(User caller);

    /**
     * Add an employee to a selected shift in the current week.
     */
    void addEmployeeToShift(User caller);

    /**
     * Transfer the cancellation card to the next shift manager.
     */
    void transferCancellationCard(User caller);
}
