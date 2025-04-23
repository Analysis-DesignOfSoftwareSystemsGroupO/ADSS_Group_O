package HR_Mudol.Service.ManagerSystem;
import HR_Mudol.domain.*;
import java.util.Scanner;

public class WeekManager implements IWeekManager{

    private IShiftManager dependency;

    public WeekManager(IShiftManager dependency){
        this.dependency = dependency;
    }

    @Override
    public Week createNewWeek(User caller) {
        Week newWeek=new Week();
        return newWeek;
    }
    //choose relevant role for each shift
    public void manageTheWeekRelevantRoles (User caller,Week week){
        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }

        for (Shift shift : week.getShifts()){
            dependency.chooseRelevantRoleForShift(caller,shift);
        }
    }

    //Assigning employees to shifts
    @Override
    public void assigningEmployToShifts(User caller, Week week) {

            Constraint constraint;
            Scanner scanner = new Scanner(System.in);

            //loop for all the week's shifts
            for (Shift shift : week.getShifts()) {
                //example: Sunday morning
                System.out.print("For the shift "+shift.getDay() + " - " + shift.getType()+ " ,");
                if (!shift.getNecessaryRoles().isEmpty()) {

                    //loop for all the necessary roles at specific shift
                    for (Role role : shift.getNecessaryRoles()) { //example: Sunday morning

                        System.out.print("You should find an employee from the list for the role - "+role.getDescription() + ", \nplease choose an employee from the list :\n");

                        //print all the relevant employee:
                        printRelevantEmp(caller,role);
                        boolean flag = false;

                        //loop for choosing an emp for that role
                        while (!flag) {
                            System.out.println("\n(Choose the employee by his place in the list)");
                            int index = scanner.nextInt();

                            Employee employee = role.getRelevantEmployees(caller).get(index-1);
                            constraint = employee.searchingForRelevantconstraint(caller, shift.getDay(), shift.getType());
                            if (constraint == null) {
                                //there no constraint for that shift
                                System.out.println("The employee can work at that shift, \n Do you want to choose him? IF so write - Y, else write N");
                                String choosing = scanner.nextLine();
                                if (choosing == "Y") {

                                    dependency.assignEmployeeToShift(caller, shift, employee);
                                    flag = true;
                                    break; //move to the next role
                                }
                                //else moving the next employee on the list
                            }
                            //there is constraint for that shift
                            else {
                                System.out.println(employee.getEmpName() + "can't work at that shift because: " + constraint.getExplanation());
                            }

                        }
                        System.out.println("Next role");
                    }
                    System.out.println("Next Shift");
                }
                else{
                    System.out.println("First you have to assigning roles to weekly shifts.");
                    break;
                }

            if (shift.getNecessaryRoles().size() == shift.getEmployees().size() && !shift.getNecessaryRoles().isEmpty()) {
                shift.updateStatus(caller, Status.Full);
            } else shift.updateStatus(caller, Status.Problem);
        }
    }
    private void printRelevantEmp(User caller,Role role){
        int index=1;
        for (Employee emp :role.getRelevantEmployees(caller)){
            System.out.println(index + ". " +emp.getEmpName() );
            index++;

        }
    }

    //Remove a shift - for holidays case use
    @Override
    public void cancelShift (User caller, Week week){

            if (!caller.isManager()) {
                throw new SecurityException("Access denied.");
            }
            Scanner scanner = new Scanner(System.in);

            System.out.print("Choose day (Capital letters only)\n");
            String day = scanner.nextLine();

            System.out.print("Choose type (Capital letters only)\n");
            String type = scanner.nextLine();

            for (Shift shift : week.getShifts()) {
                if (shift.getType().name().equals(type) && shift.getDay().name().equals(day)) {
                    week.removeShift(shift);
                    System.out.println(shift + " was deleted");
                    break;
                }
            }
    }
    @Override
    public void printWeek (Week week){
            System.out.println(week);
        }
    }

