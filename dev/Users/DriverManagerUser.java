package Users;

import Presentation.DriversMenu;



import Presentation.TruckManagerMenu;

public class DriverManagerUser extends User{
    private final DriversMenu managerMenu;

    public DriverManagerUser(String username, String password,DriversMenu menu) {
        super(username,password);
        this.managerMenu = menu;
    }

    @Override
    public void showMenu() {
        managerMenu.showMenu();
    }
}
