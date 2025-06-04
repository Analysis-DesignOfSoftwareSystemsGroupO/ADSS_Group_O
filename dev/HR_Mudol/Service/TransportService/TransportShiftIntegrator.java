package HR_Mudol.Service.TransportService;

import HR_Mudol.DTO.*;
import HR_Mudol.Service.ManagerService.HRService;
import TransportModule.DTO.ProductListDocumentDto;
import TransportModule.DTO.TransportDTO;
import TransportModule.transport_module.ITransportController;
import HR_Mudol.domain.Controllers.DTOToDomainMapper;
import HR_Mudol.domain.ShiftType;
import HR_Mudol.domain.WeekDay;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

public class TransportShiftIntegrator implements ITransportShiftIntegrator {
    private final BranchDTO branch;
    private final ITransportController transportController;
    private final HRService hrService;

    public TransportShiftIntegrator(BranchDTO branch, ITransportController transportController, HRService hrService) {
        this.branch = branch;
        this.transportController = transportController;
        this.hrService = hrService;
    }

    public void integrateTransportShifts(UserDTO theCaller) throws Exception {
        WeekDTO weekDTO = branch.getCurrentWeekDTO();
        if (weekDTO == null) {
            System.out.println("⚠ No current week found in branch.");
            return;
        }

        // Ensure required roles exist
        ensureRolesExist(theCaller);
        RoleDTO driverDTO;
        RoleDTO warehouseDTO = getRoleByDescription("Warehouse");


        List<TransportDTO> transports = transportController.getTransportNextWeek();

        for (TransportDTO transport : transports) {
            int weight = transport.getMaxWeight();
            String licence;
            if(weight<=10000)
                licence = "Driver-A";
            else if(weight<=20000)
                licence = "Driver-B";
            else
                licence = "Driver-C";

            driverDTO = getRoleByDescription(licence);
            driverDTO.setDescription(licence+":"+transport.getId());

            WeekDay day = WeekDay.valueOf(transport.getDate().getDayOfWeek().name());
            ShiftType type = determineShiftType(transport.getDepartureTime());

            ShiftDTO shiftDTO = weekDTO.getShifts().stream().filter(s -> s.getDay().equals(day) && s.getType().equals(type)).findFirst().orElse(null);
            // todo - if shiftDTO == null ->  create one and add it to weekly repository
            if (shiftDTO == null) {
                System.out.printf("⚠ No shift found for %s %s%n", day, type);
                continue;
            }

            // Origin branch: needs Driver
// Add 1 Driver
            hrService.addRoleToShiftIfNeeded(theCaller, shiftDTO, driverDTO, 1);


            // Destination branch: needs Warehouse
            List<ProductListDocumentDto> plds = transportController.getPLDbyTransportID(String.valueOf(transport.getId()));
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

    private void ensureRolesExist(UserDTO caller) throws SQLException {
        List<RoleDTO> roles = branch.getRoles();

        boolean hasDriver = roles.stream().anyMatch(r -> r.getDescription().equalsIgnoreCase("Driver"));
        boolean hasWarehouse = roles.stream().anyMatch(r -> r.getDescription().equalsIgnoreCase("Warehouse"));

        if (!hasDriver) {
            hrService.getRoleController().createRolebydescription(caller, "Driver");
            System.out.println("✅ Role 'Driver' created.");
        }
        if (!hasWarehouse) {
            hrService.getRoleController().createRolebydescription(caller, "Warehouse");
            System.out.println("✅ Role 'Warehouse' created.");
        }

        // Refresh role list after potential additions
        branch.setRoles(hrService.getRoleController().getAllRoles(caller).stream()
                .map(DTOToDomainMapper::toDTO)
                .toList());
    }

    private RoleDTO getRoleByDescription(String desc) {
        // todo - change the function because it can send Driver-A -> need to be Driver
        return branch.getRoles().stream()
                .filter(r -> r.getDescription().equalsIgnoreCase(desc))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("❌ Role not found: " + desc));
    }
}
