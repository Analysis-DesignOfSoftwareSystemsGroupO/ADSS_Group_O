import java.sql.Driver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
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
        System.out.println("5. Report on completion of transport");
        System.out.println("6. Print all available trucks ");
        System.out.println("7. Send Transport");
        System.out.println("Press E to Exit.");


    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        HashMap<String, Truck> trucks = Truck.getTrucks();
        ArrayList<Driver> drivers = new ArrayList<>();
        ArrayList<Transport> transports = new ArrayList<>();
        ArrayList<ProductListDocument> documents = new ArrayList<>();

        while (running) {
            menu_message();
            String input = scanner.nextLine();

            switch (input) {
                /** Add new transport*/
                case "1": { //todo : Init a transport
                    boolean allTrucksUnAvailabl = true;
                    for(Truck truck: trucks.values()) // check if there is an available truck.
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
                    for (Truck t : trucks.values()) {
                        t.toString();
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
                    //todo: change it to support more than 9 trucks.
                    /** Report on completion of transport */
                    System.out.println("please enter a plate number of the truck");
                    String pn = scanner.nextLine();
                    trucks.get(pn).clear(); //clear the truck out of products and set as avilable
                    break;
                }
                case "6":{
                    /** print all available trucks*/
                    int count = 0;
                    for(Truck truck : trucks.values()){
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
