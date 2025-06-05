package TransportModule.Users;

import TransportModule.Presentation.TruckManagerMenu;

public class TruckManagerUser extends User{
    private final TruckManagerMenu managerMenu;

    public TruckManagerUser(String username) throws Exception {
        super(username);
        this.managerMenu = new TruckManagerMenu();
    }

    @Override
    public void showMenu() {
        managerMenu.showMenu();
    }
}