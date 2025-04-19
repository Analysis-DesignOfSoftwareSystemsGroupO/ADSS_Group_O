package HR_Mudol.presentation;

import HR_Mudol.domain.*;

import java.util.Scanner;

public class WeekManager implements IWeekManager{

    @Override
    public void createNewWeek(User caller) {

    }
    //choose relevant role for each shift
    public void manageTheWeekRelevantRoles (User caller,Week week){
        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }
        for (Shift shift : week.getShifts()){
            ShiftManager.chooseRelevantRoleForShift(caller,shift);
        }
    }

    //Assigning employees to shifts
    @Override
    public void assigningEmployToShifts(User caller, Week week) {

        Constraint constraint;
        Scanner scanner = new Scanner(System.in);

        //loop for all the week's shifts
        for (Shift shift: week.getShifts()){ //example: Sunday morning

            //loop for all the necessary roles at specific shift
            for (Role role : shift.getNecessaryRoles()){ //example: Sunday morning

                //print all the relevant employee:
                System.out.println(role.getRelevantEmployees(caller));
                boolean flag=false;

                //loop for choosing an emp for that role
                while (!flag){
                    System.out.println("Choose by employee by his place number on the list:");
                    int index = scanner.nextInt();

                    Employee employee= role.getRelevantEmployees(caller).get(index);
                    constraint=employee.searchingForRelevantconstraint(caller,shift.getDay(),shift.getType());
                    if (constraint==null){
                        //there no constraint for that shift
                        System.out.println("The employee can work at that shift, \n Do you want to choose him? IF so write - Y, else write N");
                        String choosing = scanner.nextLine();
                        if (choosing == "Y") {
                            ShiftManager.assignEmployeeToShift(caller,shift,employee);
                            flag=true;
                            break; //move to the next role
                        }
                        //else moving the next employee on the list
                    }
                    //there is constraint for that shift
                    else {
                        System.out.println(employee.getEmpName() + "can't work at that shift because: "+ constraint.getExplanation());
                    }

                }
                System.out.println("Next role");
            }
            System.out.println("Next Shift");


        if (shift.getNecessaryRoles().size()==shift.getEmployees().size()){
            shift.updateStatus(caller,Status.Full);
        }
        else shift.updateStatus(caller,Status.Problem);

        //print result
        printWeek(week);
    }

    //Remove a shift - for holidays case use
    @Override
    public void cancelShift(User caller,Week week) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }
        Scanner scanner = new Scanner(System.in);

        System.out.print("Choose day (Capital letters only)\n");
        String day = scanner.nextLine();

        System.out.print("Choose type (Capital letters only)\n");
        String type = scanner.nextLine();

        for (Shift shift : week.getShifts()){
            if (shift.getType().name().equals(type) && shift.getDay().name().equals(day)){
                week.removeShift(shift);
                System.out.println(shift + " was deleted");
                break;
            }
        }
    }


    @Override
    public void printWeek(Week week) {
        System.out.println(week);
    }

}
