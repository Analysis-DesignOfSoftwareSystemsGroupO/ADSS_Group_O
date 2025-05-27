package HR_Mudol.domain.repository;

import HR_Mudol.DAO.IUserRepositoryDAO;
import HR_Mudol.DTO.UserDTO;
import HR_Mudol.DTO.UserMapper;
import HR_Mudol.domain.Objects.User;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserRepository {
    private final List<User> users = new LinkedList<>();

    public void add(User user) {
        users.add(user);
    }

    public void remove(User user) {
        users.remove(user);
    }

    public List<User> getAll() {
        return new LinkedList<>(users);
    }
    public static void loadUsersFromDAOToDomain(
            IUserRepositoryDAO userRepoDAO,
            EmployeeRepository employeeRepo,
            UserRepository userRepo
    ) throws SQLException {
        for (UserDTO dto : userRepoDAO.getAll()) {
            User user = UserMapper.fromDTO(dto, employeeRepo);
            userRepo.add(user);
        }
    }
}

