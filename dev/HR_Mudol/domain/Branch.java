package HR_Mudol.domain;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Branch {

    // for managing the branch
    static int counter = 0; // branches counter
    private int branchID;
    private List<Week> weeks = new LinkedList<>();             // שבועות עבודה
    private List<Employee> employees;                          // עובדים בחברה
    private List<Role> roles = new LinkedList<>();             // תפקידים בחברה
    private List<User> users;                                  // משתמשים במערכת
    private List<Employee> oldEmployee = new LinkedList<>();   // עובדים שעזבו

    public Branch() {
        this.branchID = counter;
        Week curWeek = new Week();
        this.weeks.add(curWeek);

        // יצירת רשימות
        this.employees = new LinkedList<>();
        this.users = new LinkedList<>();

        // --- HR Manager ---
        HRManager hrManager = new HRManager("Rami Levi", 111111111, "admin", "122345", 300000, LocalDate.now(), 2, 2, 5, 10);
        User hrUser = new User(hrManager, Level.HRManager);
        this.employees.add(hrManager);
        this.users.add(hrUser);

        // --- Regular Employee ---
        Employee regularEmp = new Employee("Dana", 123456789, "pass", "123456", 5000,
                LocalDate.now(), 2, 2, 5, 10);
        User regularUser = new User(regularEmp, Level.regularEmp);
        this.employees.add(regularEmp);
        this.users.add(regularUser);

        // --- Shift Manager ---
        Employee shiftManager = new Employee("Yossi Cohen", 222222222, "shiftadmin", "999999", 7000, LocalDate.now(), 2, 2, 5, 10);
        User shiftUser = new User(shiftManager, Level.shiftManager);
        this.employees.add(shiftManager);
        this.users.add(shiftUser);

        // --- 15 Additional Regular Employees ---
        for (int i = 0; i < 15; i++) {
            int empId = 200000000 + i;
            String name = "Employee" + (i + 1);
            String password = "pass" + (i + 1);
            String bankAccount = "BANK" + (i + 1);
            int salary = 4000 + (i * 100);

            Employee emp = new Employee(name, empId, password, bankAccount, salary,
                    LocalDate.now(), 2, 2, 5, 10);
            User user = new User(emp, Level.regularEmp);

            this.employees.add(emp);
            this.users.add(user);
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Employee> getOldEmployee() {
        return oldEmployee;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public List<Week> getWeeks() {
        return weeks;
    }

    public int getBranchID() {
        return branchID;
    }
}
