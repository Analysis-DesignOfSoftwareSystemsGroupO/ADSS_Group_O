package HRModule.DTO;

import HR_Mudol.DAO.ConstraintDAOImpl;
import HR_Mudol.DAO.RoleDAOImpl;
import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.domain.Controllers.DTOToDomainMapper;
import HR_Mudol.domain.repository.EmployeeRepository;
import HR_Mudol.domain.repository.RoleRepository;
import HR_Mudol.domain.repository.UserRepository;
import HR_Mudol.domain.repository.WeekRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

public class EmployeeDTOTest {

    @BeforeEach
    public void setup() throws Exception {
        // יצירת mocks של הרפוזיטוריז
        UserRepository userRepo = mock(UserRepository.class);
        EmployeeRepository empRepo = mock(EmployeeRepository.class);
        RoleRepository roleRepo = mock(RoleRepository.class);
        WeekRepository weekRepo = mock(WeekRepository.class);

        // אתחול DTOToDomainMapper עם mocks
        DTOToDomainMapper.initialize(userRepo, empRepo, roleRepo, weekRepo);

        // הזרקת mocks גם ל־roleDAO ו־constraintDAO באמצעות רפלקציה
        Field roleDAOField = DTOToDomainMapper.class.getDeclaredField("roleDAO");
        roleDAOField.setAccessible(true);
        roleDAOField.set(null, mock(RoleDAOImpl.class));

        Field constraintDAOField = DTOToDomainMapper.class.getDeclaredField("constraintDAO");
        constraintDAOField.setAccessible(true);
        constraintDAOField.set(null, mock(ConstraintDAOImpl.class));
    }

    @Test
    public void testToStringDoesNotThrow() {
        EmployeeDTO dto = new EmployeeDTO(123456789, "Test", "pass", "IL12-3456", 5000,
                LocalDate.now(), 1, 1, 5, 2);
        assertDoesNotThrow(dto::toString);
    }
}
