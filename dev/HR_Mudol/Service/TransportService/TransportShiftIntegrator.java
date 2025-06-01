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

        Role driverRole = branch.getRoleRepo().getAll().stream()
                .filter(r -> r.getDescription().equalsIgnoreCase("Driver"))
                .findFirst().orElse(null);

        Role warehouseRole = branch.getRoleRepo().getAll().stream()
                .filter(r -> r.getDescription().equalsIgnoreCase("Warehouse"))
                .findFirst().orElse(null);

        if (driverRole == null || warehouseRole == null) {
            System.out.println("❗ One or more required roles (Driver/Warehouse) not found in branch.");
            return;
        }

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

            // מקור ההובלה - נדרש נהג
            if (t.getSource().equalsIgnoreCase(branch.getName())) {
                if (!shift.getNecessaryRoles().contains(driverRole)) {
                    shift.addNecessaryRoles(driverRole);
                }
            }

            // יעד ההובלה - נדרש מחסנאי
            List<PLDDTO> plds = transportController.getPLDbyTransportID(t.getId());
            for (PLDDTO pld : plds) {
                if (pld.getDestination().equalsIgnoreCase(branch.getName())) {
                    if (!shift.getNecessaryRoles().contains(warehouseRole)) {
                        shift.addNecessaryRoles(warehouseRole);
                    }
                }
            }
        }

        System.out.println("🚚 Transport-based roles integrated into shifts successfully.");
    }



    private ShiftType determineShiftType(LocalTime time) {
        return time.isBefore(LocalTime.NOON) ? ShiftType.MORNING : ShiftType.EVENING;
    }
}
