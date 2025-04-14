import java.sql.Driver;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;

import transport_module.*;

public class submain {
    public static void menu_message() {
        System.out.println("Welcome to the Transportation Department!");
        System.out.println("Please choose your next");
        System.out.println("1. Add new transport");
        System.out.println("2. Assign driver to transport");
        System.out.println("3. Add product to transport");



        System.out.println("2. Print all Trucks");
        System.out.println("3. Print all Drivers");
        System.out.println("4. Print Transport details");
        System.out.println("5. Report on completion of transport");
        System.out.println("6. Print all available trucks ");
        System.out.println("7. Send Transport");
        System.out.println("Press E to Exit.");


    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        Truck[] trucks = new Truck[9];
        ArrayList<Driver> drivers = new ArrayList<>();
        ArrayList<Transport> transports = new ArrayList<>();
        ArrayList<ProductListDocument> documents = new ArrayList<>();

        while (running) {
            menu_message();
            String input = scanner.nextLine();

            switch (input) {
                /** Add new transport*/
                case "1": {
                    boolean allTrucksUnAvailabl = true;
                    for(Truck truck: trucks) // check if there is an available truck.
                        if(truck.getAvailablity()) {
                            allTrucksUnAvailabl = false;
                            break;
                        }
                    if(allTrucksUnAvailabl){
                        System.out.println("There is no available truck please check later");
                        break;
                    }
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
                case "5":
                {
                    /** Report on completion of transport */
                    System.out.println("please enter a number between 1-9");
                    int number = scanner.nextInt();
                    if(number<1 || number>9) break;
                    number--;
                    trucks[number].clear();

                    break;
                }
                case "6":{
                    /** print all available trucks*/
                    int count = 0;
                    for(Truck truck : trucks){
                        if(truck.getAvailablity()){
                            truck.toString();
                            count++;
                        }
                    }
                    if (count == 0)
                        System.out.println("There is no available truck in system.");


                    break;
                }
                case  "E":{
                    System.out.println("Thank you! have a nice day.");
                    running = false;
                    break;
                }
                default: {
                    System.out.println("Wrong input! Please try again.");
                    break;
                }

            }

        }
        scanner.close();


    }

}
