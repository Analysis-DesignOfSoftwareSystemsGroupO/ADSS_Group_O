package TransportModule.Users;

import TransportModule.Presentation.TransportManagerMenu;
import TransportModule.Presentation.TruckManagerMenu;

import java.util.Scanner;

public class TransportManagerUser extends User {
    private final TransportManagerMenu managerMenu;
    private final TruckManagerMenu truckManagerMenu;
    public TransportManagerUser(String username) throws Exception {
        super(username);
        this.managerMenu = new TransportManagerMenu();
        this.truckManagerMenu = new TruckManagerMenu();
    }

    @Override
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to our System");
            System.out.println("1. Trucks menu");
            System.out.println("2. Transport menu");
            System.out.println("E. Exit");
            String input = scanner.nextLine();
            switch (input){
                case "1" -> truckManagerMenu.showMenu();
                case "2" -> managerMenu.showMenu();
                case "E","e" -> {
                    System.out.println("Thank you!");
                    running = false;

                }
                default -> System.out.println("Wrong input Please try again");

            } // End switch case

        } // End while

        scanner.close();
    }
}