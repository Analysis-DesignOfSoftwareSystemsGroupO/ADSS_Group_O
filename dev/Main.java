import java.util.*;

import Users.BookingUser;
import Users.TransportManagerUser;
import Users.TruckManagerUser;
import Users.User;

public class Main {

    private static void init_useres(ArrayList<User> users){
        users.add(new TransportManagerUser("TransportManager","1234"));
        users.add(new BookingUser("User","1234"));
        users.add(new TruckManagerUser("admin","1234"));
    }
    private static User search_user(ArrayList<User> users, String name){
        for (User user: users){
            if(user.getUsername().equalsIgnoreCase(name))
                return user;
        }
        return null;
    }


    public static void main(String[] args) {

        ArrayList<User> users = new ArrayList<>();
        init_useres(users);
        Scanner scanner = new Scanner(System.in);
        int attempts = 3;
        User user;

        System.out.println("Welcome to Transport Department");
        while (true){
            System.out.println("Please enter user name: ");
            String name = scanner.nextLine();
            user = search_user(users,name);
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
            if(user.comparePassord(password)){
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