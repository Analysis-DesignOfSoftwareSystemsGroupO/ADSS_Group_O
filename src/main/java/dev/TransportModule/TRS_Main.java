package TransportModule;

import java.util.*;

import TransportModule.Users.BookingUser;
import TransportModule.Users.TransportManagerUser;
import TransportModule.Users.TruckManagerUser;
import TransportModule.Users.User;

public class TRS_Main {

    private static void init_useres(Map<String, User> users) throws Exception {

        users.put("TransportManager", new TransportManagerUser("TransportManager"));
        users.put("User", new BookingUser("User"));
        users.put("TruckManagerUser", new TruckManagerUser("TruckManagerUser"));


    }



    public static void TRS_main(String[] args) {

        Map<String, User> users = new HashMap<>();
        try {
            init_useres(users);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }
        Scanner scanner = new Scanner(System.in);
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


        user.showMenu();
        scanner.close();

//        // Run every 1 minute the sendTransport function
//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//        scheduler.scheduleAtFixedRate(() -> {
//            try {
//                sendTransport(transportsPerDate);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }, 0, 1, TimeUnit.MINUTES);
    }

}