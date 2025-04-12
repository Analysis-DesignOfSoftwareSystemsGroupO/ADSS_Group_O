import java.util.Scanner;
import transport_module.*;

public class Main {
    public static void menu_message(){
        System.out.println("Welcome to the Transportation Department!");
        System.out.println("Please choose your next");
        System.out.println("1. Add new transport");
        System.out.println("2. Print all Trucks");
        System.out.println("3. Print all Drivers");
        System.out.println("3. Print all Drivers");


    }
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            menu_message();

        }

    }

}
