package HR_Mudol.domain.Controllers;
import HR_Mudol.DTO.*;
import HR_Mudol.domain.Level;
import HR_Mudol.domain.Objects.*;
import HR_Mudol.domain.repository.*;
import HR_Mudol.domain.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DTOToDomainMapper {
    private final UserRepository userRepository;
    private static EmployeeRepository employeeRepository = null;
    private static RoleRepository roleRepository= null;
    private static WeekRepository weekRepository;

    public DTOToDomainMapper(UserRepository userRepository,
                             EmployeeRepository employeeRepository,
                             RoleRepository roleRepository, WeekRepository weekRepository) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.weekRepository=weekRepository;
    }

    public static Week fromDTO(WeekDTO dto) {
        Week week = new Week();

        for (ShiftDTO shiftDTO : dto.getShifts()) {
            Shift shift = fromDTO(shiftDTO);
            week.addShift(shift);
        }

        return week;
    }

    public User fromDTO(UserDTO dto) throws SQLException {
        User user = userRepository.getByEmployeeId(dto.getUserId());
        if (user != null) return user;

        Employee employee = employeeRepository.getById(dto.getUserId());
        User newUser = new User(employee, Level.valueOf(dto.getLevel()));
        return newUser;
    }

    public static Employee fromDTO(EmployeeDTO dto) {
        Employee emp = employeeRepository.getById(dto.getEmployeeId());
        if (emp != null) return emp;

        return new Employee(dto.getFullName(),dto.getEmployeeId(),dto.getPassword(),dto.getBankAccount(),dto.getSalary(),dto.getStartDate(),dto.getSalary(),dto.getMinEveningShift(),dto.getSickDays(),dto.getDaysOff());
    }

    public static Role fromDTO(RoleDTO dto) {
        Role role = roleRepository.getRoleByNumber(dto.getRoleNumber());
        if (role != null) return role;

        Role newRole = new Role(dto.getDescription());
        return newRole;
    }

    public static Constraint fromDTO(ConstraintDTO dto) {
        return new Constraint(
                dto.getExplanation(),
                WeekDay.valueOf(dto.getDay().toUpperCase()),
                ShiftType.valueOf(dto.getType().toUpperCase())
        );
    }

    public static Shift fromDTO(ShiftDTO dto) {
        Shift existing = weekRepository.getShiftById(dto.getShiftID());
        if (existing != null) return existing;

        Shift shift = new Shift(
                WeekDay.valueOf(dto.getDay().toUpperCase()),
                ShiftType.valueOf(dto.getType().toUpperCase())
        );
        shift.updateStatus(Status.valueOf(dto.getStatus().toUpperCase()));

        Employee shiftManager = employeeRepository.getById(dto.getShiftManagerId());
        shift.setShiftManager(shiftManager);

        // הוספת תפקידים דרושים
        for (RoleDTO roleDTO : dto.getNecessaryRoles()) {
            shift.addNecessaryRoles(fromDTO(roleDTO));
        }

        // הוספת תפקידי מילוי (FilledRoles)
        for (FilledRoleDTO filledRoleDTO : dto.getFilledRoles()) {
            Employee employee = employeeRepository.getById(filledRoleDTO.getEmployeeId());
            Role role=roleRepository.getRoleByNumber(filledRoleDTO.getRoleId());
            shift.addEmployee(employee,role);
        }

        return shift;
    }

    // --- toDTO ---

    public static ShiftDTO toDTO(Shift shift) {
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        for (Employee e : shift.getEmployees()) {
            employeeDTOs.add(toDTO(e));
        }

        List<RoleDTO> roleDTOs = new ArrayList<>();
        for (Role r : shift.getNecessaryRoles()) {
            roleDTOs.add(toDTO(r));
        }

        List<FilledRoleDTO> filledRoleDTOs = new ArrayList<>();
        for (FilledRole fr : shift.getFilledRoles()) {
            filledRoleDTOs.add(new FilledRoleDTO(
                    shift.getShiftID(),
                    fr.getEmployee().getEmpId(),
                    fr.getRole().getRoleNumber()
            ));
        }

        return new ShiftDTO(
                shift.getShiftID(),
                shift.getDay().name(),
                shift.getType().name(),
                shift.getStatus().name(),
                shift.getShiftManager() != null ? shift.getShiftManager().getEmpId() : -1,
                employeeDTOs,
                roleDTOs,
                filledRoleDTOs
        );
    }

    public static EmployeeDTO toDTO(Employee e) {

        // המרת List<Role> ל־List<Integer>
        List<Integer> roleIds = new ArrayList<>();
        for (Role role : e.getRelevantRoles()) {
            roleIds.add(role.getRoleNumber());
        }

        // המרת List<Constraint> ל־List<ConstraintDTO>
        List<ConstraintDTO> constraintDTOs = new ArrayList<>();
        for (Constraint c : e.getWeeklyConstraints()) {
            constraintDTOs.add(new ConstraintDTO(
                    e.getEmpId(),
                    c.getExplanation(),
                    c.getDay().name(),
                    c.getType().name()
            ));
        }

        return new EmployeeDTO(
                e.getEmpId(),
                e.getEmpName(),
                e.getEmpPassword(),
                e.getEmpBankAccount(),
                e.getEmpSalary(),
                e.getEmpStartDate(),
                e.getMinDayShift(),
                e.getMinEveninigShift(),
                e.getSickDays(),
                e.getDaysOff(),
                roleIds,
                constraintDTOs
        );

    }

    public static RoleDTO toDTO(Role r) {
        List<EmployeeDTO> relevantEmployees = new ArrayList<>();
        for (Employee e : r.getRelevantEmployees()) {
            relevantEmployees.add(toDTO(e));
        }

        return new RoleDTO(r.getRoleNumber(), r.getDescription(), relevantEmployees);
    }

    public static ConstraintDTO toDTO(Constraint c,int ID) {
        return new ConstraintDTO(
                ID,
                c.getExplanation(),
                c.getDay().name(),
                c.getType().name()
        );
    }

    public static EmploymentContractDTO toDTO(EmploymentContract contract, Employee employee) {
        return new EmploymentContractDTO(
                contract.getMinDayShift(employee),
                contract.getMinEveninigShift(employee),
                contract.getSickDays(employee),
                contract.getDaysOff(employee),
                employee.getEmpId()
        );
    }

    public static Branch fromDTO(BranchDTO dto) throws SQLException {
        // יוצרים את האובייקט עם name ו-district מתוך DTO
        Branch branch = new Branch(dto.getDistrict(), dto.getName());

        // שומרים על ה-ID המקורי של הסניף מה-DTO
        branch.setBranchID(dto.getBranchID());

        // מיפוי עובדים
        if (dto.getEmployees() != null) {
            for (EmployeeDTO empDTO : dto.getEmployees()) {
                branch.getEmployeeRepo().addFromDTO(fromDTO(empDTO));
            }
        }

        // מיפוי תפקידים
        if (dto.getRoles() != null) {
            for (RoleDTO roleDTO : dto.getRoles()) {
                branch.getRoleRepo().add(fromDTO(roleDTO));
            }
        }

        // מיפוי שבועות
        if (dto.getWeeks() != null) {
            for (WeekDTO weekDTO : dto.getWeeks()) {
                branch.getWeekRepo().add(fromDTO(weekDTO));
            }
        }

        return branch;
    }


}
