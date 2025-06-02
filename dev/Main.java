import java.util.*;

import TransportModule.Users.BookingUser;
import TransportModule.Users.TransportManagerUser;
import TransportModule.Users.TruckManagerUser;
import TransportModule.Users.User;

public class Main {

    private static void init_useres(Map<String, User> users) throws Exception {

            users.put("TransportManager", new TransportManagerUser("TransportManager", "1234"));
            users.put("User", new BookingUser("User", "1234"));
            users.put("TruckManagerUser", new TruckManagerUser("TruckManagerUser", "1234"));


    }



    public static void main(String[] args) {

        Map<String, User> users = new HashMap<>();
        try {
            init_useres(users);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }
        Scanner scanner = new Scanner(System.in);
        int attempts = 3;
        User user;

        System.out.println("Welcome to Transport Department");
        while (true){
            System.out.println("Please enter user name: ");
            String name = scanner.nextLine();
            user = users.get(name.toLowerCase());
            if (user == null){
                System.out.println("User is not exist. Please try again");
            }
            else {
                break;
            }
        } // End username check while

        while (true){
            if(attempts == 0){
                System.out.println("You have no more attempts. Goodbye!");
                scanner.close();
                return;
            }
            System.out.println("Please enter password: ");
            String password = scanner.nextLine();
            if(user.comparePassword(password)){
                break;
            }
            else{
                System.out.println("Wrong password. Please try again");
                attempts--;
                System.out.println("You have "+attempts+" attempts to try");
            }
        } // End password check while

        user.showMenu();
        scanner.close();


    }

}




//        // Run every 1 minute the sendTransport function
//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//        scheduler.scheduleAtFixedRate(() -> {
//            try {
//                sendTransport(transportsPerDate);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }, 0, 1, TimeUnit.MINUTES);