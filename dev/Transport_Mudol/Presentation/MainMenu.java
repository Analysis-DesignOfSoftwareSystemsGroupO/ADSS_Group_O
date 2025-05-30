package Presentation;
import Transport_Module_Exceptions.ATransportModuleException;
import Transport_Module_Exceptions.InvalidUserException;
import Users.User;

import java.util.Objects;
import java.util.Scanner;

public class MainMenu {

    public static void login(Scanner scanner, User[] Users) throws ATransportModuleException {
        System.out.println("Welcome to the Transportation Department!");
        System.out.println("Please enter User name: ");
        String username = scanner.nextLine();
        System.out.println("Please enter Password: ");
        String password = scanner.nextLine();

        for(var user : Users){

            if(Objects.equals(user.getUsername(), username)){
               if(!user.comparePassord(password))
                   throw new InvalidUserException("Wrong password");
               return;
            }

        }
        throw new InvalidUserException("Username is not in System");



    }

    public static void menu_message() {
        System.out.println("Please choose your next step");
        System.out.println("1. Add new transport");
        System.out.println("2. Create new delivery document");
        System.out.println("3. Add product to document");
        System.out.println("4. Remove product from document");
        System.out.println("5. Add driver to transport");
        System.out.println("6. Attach document to transport");
        System.out.println("7. Send transport manually");
        System.out.println("8. Print Transport details");
        System.out.println("9. Print all available trucks");
        System.out.println("10. Print document details");
        System.out.println("11. Add new Truck");
        System.out.println("12. Add new Site");
        System.out.println("13. Add new Product");


        System.out.println("Press E to Exit.");


    }
}
