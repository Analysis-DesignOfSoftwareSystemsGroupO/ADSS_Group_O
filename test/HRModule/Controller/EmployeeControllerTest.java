package HRModule.Controller;

import HR_Mudol.DTO.*;
import HR_Mudol.domain.Controllers.DTOToDomainMapper;
import HR_Mudol.domain.Controllers.EmployeeController;
import HR_Mudol.domain.Level;
import HR_Mudol.domain.Objects.*;
import HR_Mudol.domain.repository.EmployeeRepository;
import HR_Mudol.domain.repository.UserRepository;
import HR_Mudol.domain.repository.RoleRepository;
import HR_Mudol.domain.repository.WeekRepository;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeControllerTest {

    private EmployeeController controller;
    private EmployeeRepository employeeRepo;
    private UserRepository userRepo;
    private RoleRepository roleRepo;
    private WeekRepository weekRepo;
    private Branch branch;
    private BranchDTO branchDTO;
    private MockedStatic<DTOToDomainMapper> staticMock;
    @BeforeEach
    public void setup() throws SQLException {
        employeeRepo = mock(EmployeeRepository.class);
        userRepo = mock(UserRepository.class);
        roleRepo = mock(RoleRepository.class);
        weekRepo = mock(WeekRepository.class);
        branch = mock(Branch.class);
        branchDTO = mock(BranchDTO.class);

        staticMock = mockStatic(DTOToDomainMapper.class);
        staticMock.when(() -> DTOToDomainMapper.fromDTO(any(BranchDTO.class))).thenReturn(branch);
        staticMock.when(() -> DTOToDomainMapper.initialize(any(), any(), any(), any())).thenAnswer(i -> null);
        staticMock.when(() -> DTOToDomainMapper.fromDTO(any(UserDTO.class))).thenAnswer(inv -> {
            UserDTO dto = inv.getArgument(0);
            return new User(new Employee("Test", dto.getUserId(), "pass", "IL123", 5000,
                    LocalDate.now(), 1, 1, 5, 2), Level.valueOf(dto.getLevel()));
        });
        staticMock.when(() -> DTOToDomainMapper.toDTO(any(Employee.class))).thenAnswer(inv -> {
            Employee e = inv.getArgument(0);
            return new EmployeeDTO(e.getEmpId(), e.getEmpName(), e.getEmpPassword(), e.getEmpBankAccount(),
                    e.getEmpSalary(), e.getEmpStartDate(), e.getMinDayShift(), e.getMinEveninigShift(),
                    e.getSickDays(), e.getDaysOff());
        });
        staticMock.when(() -> DTOToDomainMapper.fromDTO(any(RoleDTO.class))).thenAnswer(inv -> {
            RoleDTO dto = inv.getArgument(0);
            return new HR_Mudol.domain.Objects.Role(dto.getDescription());
        });

        when(branch.getEmployeeRepo()).thenReturn(employeeRepo);
        when(branch.getUserRepo()).thenReturn(userRepo);
        when(branch.getRoleRepo()).thenReturn(roleRepo);
        when(branch.getWeekRepo()).thenReturn(weekRepo);

        controller = new EmployeeController(branchDTO);
    }

    @AfterEach
    public void tearDown() {
        staticMock.close();
    }

    @Test
    public void testGetEmployeeById_Success() throws SQLException {
        long empId = 123456789L;
        Employee emp = new Employee("Alice", empId, "pass", "IL123", 5000, LocalDate.now(), 2, 2, 10, 5);
        when(employeeRepo.getById(empId)).thenReturn(emp);

        when(userRepo.getByEmployeeId(empId)).thenReturn(new User(emp, Level.regularEmp));
        UserDTO caller = new UserDTO(empId, Level.regularEmp.name());
        EmployeeDTO result = controller.getEmployeeById(caller, empId);
        assertNotNull(result);
        assertEquals(empId, result.getEmployeeId());
    }

    @Test
    public void testVerifyPassword_WrongPassword() {
        long empId = 123456789L;
        Employee emp = new Employee("Bob", empId, "correctPass", "IL12", 6000, LocalDate.now(), 2, 2, 5, 3);
        when(employeeRepo.getById(empId)).thenReturn(emp);

        UserDTO caller = new UserDTO(empId, Level.regularEmp.name());
        assertFalse(controller.verifyPassword(caller, empId, "wrongPass"));
    }

    @Test
    public void testVerifyPassword_UnauthorizedUser() {
        long empId = 123456789L;
        Employee emp = new Employee("Charlie", empId, "secure123", "IL22", 7000, LocalDate.now(), 2, 2, 5, 3);
        when(employeeRepo.getById(empId)).thenReturn(emp);

        UserDTO otherCaller = new UserDTO(999999999L, Level.regularEmp.name());
        assertFalse(controller.verifyPassword(otherCaller, empId, "secure123"));
    }

    @Test
    public void testUpdateSalary_AsManager() throws Exception {
        long empId = 123456789L;
        Employee emp = new Employee("Dana", empId, "1234", "IL99", 5000, LocalDate.of(2023, 1, 1), 2, 2, 10, 5);
        User caller = new User(emp, Level.HRManager);
        UserDTO callerDTO = new UserDTO(empId, Level.HRManager.name());

        when(employeeRepo.exists((int) empId)).thenReturn(true);
        when(employeeRepo.getById(empId)).thenReturn(emp);

        Scanner mockScanner = new Scanner(empId + "\n" + "8000\n");
        var scannerField = EmployeeController.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(controller, mockScanner);

        controller.updateSalary(callerDTO);
        verify(employeeRepo).updateSalary(any(), eq((int) empId), eq(8000));
    }


    @Test
    public void testGetContractDetails_AsSelf() throws SQLException {
        long empId = 123456789L;
        Employee emp = new Employee("Eli", empId, "mypwd", "IL88", 6000,
                LocalDate.now(), 2, 3, 5, 6); // minDayShift = 2, minEveningShift = 3, sickDays = 5, daysOff = 6

        // ודא שהחוזה נוצר
        assertNotNull(emp.getContract(), "Contract should not be null");

        when(employeeRepo.getById(empId)).thenReturn(emp);
        when(userRepo.getByEmployeeId(empId)).thenReturn(new User(emp, Level.regularEmp));

        // הוספת מיפוי ל־DTO
        staticMock.when(() -> DTOToDomainMapper.toDTO(any(EmploymentContract.class), eq(emp)))
                .thenAnswer(inv -> {
                    EmploymentContract contract = inv.getArgument(0);
                    return new EmploymentContractDTO(contract.getMinDayShift(emp), contract.getMinEveninigShift(emp), contract.getSickDays(emp), contract.getDaysOff(emp),empId);
                });

        UserDTO caller = new UserDTO(empId, Level.regularEmp.name());
        EmploymentContractDTO contractDTO = controller.getContractDetails(caller, empId);

        assertNotNull(contractDTO);
        assertEquals(2, contractDTO.getMinDayShift());
        assertEquals(3, contractDTO.getMinEveningShift());
    }



    @Test
    public void testGetRolesForEmployee() {
        long empId = 987654321L;
        Employee emp = new Employee("Roley", empId, "rolepwd", "IL55", 7000, LocalDate.now(), 2, 2, 3, 2);
        RoleDTO r1 = new RoleDTO("Manager");
        RoleDTO r2 = new RoleDTO("Cashier");

        // סימולציה למיפוי RoleDTO -> Role
        staticMock.when(() -> DTOToDomainMapper.fromDTO(any(RoleDTO.class))).thenAnswer(inv -> {
            RoleDTO dto = inv.getArgument(0);
            return new Role(dto.getDescription());
        });

        // סימולציה למיפוי Role -> RoleDTO
        staticMock.when(() -> DTOToDomainMapper.toDTO(any(Role.class))).thenAnswer(inv -> {
            Role role = inv.getArgument(0);
            return new RoleDTO(role.getDescription());
        });

        emp.addNewRole(new User(emp, Level.HRManager), DTOToDomainMapper.fromDTO(r1));
        emp.addNewRole(new User(emp, Level.HRManager), DTOToDomainMapper.fromDTO(r2));
        when(employeeRepo.getById(empId)).thenReturn(emp);

        List<RoleDTO> roles = controller.getRolesForEmployee(empId);
        assertEquals(2, roles.size());
        assertTrue(roles.stream().anyMatch(r -> "Manager".equals(r.getDescription())));
        assertTrue(roles.stream().anyMatch(r -> "Cashier".equals(r.getDescription())));
    }


}