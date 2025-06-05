package HR_Mudol.Service.TransportService;

import HR_Mudol.DTO.*;
import HR_Mudol.Service.ManagerService.HRService;
import HR_Mudol.domain.Objects.Role;
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
    ///todo  change to branchrepository from branch
    private final BranchDTO branch;
    private final ITransportController transportController;
    private final HRService hrService;

    public TransportShiftIntegrator(BranchDTO branch, ITransportController transportController, HRService hrService) {
        this.branch = branch;
        this.transportController = transportController;
        this.hrService = hrService;
    }

    public void integrateTransportShifts(UserDTO theCaller) throws Exception {
        ///todo check if  branch.getCurrentWeekDTO() is exist if not create one
        WeekDTO weekDTO = branch.getCurrentWeekDTO();///todo chnage to next week
        if (weekDTO == null) {
            System.out.println("⚠ No current week found in branch.");
            return;
        }

        // Ensure required roles exist
        ensureRolesExist(theCaller);
        RoleDTO driverDTO;
        RoleDTO warehouseDTO = getRoleByDescription("Warehouse",theCaller);


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

            driverDTO = getRoleByDescription(licence,theCaller);
            driverDTO.setDescription(licence+":"+transport.getId());

            WeekDay day = WeekDay.valueOf(transport.getDate().getDayOfWeek().name());
            ShiftType type = determineShiftType(transport.getDepartureTime());
            List<ShiftDTO> shiftDTOs = branch.getCurrentWeekDTO().getShifts();
            for (ShiftDTO dto : shiftDTOs) {
                if (dto.getDay().equals(day.name()) && dto.getType().equals(type.name())) {
                    hrService.addRoleToShiftIfNeeded(theCaller, dto, driverDTO, 1);


                    ///todo לכל סניף להוסיף מחסנאי בזמן ההגעה של ההובלה
                    // Destination branch: needs Warehouse
                    List<ProductListDocumentDto> plds = transportController.getPLDbyTransportID(String.valueOf(transport.getId()));
                    for (ProductListDocumentDto pld : plds) {

                        if (pld.getSiteDes().equalsIgnoreCase(branch.getName())) {
                            hrService.addRoleToShiftIfNeeded(theCaller, dto, warehouseDTO, 1);
                        }
                    }



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
            hrService.getRoleController().createRolebydescription(caller, "Driver-A");
            hrService.getRoleController().createRolebydescription(caller, "Driver-B");
            hrService.getRoleController().createRolebydescription(caller, "Driver-C");
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

    private RoleDTO getRoleByDescription(String desc ,UserDTO caller) throws SQLException {
        // todo - change the function because it can send Driver-A -> need to be Driver
        for (Role role : hrService.getRoleController().getAllRoles(caller)) {
            if (role.getDescription().toLowerCase().contains("driver")) {
                return DTOToDomainMapper.toDTO(role);
            }
        }
        return null;
    }
}
//