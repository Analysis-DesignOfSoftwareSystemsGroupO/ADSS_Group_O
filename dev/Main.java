import java.sql.Driver;
import java.util.ArrayList;
import java.util.Scanner;

import transport_module.*;

public class Main {
    public static void menu_message() {
        System.out.println("Welcome to the Transportation Department!");
        System.out.println("Please choose your next");
        System.out.println("1. Add new transport");
        System.out.println("2. Print all Trucks");
        System.out.println("3. Print all Drivers");
        System.out.println("4. Print Transport details");
        System.out.println("Press any Key to Exit.");


    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        Truck[] trucks = new Truck[9];
        ArrayList<Driver> drivers = new ArrayList<>();
        ArrayList<Transport> transports = new ArrayList<>();

        while (running) {
            menu_message();
            String input = scanner.nextLine();

            switch (input) {
                /** Add new transport*/
                case "1": {
                    break;

                }
                case "2": {
                    /** Print all trucks*/
                    for (int i = 0; i < 9; i++) {
                        trucks[i].toString();
                    }
                    break;

                }
                case "3": {
                    /**print all Drivers*/
                    for (Driver driver : drivers) {
                        driver.toString();
                    }
                    break;
                }
                case "4": {
                    /** print Transport details*/
                    for (Transport transport : transports){
                        transport.toString();
                    }
                        break;

                }
                default: {
                    System.out.println("Thank you! have a nice day.");
                    running = false;
                    break;
                }

            }

        }
        scanner.close();


    }

}
