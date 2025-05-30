package HR_Mudol.presentation;

import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DTO.UserDTO;
import HR_Mudol.domain.Objects.Branch;
import HR_Mudol.domain.Objects.Employee;
import HR_Mudol.domain.Objects.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class LoginScreen {

    private final List<Branch> allBranches;

    public LoginScreen(List<Branch> branches) {
        this.allBranches = branches;
    }

    public void start() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // שלב 1: התחברות
            System.out.print("Enter employee ID: ");
            String idInput = scanner.nextLine();
            int id;

            try {
                id = Integer.parseInt(idInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID format. Please enter numbers only.\n");
                continue;
            }

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            // שלב 2: חיפוש המשתמש בכל הסניפים
            User matched = null;
            Branch userBranch = null;

            for (Branch branch : allBranches) {
                User u = branch.getUserRepo().getByCredentials(id, password);
                if (u != null) {
                    matched = u;
                    userBranch = branch;
                    break;
                }
            }

            if (matched == null) {
                System.out.println("Invalid credentials. Please try again.\n");
                continue;
            }

            // שלב 3: אימות מול סניפים מותרים ליוזר
            List<Branch> userBranches = matched.isManager()
                    ? allBranches
                    : allBranches.stream()
                    .filter(b -> {
                        try {
                            return b.getUserRepo().getAll().stream().anyMatch(u -> u.getUser().getEmpId() == id);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());

            Branch selectedBranch = selectBranch(scanner, userBranches);
            if (selectedBranch == null) {
                System.out.println("Invalid branch selection.");
                continue;
            }

            // שלב 4: פתיחת תפריט
            launchMenuForUser(matched, selectedBranch);
        }
    }

    private Branch selectBranch(Scanner scanner, List<Branch> userBranches) {
        System.out.println("Select your branch:");
        for (int i = 0; i < userBranches.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, userBranches.get(i).getName());
        }

        System.out.print("Enter choice: ");
        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index >= 0 && index < userBranches.size()) {
                return userBranches.get(index);
            }
        } catch (NumberFormatException ignored) {}
        return null;
    }

    private User findUser(int id, String password, List<User> users) {
        for (User u : users) {
            if (u.getUser().getEmpId() == id && u.getUser().getEmpPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    private void launchMenuForUser(User matched, Branch curBranch) {
        // יצירת DTO עבור המשתמש
        UserDTO userDTO = new UserDTO(matched.getUser().getEmpId(), matched.getLevel().name());

        EmployeeDTO employeeDTO = null;
        if (matched.getUser() instanceof Employee emp) {
            employeeDTO = new EmployeeDTO(
                    emp.getEmpId(),
                    emp.getEmpName(),
                    emp.getEmpPassword(),
                    emp.getEmpBankAccount(),
                    emp.getEmpSalary(),
                    emp.getEmpStartDate(),
                    emp.getMinDayShift(),
                    emp.getMinEveninigShift(),
                    emp.getSickDays(),
                    emp.getDaysOff()
            );
        }

        // מעבר לתפריט המתאים לפי סוג המשתמש
        if (matched.isManager()) {
            HRManagerMenu menu = new HRManagerMenu();
            if (menu.start(userDTO, employeeDTO, curBranch)) return;
        } else if (matched.isShiftManager()) {
            ShiftManagerMenu menu = new ShiftManagerMenu();
            if (menu.start(userDTO, employeeDTO, curBranch)) return;
        } else {
            EmployeeMenu menu = new EmployeeMenu();
            if (menu.start(userDTO, employeeDTO, curBranch)) return;
        }
    }


}
