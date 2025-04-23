package HR_Mudol;

import HR_Mudol.domain.*;
import HR_Mudol.presentation.LoginScreen;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        Branch curBranch=new Branch();
        LoginScreen login = new LoginScreen();
        login.start(curBranch); // שלח את השבוע למסך ההתחברות
    }
}
