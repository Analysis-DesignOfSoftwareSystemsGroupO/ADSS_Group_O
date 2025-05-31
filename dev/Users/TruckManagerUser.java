package Users;

import Presentation.TruckManagerMenu;

public class TruckManagerUser extends User{
        private final TruckManagerMenu managerMenu;

        public TruckManagerUser(String username, String password) {
            super(username,password);
            this.managerMenu = new TruckManagerMenu();
        }

        @Override
        public void showMenu() {
            managerMenu.showMenu();
        }
}
