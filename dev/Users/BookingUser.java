package Users;

import Presentation.BookingMenu;

public class BookingUser extends User {
    private final BookingMenu bookingMenu;

    public BookingUser(String username,String password) {
        super(username,password);
        this.bookingMenu = new BookingMenu();
    }

    @Override
    public void showMenu() {
        bookingMenu.showMenu();
    }
}

