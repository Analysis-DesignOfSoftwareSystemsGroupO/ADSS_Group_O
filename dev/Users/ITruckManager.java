package Users;

import Transport_Module_Exceptions.ATransportModuleException;
import Transport_Module_Exceptions.InvalidDateFormatException;
import Transport_Module_Exceptions.InvalidInputException;
import transport_module.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Scanner;

public interface ITruckManager {


    /***
     * function that add new truck to system
     * @param scanner input variable
     * @param trucks map of trucks and their plate numbers
     * @param drivingLicences map of all driving licences in system
     * @throws ATransportModuleException if input is invalid / truck is already in system / driving license is not in system
     */
    default void addNewTruckToSystem(Scanner scanner, Map<String, Truck> trucks, Map<String, DrivingLicence> drivingLicences) throws ATransportModuleException {
        if (trucks == null || scanner == null || drivingLicences == null)
            throw new InvalidInputException();
        System.out.println("Please enter Truck's plate number");
        String platenumber = scanner.nextLine();
        if (trucks.get(platenumber) != null)
            throw new InvalidInputException("Truck is already in system");
        System.out.println("Please enter driving licence code");
        String drivinglincenceCode = scanner.nextLine();
        DrivingLicence drivingLicence = drivingLicences.get(drivinglincenceCode);
        if (drivingLicence == null)
            throw new InvalidInputException("driving licence is not in system");
        int maxweight;
        try {
            System.out.println("Please enter maximum");
            maxweight = scanner.nextInt();
        } catch (Exception e) {
            throw new InvalidInputException("Please enter integer number");
        }
        if (maxweight <= 0) {
            throw new InvalidInputException("Please enter positive number");
        }
        Truck t = new Truck(drivingLicence, maxweight, platenumber);
        trucks.put(platenumber, t);
        System.out.println("Truck has successfully added");


    }

    /***
     * function that add prints all trucks in system
     * @param trucks map of trucks and their plate numbers
     * @throws ATransportModuleException if input is invalid */
    default void printAllTrucks(Map<String, Truck> trucks) throws ATransportModuleException {
        if (trucks == null)
            throw new InvalidInputException("Trucks data is null");
        for (String platenumber : trucks.keySet()) {
            System.out.println(trucks.get(platenumber));
        }
    }

    /***
     * function that add prints all available trucks in specific date
     * @param trucks map of trucks and their plate numbers
     * @throws ATransportModuleException if input is invalid */
    default void printAllAvailableTrukcs(Map<String, Truck> trucks, Scanner scanner) throws ATransportModuleException {
        if (trucks == null || scanner == null)
            throw new InvalidInputException();
        System.out.println("Please enter date by format: DD/MM/YYYY"); // ask for date from user
        String dateStr = scanner.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // try to create date variable
        LocalDate parsedDate;
        try { // if date format is wrong
            parsedDate = LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException("Invalid date format - please try again with the format: DD/MM/YYYY");
        }
        int count = 0; // count all avalialbe trucks in system
        for (String platenumber : trucks.keySet()) { // for each truck in database
            Truck truck = trucks.get(platenumber);
            if (truck.getAvailablity(parsedDate)) { // if truck is available at this date
                System.out.println(truck); // print its data
                count++; // count how many trucks are available
            }
        }
        if (count == 0) // if there is no available - message to user
            System.out.println("There is no available truck in system.");


    }

    /***
     * function that add prints all available trucks in specific date
     * @param trucks map of trucks and their plate numbers
     * @throws ATransportModuleException if input is invalid  / truck is not in system*/
    default void removeTruckFromSystem(Map<String, Truck> trucks, Scanner scanner) throws ATransportModuleException {
        if (trucks == null || scanner == null)
            throw new InvalidInputException();
        System.out.println("Please enter Truck's plate number you want to remove");
        String platenumber = scanner.nextLine();
        Truck truck = trucks.get(platenumber);
        if (truck == null)
            throw new InvalidInputException("Truck is not in system");
        trucks.remove(platenumber);
        System.out.println("Truck has successfully removed");

    }

    default void TruckManagerMenuMessage() {
        System.out.println("Welcome to Truck Manager Menu!");
        System.out.println("Please choose your next step");
        System.out.println("1. Add new Truck");
        System.out.println("2. Print all Trucks in system");
        System.out.println("3. Print all available Trucks in chosen date");
        System.out.println("4. Remove Truck from System");

        System.out.println("Press E to Exit.");
    }

    default void TruckManagerMenu(Map<String, Truck> trucks, Map<String, DrivingLicence> drivingLicences) throws ATransportModuleException {
        if (trucks == null || drivingLicences == null)
            throw new InvalidInputException();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            TruckManagerMenuMessage();
            String input = scanner.nextLine();
            try {


                switch (input) {
                    case "1": {
                        addNewTruckToSystem(scanner, trucks, drivingLicences);
                        break;
                    }
                    case "2": {
                        printAllTrucks(trucks);
                        break;
                    }
                    case "3":{
                        printAllAvailableTrukcs(trucks,scanner);
                        break;
                    }
                    case "4":
                    {
                        removeTruckFromSystem(trucks,scanner);
                        break;
                    }
                    case "E","e":
                    {
                        running = false;
                        System.out.println("Thank you for using Trucks menu");
                        break;
                    }
                    default:
                    {
                        System.out.println("Wrong input! Please try again.");
                        break;
                    }


                }
            } catch (Exception e) {
                System.out.println(e.getMessage());

            }
        }
    }


}
