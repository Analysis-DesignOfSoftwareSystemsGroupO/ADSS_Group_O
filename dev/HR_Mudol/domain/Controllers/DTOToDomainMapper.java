//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package HR_Mudol.domain.Controllers;

import HR_Mudol.DAO.ConstraintDAOImpl;
import HR_Mudol.DAO.RoleDAOImpl;
import HR_Mudol.DTO.BranchDTO;
import HR_Mudol.DTO.ConstraintDTO;
import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DTO.EmploymentContractDTO;
import HR_Mudol.DTO.FilledRoleDTO;
import HR_Mudol.DTO.RoleDTO;
import HR_Mudol.DTO.ShiftDTO;
import HR_Mudol.DTO.UserDTO;
import HR_Mudol.DTO.WeekDTO;
import HR_Mudol.domain.Level;
import HR_Mudol.domain.ShiftType;
import HR_Mudol.domain.Status;
import HR_Mudol.domain.WeekDay;
import HR_Mudol.domain.Objects.Branch;
import HR_Mudol.domain.Objects.Constraint;
import HR_Mudol.domain.Objects.Employee;
import HR_Mudol.domain.Objects.EmploymentContract;
import HR_Mudol.domain.Objects.FilledRole;
import HR_Mudol.domain.Objects.Role;
import HR_Mudol.domain.Objects.Shift;
import HR_Mudol.domain.Objects.User;
import HR_Mudol.domain.Objects.Week;
import HR_Mudol.domain.repository.EmployeeRepository;
import HR_Mudol.domain.repository.RoleRepository;
import HR_Mudol.domain.repository.UserRepository;
import HR_Mudol.domain.repository.WeekRepository;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;



public class DTOToDomainMapper {
    private static UserRepository userRepository;
    private static EmployeeRepository employeeRepository;
    private static RoleRepository roleRepository;
    private static WeekRepository weekRepository;
    private static RoleDAOImpl roleDAO;
    private static ConstraintDAOImpl constraintDAO;

    public DTOToDomainMapper() {
    }

    public static void initialize(UserRepository userRepo, EmployeeRepository empRepo, RoleRepository roleRepo, WeekRepository weekRepo) throws SQLException {
        userRepository = userRepo;
        employeeRepository = empRepo;
        roleRepository = roleRepo;
        weekRepository = weekRepo;
        roleDAO = new RoleDAOImpl();
        constraintDAO = new ConstraintDAOImpl();
    }

    public static User fromDTO(UserDTO dto) throws SQLException {
        Employee employee = employeeRepository.getById((long)dto.getUserId());
        return new User(employee, Level.valueOf(dto.getLevel()));
    }

    public static Employee fromDTO(EmployeeDTO dto) {
        Employee e = new Employee(dto.getFullName(), dto.getEmployeeId(), dto.getPassword(), dto.getBankAccount(), dto.getSalary(), dto.getStartDate(), dto.getMinDayShift(), dto.getMinEveningShift(), dto.getSickDays(), dto.getDaysOff());
        List<Role> roles = new ArrayList();
        Iterator var3 = roleDAO.getRolesByEmpId(dto.getEmployeeId()).iterator();

        while(var3.hasNext()) {
            RoleDTO r = (RoleDTO)var3.next();
            roles.add(fromDTO(r));
        }

        e.setRelevantRoles(roles);
        List<Constraint> weekly = new ArrayList();
        Iterator var10 = constraintDAO.getWeeklyConstraints(dto.getEmployeeId()).iterator();

        while(var10.hasNext()) {
            ConstraintDTO c = (ConstraintDTO)var10.next();
            weekly.add(fromDTO(c));
        }

        e.setWeeklyConstraints(weekly);
        List<Constraint> morning = new ArrayList();
        Iterator var12 = constraintDAO.getMorningConstraints(dto.getEmployeeId()).iterator();

        while(var12.hasNext()) {
            ConstraintDTO c = (ConstraintDTO)var12.next();
            morning.add(fromDTO(c));
        }

        e.setMorningConstraints(morning);
        List<Constraint> evening = new ArrayList();
        Iterator var14 = constraintDAO.getEveningConstraints(dto.getEmployeeId()).iterator();

        while(var14.hasNext()) {
            ConstraintDTO c = (ConstraintDTO)var14.next();
            evening.add(fromDTO(c));
        }

        e.setEveningConstraints(evening);
        List<Constraint> locked = new ArrayList();
        Iterator var16 = constraintDAO.getLockedConstraints(dto.getEmployeeId()).iterator();

        while(var16.hasNext()) {
            ConstraintDTO c = (ConstraintDTO)var16.next();
            locked.add(fromDTO(c));
        }

        e.setLockedConstraints(locked);
        return e;
    }

