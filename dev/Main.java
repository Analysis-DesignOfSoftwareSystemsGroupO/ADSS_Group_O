import java.sql.Driver;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import transport_module.*;

public class Main {
    public static void menu_message() {
        System.out.println("Welcome to the Transportation Department!");
        System.out.println("Please choose your next");
        System.out.println("1. Add new transport");
        System.out.println("2. Add driver to transport");
        System.out.println("3. Add product to transport");
        System.out.println("4. Remove product from transport");
        System.out.println("5. Send transport");
        System.out.println("6. Print all Trucks");
        System.out.println("7. Print all Drivers");
        System.out.println("8. Print Transport details");
        System.out.println("9. Print all available trucks ");
        System.out.println("Press E to Exit.");


    }
    /** Add new Transport function*/
    public static void AddNewTransport(Truck[] trucks,  Map<Integer,Transport> transports){
        boolean allTrucksUnAvailabl = true;
        Truck t =null;
        for(Truck truck: trucks) // check if there is an available truck.
            if(truck.getAvailablity()) {
                allTrucksUnAvailabl = false;
                t = truck;
                break;
            }
        if(allTrucksUnAvailabl){
            System.out.println("There is no available truck please check later");
            return;
        }
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter date by format: DD/MM/YEAR");
        String dateStr = scanner.nextLine();


        System.out.println("Please enter date by format: HH:MM");
        String timeStr = scanner.nextLine();


        System.out.println("Please enter source site name");
        String site_name = scanner.nextLine();

        System.out.println("Please enter source site area");
        String area_name = scanner.nextLine();

        Site site = new Site(site_name,area_name);
        try {
            Transport transport = new Transport(dateStr,timeStr,t,site);
            transports.put(transport.getId(),transport);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        scanner.close();


    }
    public void AddDriverToTransport(   Map<String,Driver> drivers ,ArrayList<Transport> transports){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter driver's id");
        String id = scanner.nextLine();
        Driver driver = drivers.get(id);
        if(driver == null){
            System.out.println("Driver is not in system - please try again");
            return;
        }



    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        Truck[] trucks = new Truck[9];
        Map<String,Driver> drivers = new HashMap<>();
        Map<Integer,Transport> transports = new HashMap<>();
//        ArrayList<Transport> transports = new ArrayList<>();
        ArrayList<ProductListDocument> documents = new ArrayList<>();


        while (running) {
            menu_message();
            String input = scanner.nextLine();

            switch (input) {
                /** Add new transport*/
                case "1": {
                    AddNewTransport(trucks,transports);
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
