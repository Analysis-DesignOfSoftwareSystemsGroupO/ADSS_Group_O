package HR_Mudol.domain.repository;

import HR_Mudol.DAO.*;
import HR_Mudol.DTO.*;
import HR_Mudol.domain.*;
import HR_Mudol.domain.Objects.*;

import java.sql.SQLException;
import java.util.*;

public class UserRepository {
    private final Map<Integer, User> usersByEmployeeId = new HashMap<>();
    private final IUserDAO userDAO;
    private EmployeeRepository employeeRepo;

    public UserRepository(IUserDAO userDAO, EmployeeRepository employeeRepo) {
        this.userDAO = userDAO;
        this.employeeRepo = employeeRepo;
    }

    public void add(User user) throws SQLException {
        int empId = user.getUser().getEmpId();

        if (exists(empId)) return; // avoid duplicates

        usersByEmployeeId.put(empId, user); //RAM
        userDAO.insert(toDTO(user)); // DB

    }

    public void remove(User user) throws SQLException {
        int empId = user.getUser().getEmpId();
        usersByEmployeeId.remove(empId);//RAM
        userDAO.delete(empId); // Remove from DB
    }

    public boolean exists(int empId) throws SQLException {
        if (usersByEmployeeId.containsKey(empId)) return true;
        return userDAO.exists(empId);
    }

    public List<User> getAll() throws SQLException {
        if (usersByEmployeeId.isEmpty()) {
            List<UserDTO> dtos = userDAO.getAll();
            for (UserDTO dto : dtos) {
                User user = fromDTO(dto,employeeRepo);
                usersByEmployeeId.put(user.getUser().getEmpId(), user);
            }
        }
        return new ArrayList<>(usersByEmployeeId.values());
    }

    public User getByEmployeeId(int empId) throws SQLException {

        if (usersByEmployeeId.containsKey(empId)) {
            return usersByEmployeeId.get(empId);
        }

        UserDTO dto = userDAO.get(empId);
        if (dto == null) return null;

        User user = fromDTO(dto,employeeRepo);
        usersByEmployeeId.put(empId, user);
        return user;
    }



    private UserDTO toDTO(User user) {
        return new UserDTO(
                user.getUser().getEmpId(),
                user.getLevel().name()
        );
    }

    private User fromDTO(UserDTO dto, EmployeeRepository empRepo) {
        AbstractEmployee emp = empRepo.getById(dto.getUserId());
        if (emp == null) {
            throw new IllegalArgumentException("No employee found with ID: " + dto.getUserId());
        }
        return new User(emp, Level.valueOf(dto.getLevel()));
    }
    public User getByCredentials(int empId, String password) throws SQLException {
        User user = getByEmployeeId(empId);
        if (user != null && user.getUser().getEmpPassword().equals(password)) {
            return user;
        }
        return null;
    }
    public Level getLevelById(int employeeId) {
        User user = usersByEmployeeId.get(employeeId);
        if (user == null) throw new IllegalArgumentException("User not found");
        return user.getLevel();
    }



}
