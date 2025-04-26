package dev.HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.Branch;
import HR_Mudol.domain.Week;

import java.util.LinkedList;

/**
 * TestBranch is a special version of Branch used for unit testing.
 * It initializes completely empty without creating employees or roles.
 */
public class TestBranch extends Branch {

    public TestBranch() {
        super(); // Call normal constructor

        // Clear all automatic initializations (if any)
        getWeeks().clear();
        getEmployees().clear();
        getRoles().clear();
        getUsers().clear();
        getOldEmployee().clear();

        // Reinitialize empty lists
        getWeeks().add(new Week()); // We do want one current week
    }
}
