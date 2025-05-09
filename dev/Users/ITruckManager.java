package Users;
import Transport_Module_Exceptions.ATransportModuleException;
import transport_module.*;

import java.util.Scanner;

public interface ITruckManager {

    default void addNewTruckToSystem(Truck truck, Truck[] trucks) throws ATransportModuleException {

    }
    default void printAllTrucks(Truck[] trucks){

    }
    default void  printAllAvailableTrukcs(Truck[] trucks, Scanner scanner){

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
