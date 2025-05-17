package Users;

import Presentation.BookingMenu;

public class BookingUser extends User {
    private final BookingMenu bookingMenu;

    public BookingUser(String username,String password, BookingMenu menu) {
        super(username,password);
        this.bookingMenu = menu;
    }

    @Override
    public void showMenu() {
        bookingMenu.showMenu();
    }
}

