import HR_Mudol.DataBase.DatabaseInitializer;
import TransportModule.TRS_Main;
import HR_Mudol.HR_Main;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== welcome to HR & Transport Module ===");
        System.out.println("1. Load data from database");
        System.out.println("2. Start with a fresh (empty) system");
        System.out.print("Choose option [1/2]: ");
        String choice = scanner.nextLine().trim();


        boolean loadFromDatabase = choice.equals("1");

        if (!loadFromDatabase && !choice.equals("2")) {
            System.out.println("Invalid option.");
            return;
        }

        // Init DB
        DatabaseInitializer.initialize(loadFromDatabase);

        boolean running = true;
        while (running){
            System.out.println("Please choose your menu:");
            System.out.println("1. HR Menu");
            System.out.println("2. Transport Menu");
            System.out.println("E. Exit");

            choice = scanner.nextLine();
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