package HR_Mudol.Service.TransportService;

import HR_Mudol.DTO.*;
import HR_Mudol.Service.ManagerService.HRService;
import TransportModule.DTO.ProductListDocumentDto;
import TransportModule.DTO.TransportDTO;
import TransportModule.transport_module.ITransportController;
import HR_Mudol.domain.Controllers.DTOToDomainMapper;
import HR_Mudol.domain.Objects.Week;
import HR_Mudol.domain.ShiftType;
import HR_Mudol.domain.WeekDay;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

public class TransportShiftIntegrator implements ITransportShiftIntegrator {
    private final BranchDTO branch;
    private final ITransportController transportController;
    private final HRService hrService;

    public TransportShiftIntegrator(BranchDTO branch, ITransportController transportController, HRService hrService) throws SQLException {
        this.branch = branch;
        this.transportController = transportController;
        this.hrService = hrService;
    }

    public void integrateTransportShifts(UserDTO theCaller) throws Exception {
        List<TransportDTO> transports = transportController.getTransportNextWeek();
        WeekDTO weekDTO = branch.getCurrentWeekDTO();
        if (weekDTO == null) {
            System.out.println("⚠ No current week found in branch.");
            return;
        }
        Week week = DTOToDomainMapper.fromDTO(weekDTO);

        List<RoleDTO> roleDTOList = branch.getRoles();

        // יצירה או שליפה של התפקיד "Driver"
        RoleDTO driverDTO = roleDTOList.stream()
                .filter(r -> r.getDescription().equalsIgnoreCase("Driver"))
                .findFirst()
                .orElseGet(() -> {
                    try {
                        hrService.getRoleController().createRolebydescription(theCaller, "Driver");
                        System.out.println("✅ Role 'Driver' created.");
                    } catch (Exception e) {
                        throw new RuntimeException("❌ Failed to create Driver role", e);
                    }
                    return null; // נחפש אותו שוב למטה אם צריך
                });

        // יצירה או שליפה של התפקיד "Warehouse"
        RoleDTO warehouseDTO = roleDTOList.stream()
                .filter(r -> r.getDescription().equalsIgnoreCase("Warehouse"))
                .findFirst()
                .orElseGet(() -> {
                    try {
                        hrService.getRoleController().createRolebydescription(theCaller, "Warehouse");
                        System.out.println("✅ Role 'Warehouse' created.");
                    } catch (Exception e) {
                        throw new RuntimeException("❌ Failed to create Warehouse role", e);
                    }
                    return null; // נחפש אותו שוב למטה אם צריך
                });

        // עדכון הרשימה מחדש במקרה ונוצר תפקיד חדש
        roleDTOList = branch.getRoles();
        driverDTO = roleDTOList.stream()
                .filter(r -> r.getDescription().equalsIgnoreCase("Driver"))
                .findFirst().orElseThrow(() -> new IllegalStateException("Driver role not found after creation"));

        warehouseDTO = roleDTOList.stream()
                .filter(r -> r.getDescription().equalsIgnoreCase("Warehouse"))
                .findFirst().orElseThrow(() -> new IllegalStateException("Warehouse role not found after creation"));

        for (TransportDTO t : transports) {
            WeekDay day = WeekDay.valueOf(t.getDate().getDayOfWeek().name());
            ShiftType type = determineShiftType(t.getDepartureTime());
            ShiftDTO shiftDTO = weekDTO.getShifts().stream()
                    .filter(s -> s.getDay().equals(day) && s.getType().equals(type))
                    .findFirst()
                    .orElse(null);

            if (shiftDTO == null) {
                System.out.printf("⚠ No shift found for %s %s%n", day, type);
                continue;
            }

            // אם האתר שלנו הוא המקור - דרוש נהג
            if (t.getSiteName().equalsIgnoreCase(branch.getName())) {
                hrService.addRoleToShiftIfNeeded(theCaller, shiftDTO, driverDTO, 1);
            }

            // אם האתר שלנו הוא היעד - דרוש מחסנאי
            List<ProductListDocumentDto> plds = transportController.getPLDbyTransportID(String.valueOf(t.getId()));
            for (ProductListDocumentDto pld : plds) {
                if (pld.getSiteDes().equalsIgnoreCase(branch.getName())) {
                    hrService.addRoleToShiftIfNeeded(theCaller, shiftDTO, warehouseDTO, 1);
                }
            }
        }

        System.out.println("🚚 Transport-based roles integrated into shifts and saved to DB.");
    }

    private ShiftType determineShiftType(LocalTime time) {
        return time.isBefore(LocalTime.NOON) ? ShiftType.MORNING : ShiftType.EVENING;
    }
}
