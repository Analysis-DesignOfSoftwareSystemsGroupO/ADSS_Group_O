package HR_Mudol.Service.TransportService;

import TransportModule.transport_module.ITransportController;
import HR_Mudol.DTO.WeekDTO;
import HR_Mudol.domain.Controllers.DTOToDomainMapper;
import HR_Mudol.domain.Objects.*;
import HR_Mudol.domain.ShiftType;
import HR_Mudol.domain.WeekDay;
import HR_Mudol.domain.repository.RoleRepository;
import TransportModule.DTO.*;


import java.time.LocalTime;
import java.util.List;

public class TransportShiftIntegrator implements ITransportShiftIntegrator {
    private final Branch branch;
    private final ITransportController transportController;

    public TransportShiftIntegrator(Branch branch, ITransportController transportController) {
        this.branch = branch;
        this.transportController = transportController;
    }

    public void integrateTransportShifts() throws Exception {
        List<TransportDTO> transports = transportController.getTransportNextWeek();
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

        for (TransportDTO t : transports) {
            WeekDay day = WeekDay.valueOf(t.getDate().getDayOfWeek().name());
            ShiftType type = determineShiftType(t.getDepartureTime());
            Shift shift = week.getShifts().stream()
                    .filter(s -> s.getDay() == day && s.getType() == type)
                    .findFirst()
                    .orElse(null);

            if (shift == null) {
                System.out.printf("⚠ No shift found for %s %s%n", day, type);
                continue;
            }

            // מקור ההובלה - נדרש נהג
            if (t.getSiteName().equalsIgnoreCase(branch.getName())) {
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

            // יעד ההובלה - נדרש מחסנאי
            List<ProductListDocumentDto> plds = transportController.getPLDbyTransportID(String.valueOf(t.getId()));
            for (ProductListDocumentDto pld : plds) {
                if (pld.getSiteDes().equalsIgnoreCase(branch.getName())) {
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
