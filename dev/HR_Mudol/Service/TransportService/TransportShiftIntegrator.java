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
import java.util.Objects;

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
        RoleDTO driverDTO;
        List<ShiftDTO> shiftDTOs;
        List<TransportDTO> transports = transportController.getTransportNextWeek();

        for (TransportDTO transport : transports) {
            String licence = transportController.getLicenceRequiredByTransportID(transport.getId());

            WeekDay day = WeekDay.valueOf(transport.getDate().getDayOfWeek().name());
            ShiftType type = determineShiftType(transport.getDepartureTime());
            shiftDTOs = hrService.getNextWeekDTO().getShifts();

            //נעבור על המשמרות נבדוק איזו מתאימה

            for (ShiftDTO shiftDTO : shiftDTOs) {
                //&& hrService.getBranchOfShift(shiftDTO)=branchTransport
                if (shiftDTO.getDay().equals(day.name()) && shiftDTO.getType().equals(type.name())) {

                    //נהג ישבץ סניף המוצא
                    if (licence != null && !licence.trim().isEmpty() &&  Objects.equals(hrService.getBranchOfShift(shiftDTO), transport.getSiteName())) {//בדיקה למקרה שלא שובצה משאית להובלה + בדיקה שהסניף רלוונטי
                        licence="Driver "+licence+ ":" + transport.getId();
                        hrService.insertNewRole(licence);
                        driverDTO = getRoleByDescription(licence, theCaller);
                        hrService.addRoleToShiftIfNeeded(theCaller, shiftDTO, driverDTO, 1);
                    }

                    //מחסנאי ישבץ סניף היעד
                    List<ProductListDocumentDto> plds = transportController.getPLDbyTransportID(transport.getId());
                    for (ProductListDocumentDto pld : plds) {
                        ShiftType typeforWarehouseDTO = determineShiftType(pld.getApproximatedArrivalTime());
                        WeekDay dayforWarehouseDTO = WeekDay.valueOf(pld.getDate().getDayOfWeek().name());
                        if (Objects.equals(hrService.getBranchOfShift(shiftDTO), transport.getSiteName()) && shiftDTO.getDay().equals(dayforWarehouseDTO.name()) && shiftDTO.getType().equals(typeforWarehouseDTO.name())) {
                            ensureRolesWarehouseExist(theCaller);
                            RoleDTO warehouse = getRoleByDescription("Warehouse", theCaller);
                            hrService.addRoleToShiftIfNeeded(theCaller, shiftDTO, warehouse, 1);
                        }
                    }
                    System.out.println("🚚 Transport-based roles integrated into shifts and saved to DB.");
                }
            }

        }
    }

    private ShiftType determineShiftType(LocalTime time) {
        return time.isBefore(LocalTime.NOON) ? ShiftType.MORNING : ShiftType.EVENING;
    }

    private void ensureRolesExist(UserDTO caller,String str) throws SQLException {
        List<RoleDTO> roles = branch.getRoles();

        boolean hasDriver = roles.stream().anyMatch(r -> r.getDescription().equals(str));

        if (!hasDriver) {
           hrService.getRoleController().createRolebydescription(caller, str);
        }

        // Refresh role list after potential additions
        branch.setRoles(hrService.getRoleController().getAllRoles(caller).stream().map(DTOToDomainMapper::toDTO).toList());
    }

    private void ensureRolesWarehouseExist(UserDTO caller) throws SQLException {
        List<RoleDTO> roles = branch.getRoles();

        boolean hasWarehouse = roles.stream().anyMatch(r -> r.getDescription().toLowerCase().contains("Warehouse"));

        if (!hasWarehouse) {
            hrService.getRoleController().createRolebydescription(caller,"Warehouse" );
        }


        // Refresh role list after potential additions
        branch.setRoles(hrService.getRoleController().getAllRoles(caller).stream().map(DTOToDomainMapper::toDTO).toList());
    }






    private RoleDTO getRoleByDescription(String desc ,UserDTO caller) throws SQLException {
        for (Role role : hrService.getRoleController().getAllRoles(caller)) {
            if (role.getDescription().equals(desc)) {
                return DTOToDomainMapper.toDTO(role);
            }
        }
        return null;
    }
}
//