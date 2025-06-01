package TransportModule.Users;

import TransportModule.Presentation.BookingMenu;

public class BookingUser extends User {
    private final BookingMenu bookingMenu;

    public BookingUser(String username,String password) throws Exception {
        super(username,password);
        this.bookingMenu = new BookingMenu();
    }

    @Override
    public void showMenu() {
        bookingMenu.showMenu();
    }
}

