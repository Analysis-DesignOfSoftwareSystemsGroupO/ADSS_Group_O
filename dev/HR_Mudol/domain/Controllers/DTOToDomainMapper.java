package HR_Mudol.domain.Controllers;

import HR_Mudol.DTO.*;
import HR_Mudol.domain.Level;
import HR_Mudol.domain.Objects.*;
import HR_Mudol.domain.ShiftType;
import HR_Mudol.domain.Status;
import HR_Mudol.domain.WeekDay;
import HR_Mudol.domain.repository.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DTOToDomainMapper {

    private static UserRepository userRepository;
    private static EmployeeRepository employeeRepository;
    private static RoleRepository roleRepository;
    private static WeekRepository weekRepository;

    public static void initialize(UserRepository userRepo,
                                  EmployeeRepository empRepo,
                                  RoleRepository roleRepo,
                                  WeekRepository weekRepo) {
        userRepository = userRepo;
        employeeRepository = empRepo;
        roleRepository = roleRepo;
        weekRepository = weekRepo;
    }

    public static User fromDTO(UserDTO dto) throws SQLException {

        Employee employee = employeeRepository.getById(dto.getUserId());
        return new User(employee, Level.valueOf(dto.getLevel()));
    }

    public static Employee fromDTO(EmployeeDTO dto) {
        return new Employee(
                dto.getFullName(),
                dto.getEmployeeId(),
                dto.getPassword(),
                dto.getBankAccount(),
                dto.getSalary(),
                dto.getStartDate(),
                dto.getMinDayShift(),
                dto.getMinEveningShift(),
                dto.getSickDays(),
                dto.getDaysOff()
        );
    }

    public static Role fromDTO(RoleDTO dto) {

        return new Role(dto.getDescription());
    }

    public static Constraint fromDTO(ConstraintDTO dto) {
        return new Constraint(
                dto.getExplanation(),
                WeekDay.valueOf(dto.getDay().toUpperCase()),
                ShiftType.valueOf(dto.getType().toUpperCase())
        );
    }

    public static Shift fromDTO(ShiftDTO dto) {

        Shift shift = new Shift(
                WeekDay.valueOf(dto.getDay().toUpperCase()),
                ShiftType.valueOf(dto.getType().toUpperCase())
        );
        shift.updateStatus(Status.valueOf(dto.getStatus().toUpperCase()));

        Employee shiftManager = employeeRepository.getById(dto.getShiftManagerId());
        shift.setShiftManager(shiftManager);

        for (RoleDTO roleDTO : dto.getNecessaryRoles()) {
            shift.addNecessaryRoles(fromDTO(roleDTO));
        }

        for (FilledRoleDTO filledRoleDTO : dto.getFilledRoles()) {
            Employee employee = employeeRepository.getById(filledRoleDTO.getEmployeeId());
            Role role = roleRepository.getRoleByNumber(filledRoleDTO.getRoleId());
            shift.addEmployee(employee, role);
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
        List<Integer> roleIds = new ArrayList<>();
        for (Role role : e.getRelevantRoles()) {
            roleIds.add(role.getRoleNumber());
        }

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

    public static ConstraintDTO toDTO(Constraint c, int ID) {
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
        Branch branch = new Branch(dto.getDistrict(), dto.getName());
        branch.setBranchID(dto.getBranchID());

        if (dto.getEmployees() != null) {
            for (EmployeeDTO empDTO : dto.getEmployees()) {
                branch.getEmployeeRepo().addFromDTO(fromDTO(empDTO));
            }
        }

        if (dto.getRoles() != null) {
            for (RoleDTO roleDTO : dto.getRoles()) {
                branch.getRoleRepo().add(fromDTO(roleDTO));
            }
        }

        if (dto.getWeeks() != null) {
            for (WeekDTO weekDTO : dto.getWeeks()) {
                branch.getWeekRepo().add(fromDTO(weekDTO));
            }
        }

        return branch;
    }

    public static BranchDTO toDTO(Branch branch) {
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        for (Employee employee : branch.getEmployeeRepo().getAll()) {
            employeeDTOs.add(toDTO(employee));
        }

        List<RoleDTO> roleDTOs = new ArrayList<>();
        for (Role role : branch.getRoleRepo().getAll()) {
            roleDTOs.add(toDTO(role));
        }

        List<WeekDTO> weekDTOs = new ArrayList<>();
        for (Week week : branch.getWeekRepo().getAll()) {
            List<ShiftDTO> shiftDTOs = new ArrayList<>();
            for (Shift shift : week.getShifts()) {
                shiftDTOs.add(toDTO(shift));
            }
            weekDTOs.add(new WeekDTO(week.getConstraintDeadline(), shiftDTOs));
        }

        return new BranchDTO(
                branch.getBranchID(),
                branch.getName(),
                branch.getDistrict(),
                employeeDTOs,
                roleDTOs,
                weekDTOs
        );
    }
    public static Week fromDTO(WeekDTO dto) {
        Week week = new Week();

        for (ShiftDTO shiftDTO : dto.getShifts()) {
            Shift shift = fromDTO(shiftDTO);
            week.addShift(shift);
        }

        return week;
    }

    public static UserDTO toDTO(User user) {
        if (user == null || user.getUser() == null || user.getLevel() == null)
            throw new IllegalArgumentException("User or user details cannot be null");

        long userId = user.getUser().getEmpId();
        String level = user.getLevel().name();
        return new UserDTO(userId, level);
    }

}
