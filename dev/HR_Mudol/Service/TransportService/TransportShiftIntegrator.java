package HR_Mudol.Service.TransportService;

import HR_Mudol.DTO.PLDDTO;
import HR_Mudol.DTO.TransportReqDTO;
import HR_Mudol.DTO.WeekDTO;
import HR_Mudol.domain.Controllers.DTOToDomainMapper;
import HR_Mudol.domain.Objects.*;
import HR_Mudol.domain.ShiftType;
import HR_Mudol.domain.WeekDay;
import HR_Mudol.domain.repository.*;

import java.time.LocalTime;
import java.util.List;

public class TransportShiftIntegrator implements ITransportShiftIntegrator {
    private final Branch branch;
    private final ITransportController transportController;

    public TransportShiftIntegrator(Branch branch, ITransportController transportController) {
        this.branch = branch;
        this.transportController = transportController;
    }

    public void integrateTransportShifts() {
        List<TransportReqDTO> transports = transportController.getTransportNextWeek();
        WeekDTO weekDTO = branch.getWeekRepo().getCurrentWeekDTO();
        Week week = DTOToDomainMapper.fromDTO(weekDTO);

        RoleRepository roleRepo = branch.getRoleRepo();

        Role driverRole = roleRepo.getAll().stream()
                .filter(r -> r.getDescription().equalsIgnoreCase("Driver"))
                .findFirst()
                .orElseGet(() -> {
                    Role newRole = new Role("Driver");
                    try {
                        roleRepo.add(newRole);
                        System.out.println("✅ Role 'Driver' created.");
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to create Driver role", e);
                    }
                    return newRole;
                });

        Role warehouseRole = roleRepo.getAll().stream()
                .filter(r -> r.getDescription().equalsIgnoreCase("Warehouse"))
                .findFirst()
                .orElseGet(() -> {
                    Role newRole = new Role("Warehouse");
                    try {
                        roleRepo.add(newRole);
                        System.out.println("✅ Role 'Warehouse' created.");
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to create Warehouse role", e);
                    }
                    return newRole;
                });

        for (TransportReqDTO t : transports) {
            WeekDay day = WeekDay.valueOf(t.getDate().getDayOfWeek().name());
            ShiftType type = determineShiftType(t.getTime());

            Shift shift = week.getShifts().stream()
                    .filter(s -> s.getDay() == day && s.getType() == type)
                    .findFirst()
                    .orElse(null);

            if (shift == null) {
                System.out.printf("⚠ No shift found for %s %s%n", day, type);
                continue;
            }

            if (t.getSource().equalsIgnoreCase(branch.getName())) {
                if (!shift.getNecessaryRoles().contains(driverRole)) {
                    shift.addNecessaryRoles(driverRole);
                    branch.getWeekRepo().addOrUpdateRequiredRole(
                            branch.getBranchID(),
                            shift.getShiftID(),
                            driverRole.getRoleNumber(),
                            1
                    );
                }
            }

            List<PLDDTO> plds = transportController.getPLDbyTransportID(t.getId());
            for (PLDDTO pld : plds) {
                if (pld.getDestination().equalsIgnoreCase(branch.getName())) {
                    if (!shift.getNecessaryRoles().contains(warehouseRole)) {
                        shift.addNecessaryRoles(warehouseRole);
                        branch.getWeekRepo().addOrUpdateRequiredRole(
                                branch.getBranchID(),
                                shift.getShiftID(),
                                warehouseRole.getRoleNumber(),
                                1
                        );
                    }
                }
            }
        }

        System.out.println("🚚 Transport-based roles integrated into shifts and saved to DB.");
    }


    private ShiftType determineShiftType(LocalTime time) {
        return time.isBefore(LocalTime.NOON) ? ShiftType.MORNING : ShiftType.EVENING;
    }
}