    public static Role fromDTO(RoleDTO dto) {
        return new Role(dto.getRoleNumber(), dto.getDescription());
    }
    public static List<Employee> convertEmployeeDTOListToDomain(List<EmployeeDTO> employeeDTOs) {
        return (List)employeeDTOs.stream().map(DTOToDomainMapper::fromDTO).collect(Collectors.toList());
    }



    public static Constraint fromDTO(ConstraintDTO dto) {
        return new Constraint(dto.getExplanation(), WeekDay.valueOf(dto.getDay().toUpperCase()), ShiftType.valueOf(dto.getType().toUpperCase()));
    }

    public static Shift fromDTO(ShiftDTO dto) {
        Shift shift = new Shift(dto.getShiftID(), WeekDay.valueOf(dto.getDay().toUpperCase()), ShiftType.valueOf(dto.getType().toUpperCase()));

        // עדכון סטטוס
        shift.updateStatus(Status.valueOf(dto.getStatus()));

        // הגדרת מנהל משמרת אם יש
        if (!Status.valueOf(dto.getStatus()).equals(Status.Empty)) {
            Employee shiftManager = employeeRepository.getById(dto.getShiftManagerId());
            shift.setShiftManager(shiftManager);
        }


        // הוספת תפקידים דרושים
        for (RoleDTO r : dto.getNecessaryRoles()) {
            shift.addNecessaryRoles(fromDTO(r));
        }

        // הוספת עובדים ששובצו בפועל
        for (FilledRoleDTO filledRoleDTO : dto.getFilledRoles()) {
            Employee employee = employeeRepository.getById(filledRoleDTO.getEmployeeId());
            Role role = roleRepository.getRoleByNumber(filledRoleDTO.getRoleId());
            shift.addEmployee(employee, role);
        }

        return shift;
    }

    public static ShiftDTO toDTO(Shift shift) {
        // המרת עובדים ל־DTO
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        for (Employee e : shift.getEmployees()) {
            employeeDTOs.add(toDTO(e));
        }

        // המרת תפקידים דרושים ל־DTO
        List<RoleDTO> roleDTOs = new ArrayList<>();
        for (Role r : shift.getNecessaryRoles()) {
            roleDTOs.add(toDTO(r));
        }

        // המרת שיבוצים בפועל ל־DTO
        List<FilledRoleDTO> filledRoleDTOs = new ArrayList<>();
        for (FilledRole fr : shift.getFilledRoles()) {
            filledRoleDTOs.add(
                    new FilledRoleDTO(
                            shift.getShiftID(),
                            fr.getEmployee().getEmpId(),
                            fr.getRole().getRoleNumber()
                    )
            );
        }

        // יצירת האובייקט הסופי של DTO
        return new ShiftDTO(
                shift.getShiftID(),
                shift.getDay().name(),
                shift.getType().name(),
                shift.getStatus().name(),
                shift.getShiftManager() != null ? shift.getShiftManager().getEmpId() : -1L,
                employeeDTOs,
                roleDTOs,
                filledRoleDTOs
        );
    }

    public static EmployeeDTO toDTO(Employee e) {
        List<Integer> roleIds = new ArrayList();
        Iterator var2 = e.getRelevantRoles().iterator();

        while(var2.hasNext()) {
            Role role = (Role)var2.next();
            roleIds.add(role.getRoleNumber());
        }

        List<ConstraintDTO> weekly = new ArrayList();
        Iterator var9 = e.getWeeklyConstraints().iterator();

        while(var9.hasNext()) {
            Constraint c = (Constraint)var9.next();
            weekly.add(new ConstraintDTO(e.getEmpId(), c.getExplanation(), c.getDay().name(), c.getType().name()));
        }

        List<ConstraintDTO> morning = new ArrayList();
        Iterator var11 = e.getMorningConstraints().iterator();

        while(var11.hasNext()) {
            Constraint c = (Constraint)var11.next();
            morning.add(new ConstraintDTO(e.getEmpId(), c.getExplanation(), c.getDay().name(), c.getType().name()));
        }

        List<ConstraintDTO> evening = new ArrayList();
        Iterator var13 = e.getEveningConstraints().iterator();

        while(var13.hasNext()) {
            Constraint c = (Constraint)var13.next();
            evening.add(new ConstraintDTO(e.getEmpId(), c.getExplanation(), c.getDay().name(), c.getType().name()));
        }

        List<ConstraintDTO> locked = new ArrayList();
        Iterator var15 = e.getLockedConstraints().iterator();

        while(var15.hasNext()) {
            Constraint c = (Constraint)var15.next();
            locked.add(new ConstraintDTO(e.getEmpId(), c.getExplanation(), c.getDay().name(), c.getType().name()));
        }

        return new EmployeeDTO(e.getEmpId(), e.getEmpName(), e.getEmpPassword(), e.getEmpBankAccount(), e.getEmpSalary(), e.getEmpStartDate(), e.getMinDayShift(), e.getMinEveninigShift(), e.getSickDays(), e.getDaysOff(), roleIds, weekly, evening, locked, morning);
    }

