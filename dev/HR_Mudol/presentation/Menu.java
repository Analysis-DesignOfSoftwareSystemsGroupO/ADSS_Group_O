package HR_Mudol.presentation;
import HR_Mudol.domain.AbstractEmployee;
import HR_Mudol.domain.Branch;
import HR_Mudol.domain.User;

/**
 * The Menu interface defines the structure for all menus in the system.
 * Any menu (e.g., HR Manager Menu, Shift Manager Menu, etc.) must implement this interface to standardize the start method.
 */
public interface Menu {

    /**
     * Starts the menu interaction for the given user (caller).
     * This method will be implemented by all classes that represent different menus in the system.
     * @param caller The user initiating the menu interaction (can be a manager, shift manager, or employee).
     * @param self The AbstractEmployee object representing the caller's personal data.
     * @param curBranch The branch in which the caller works.
     * @return boolean indicating if the menu interaction was completed successfully (i.e., whether the user logged out or not).
     */
    public abstract boolean start(User caller, AbstractEmployee self, Branch curBranch);
}
