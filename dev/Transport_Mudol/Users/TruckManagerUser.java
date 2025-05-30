package Users;

import Presentation.TruckManagerMenu;

public class TruckManagerUser extends User{
        private final TruckManagerMenu managerMenu;

        public TruckManagerUser(String username, String password,TruckManagerMenu menu) {
            super(username,password);
            this.managerMenu = menu;
        }

        @Override
        public void showMenu() {
            managerMenu.showMenu();
        }
}
