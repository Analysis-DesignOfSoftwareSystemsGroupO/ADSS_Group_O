package Users;

import Presentation.TransportManagerMenu;

public class TransportManagerUser extends User {
    private final TransportManagerMenu managerMenu;

    public TransportManagerUser(String username, String password,TransportManagerMenu menu) {
        super(username,password);
        this.managerMenu = menu;
    }

    @Override
    public void showMenu() {
        managerMenu.showMenu();
    }
}
