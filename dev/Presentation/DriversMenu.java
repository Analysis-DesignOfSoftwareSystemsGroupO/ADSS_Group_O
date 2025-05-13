package Presentation;

import DataAccess.DriverDto;
import transport_module.Driver;
import transport_module.DrivingLicence;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class DriversMenu {


    public static void addNewDriver( ){
        System.out.println("Enter name od the driver: ");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine(); // user input name
        System.out.println("Enter id of Driver: ");
        String id = scanner.nextLine();
        System.out.println("Enter Licence of Driver, Seperated by  ',' ");
        String licences = scanner.nextLine();
        String regex = "[,\\.\\s]";
        String[] licence = licences.split(regex);
        DriverDto driverDto = new DriverDto(name, id, licence);
    };

    default ArrayList<Driver> getAllDrivers(){
        //todo: make a data base and return all drivers from the data base
        return null;
    }

    default  Driver getAvailableDriver(Date date){
        //todo
    }

    default Driver getDriver(String id){
        //todo
    }






}
