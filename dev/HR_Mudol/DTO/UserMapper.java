package HR_Mudol.DTO;

import HR_Mudol.domain.Objects.User;
import HR_Mudol.domain.Objects.AbstractEmployee;
import HR_Mudol.domain.repository.EmployeeRepository;
import HR_Mudol.domain.Level;

public class UserMapper {

    // המרה מ-User (Domain) ל-UserDTO
    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getUser().getEmpId(),
                user.getLevel().name()
        );
    }

    // המרה מ-UserDTO ל-User (דורש גישה ל-EmployeeRepository)
    public static User fromDTO(UserDTO dto, EmployeeRepository empRepo) {
        AbstractEmployee emp = empRepo.getById(dto.getUserId());
        if (emp == null) {
            throw new IllegalArgumentException("No employee found with ID: " + dto.getUserId());
        }
        return new User(emp, Level.valueOf(dto.getLevel()));
    }
}
