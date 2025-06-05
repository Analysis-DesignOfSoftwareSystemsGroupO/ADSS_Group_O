package HR_Mudol.presentation;

import HR_Mudol.DTO.BranchDTO;
import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DTO.UserDTO;
import HR_Mudol.domain.Objects.Branch;

import java.sql.SQLException;

/**
 * The Menu interface defines the structure for all menus in the system.
 * Any menu (e.g., HR Manager Menu, Shift Manager Menu, etc.) must implement this interface to standardize the start method.
 */
public interface Menu {

    /**
     * Starts the menu interaction for the given user (caller).
     * This method will be implemented by all classes that represent different menus in the system.
     *
     * @param caller    The user initiating the menu interaction (can be a manager, shift manager, or employee).
     * @param self      The EmployeeDTO object representing the caller's personal data.
     * @param curBranch The branch in which the caller works.
     * @return boolean indicating if the menu interaction was completed successfully (i.e., whether the user logged out or not).
     */
    boolean start(UserDTO caller, EmployeeDTO self, BranchDTO curBranch) throws SQLException;
}
