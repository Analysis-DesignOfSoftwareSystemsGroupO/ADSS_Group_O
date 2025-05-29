package HR_Mudol.domain.Controllers;
import HR_Mudol.DTO.*;
import HR_Mudol.domain.Objects.*;
import HR_Mudol.domain.repository.*

public class DTOToDomainMapper {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;

    public DTOToDomainMapper(UserRepository userRepository,
                             EmployeeRepository employeeRepository,
                             RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
    }

    // ... (fromDTO for User, Employee, Role, Constraint, Shift)

    public Week fromDTO(WeekDTO dto) {
        Week week = new Week();
        week.setWeekNumber(dto.getWeekNumber());

        for (ShiftDTO shiftDTO : dto.getShifts()) {
            Shift shift = fromDTO(shiftDTO);
            week.addShift(shift);
        }

        return week;
    }
    public User fromDTO(UserDTO dto) {
        User user = userRepository.getById(dto.getId());
        if (user != null) return user;

        Employee employee = fromDTO(dto.getEmployee());
        User newUser = new User(dto.getId(), dto.getUsername(), dto.getPassword(), dto.getPhone(), dto.getEmail(), employee, dto.isManager());
        userRepository.add(newUser);
        return newUser;
    }

    public Employee fromDTO(EmployeeDTO dto) {
        Employee emp = employeeRepository.getById(dto.getId());
        if (emp != null) return emp;

        Employee newEmp = new Employee(dto.getId(), dto.getEmpName());
        employeeRepository.add(newEmp);
        return newEmp;
    }

    public Role fromDTO(RoleDTO dto) {
        Role role = roleRepository.getById(dto.getRoleNumber());
        if (role != null) return role;

        Role newRole = new Role(dto.getRoleNumber(), dto.getDescription());
        roleRepository.add(newRole);
        return newRole;
    }

    public Constraint fromDTO(ConstraintDTO dto) {
        return new Constraint(
                dto.getExplanation(),
                WeekDay.valueOf(dto.getDay().toUpperCase()),
                ShiftType.valueOf(dto.getType().toUpperCase())
        );
    }

    public Shift fromDTO(ShiftDTO dto) {
        Shift shift = new Shift(dto.getDay(), dto.getType());
        shift.setShiftID(dto.getShiftID());
        shift.setStatus(dto.getStatus());

        // הוספת עובדים
        List<Employee> employees = dto.getEmployeeIds().stream()
                .map(employeeRepository::getById)
                .collect(Collectors.toList());
        shift.setEmployees(employees);

        // הוספת תפקידים דרושים
        List<Role> necessaryRoles = dto.getNecessaryRoleIds().stream()
                .map(roleRepository::getById)
                .collect(Collectors.toList());
        shift.setNecessaryRoles(necessaryRoles);

        return shift;
    }
    public ShiftDTO toDTO(Shift shift) {
        return new ShiftDTO(
                shift.getShiftID(),
                shift.getDay().name(),              // assuming WeekDay enum
                shift.getType().name(),             // assuming ShiftType enum
                shift.getStatus().name(),           // assuming Status enum
                shift.getShiftManager() != null ? shift.getShiftManager().getEmpId() : -1
        );
    }
    public static EmployeeDTO toDTO(Employee e) {
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
                e.getDaysOff()
        );
    }

    public static RoleDTO toDTO(Role r) {
        return new RoleDTO(
                r.getRoleNumber(),
                r.getDescription()
        );
    }
}
