package HR_Mudol;

import java.util.Date;
import java.util.List;

public class Branch {

    //for managing the branch
    static int counter=0; //branches counter
    private int branchID;
    private List<Role> Roles;
    private List<Employee> Emploees;
    private List<Week> ShiftsHistory;

    //for managing the shifts
    private Date StratTimeSubmittingShifts;
    private Date deadLineSubmittingShifts;

}