    public static RoleDTO toDTO(Role r) {
        List<EmployeeDTO> relevantEmployees = new ArrayList();
        Iterator var2 = r.getRelevantEmployees().iterator();

        while(var2.hasNext()) {
            Employee e = (Employee)var2.next();
            relevantEmployees.add(toDTO(e));
        }

        return new RoleDTO(r.getRoleNumber(), r.getDescription(), relevantEmployees);
    }

    public static ConstraintDTO toDTO(Constraint c, long ID) {
        return new ConstraintDTO(ID, c.getExplanation(), c.getDay().name(), c.getType().name());
    }

    public static EmploymentContractDTO toDTO(EmploymentContract contract, Employee employee) {
        return new EmploymentContractDTO(contract.getMinDayShift(employee), contract.getMinEveninigShift(employee), contract.getSickDays(employee), contract.getDaysOff(employee), employee.getEmpId());
    }

    public static Branch fromDTO(BranchDTO dto) throws SQLException {
        Branch branch = new Branch(dto.getDistrict(), dto.getBranchID(), dto.getName(), dto.getCurrentWeekDTO());
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

    public static BranchDTO toDTO(Branch branch) throws SQLException {
        List<EmployeeDTO> employeeDTOs = new ArrayList();
        Iterator var2 = branch.getEmployeeRepo().getAll().iterator();

        while(var2.hasNext()) {
            Employee employee = (Employee)var2.next();
            employeeDTOs.add(toDTO(employee));
        }

        List<RoleDTO> roleDTOs = new ArrayList();
        Iterator var10 = branch.getRoleRepo().getAll().iterator();

        while(var10.hasNext()) {
            Role role = (Role)var10.next();
            roleDTOs.add(toDTO(role));
        }

        List<WeekDTO> weekDTOs = new ArrayList();
        Iterator var12 = branch.getWeekRepo().getAll().iterator();

        while(var12.hasNext()) {
            Week week = (Week)var12.next();
            List<ShiftDTO> shiftDTOs = new ArrayList();
            Iterator var7 = week.getShifts().iterator();

            while(var7.hasNext()) {
                Shift shift = (Shift)var7.next();
                shiftDTOs.add(toDTO(shift));
            }

            weekDTOs.add(new WeekDTO(week.getConstraintDeadline(), shiftDTOs));
        }

        return new BranchDTO(branch.getBranchID(), branch.getName(), branch.getDistrict(), employeeDTOs, roleDTOs, weekDTOs);
    }

    public static Week fromDTO(WeekDTO dto) {
        Week week = new Week();
        List<Shift> shifts = new LinkedList();
        Iterator var3 = dto.getShifts().iterator();

        while(var3.hasNext()) {
            ShiftDTO shiftDTO = (ShiftDTO)var3.next();
            Shift shift = fromDTO(shiftDTO);
            shifts.add(shift);
        }

        week.setShifts(shifts);
        return week;
    }

    public static UserDTO toDTO(User user) {
        if (user != null && user.getUser() != null && user.getLevel() != null) {
            long userId = user.getUser().getEmpId();
            String level = user.getLevel().name();
            return new UserDTO(userId, level);
        } else {
            throw new IllegalArgumentException("User or user details cannot be null");
        }
    }

    public static WeekDTO toDTO(Week newWeek) {
        List<ShiftDTO> shiftDTOs = new ArrayList();
        Iterator var2 = newWeek.getShifts().iterator();

        while(var2.hasNext()) {
            Shift shift = (Shift)var2.next();
            shiftDTOs.add(toDTO(shift));
        }

        return new WeekDTO(newWeek.getConstraintDeadline(), shiftDTOs);
    }
}
