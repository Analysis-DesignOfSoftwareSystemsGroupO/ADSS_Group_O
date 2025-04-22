package HR_Mudol;

import HR_Mudol.domain.Week;
import HR_Mudol.presentation.LoginScreen;

public class Main {
    public static void main(String[] args) {
        Week currentWeek = new Week(); // צור מופע חדש של שבוע העבודה
        LoginScreen login = new LoginScreen();
        login.start(currentWeek); // שלח את השבוע למסך ההתחברות
    }

}
