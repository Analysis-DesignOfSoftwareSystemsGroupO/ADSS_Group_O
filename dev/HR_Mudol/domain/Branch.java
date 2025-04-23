package HR_Mudol.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Branch {

    //for managing the branch
    static int counter=0; //branches counter
    private int branchID;
    private List<Week> weeks=new LinkedList<>();//שבועות עבודה
    private List<Employee> employees; //עובדים בחברה
    private List<Role> roles=new LinkedList<>();//תפקידים בחברה
    private List<User> users;

    public Branch() {
        this.branchID = counter;
        Week curWeek=new Week();
        this.weeks.add(curWeek);

        Employee reg=new Employee("Dana", 123456789, "pass", "123456", 5000,LocalDate.now(), 2, 2, 5, 10);
        HRManager manager=new HRManager("Rami Levi",111111111,"admin","122345",300000,LocalDate.now());

        User admin= new User(manager,Level.HRManager);
        User dummyEmo=new User(reg, Level.regularEmp);

        this.employees=new LinkedList<>();
        this.employees.add(reg);

        this.users=new LinkedList<>();
        this.users.add(admin);
        this.users.add(dummyEmo);
    }

    public List<User> getUsers() {
        return users;
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
