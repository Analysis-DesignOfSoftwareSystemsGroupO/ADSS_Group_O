import TransportModule.TRS_Main;
import HR_Mudol.HR_Main;

import java.util.Scanner;

public class Main {
    public static void print_message(){
        System.out.println("welcome to HR & Transport Module");
        System.out.println("Please choose your menu:");
        System.out.println("1. HR Menu");
        System.out.println("2. Transport Menu");
        System.out.println("E. Exit");
    }
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running){
            print_message();
            String choice = scanner.nextLine();
            switch (choice){
                case "1" ->  HR_Main.HR_main(args);
                case "2" -> TRS_Main.TRS_main(args);
                case "E","e" -> {
                    System.out.println("GoodBye!");
                    running = false;
                }
                default -> System.out.println("Wrong input please try again");
            }
        }

    }

}
