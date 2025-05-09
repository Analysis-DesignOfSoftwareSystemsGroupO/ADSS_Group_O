package Users;
import Transport_Module_Exceptions.ATransportModuleException;
import Transport_Module_Exceptions.InvalidInputException;
import transport_module.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Scanner;

public interface ITruckManager {

    default void addNewTruckToSystem(Scanner scanner, Truck[] trucks,DrivingLicence[] drivingLicence) throws ATransportModuleException {
        if(trucks == null || scanner == null || drivingLicence == null)
            throw new InvalidInputException();
        System.out.println("Please enter Truck's plate number");
        String platenumber = scanner.nextLine();
        for (Truck truck: trucks){
            if (Objects.equals(truck.getPlateNumber(), platenumber))
                throw new InvalidInputException("Truck is already in system");
        }
        System.out.println("Please enter driving licence code");
        String drivinglincenceCode = scanner.nextLine();

    }
    default void printAllTrucks(Truck[] trucks) throws ATransportModuleException{
            if (trucks == null)
                throw new InvalidInputException("Trucks array is null");
            for(Truck truck : trucks){
                System.out.println(truck);
            }
    }
    default void  printAllAvailableTrukcs(Truck[] trucks, Scanner scanner){
        System.out.println("Please enter date by format: DD/MM/YYYY"); // ask for date from user
        String dateStr = scanner.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // try to create date variable
        LocalDate parsedDate;
        try { // if date format is wrong
            parsedDate = LocalDate.parse(dateStr, formatter);
        }
        catch (DateTimeParseException e){
            System.out.println("Invalid date format - please try again with the format: DD/MM/YYYY"); // message to user and ask him to try again
            return;
        }
        int count = 0; // count all avalialbe trucks in system
        for (Truck truck : trucks) { // for each truck in database
            if (truck.getAvailablity(parsedDate)) { // if truck is available at this date
                System.out.println(truck); // print its data
                count++; // count how many trucks are available
            }
        }
        if (count == 0) // if there is no available - message to user
            System.out.println("There is no available truck in system.");


    }

    default void removeTruckFromSystem(Truck truck, Truck[] trucks){

    }

    default void TruckManagerMenuMessage(){
        System.out.println("Welcome to Truck Manager Menu!");
        System.out.println("Please choose your next step");
        System.out.println("1. Add new Truck");
        System.out.println("2. Print all Trucks in system");
        System.out.println("3. Print all available Trucks in chosen date");
        System.out.println("4. Remove Truck from System");

        System.out.println("Press E to Exit.");
    }


}
