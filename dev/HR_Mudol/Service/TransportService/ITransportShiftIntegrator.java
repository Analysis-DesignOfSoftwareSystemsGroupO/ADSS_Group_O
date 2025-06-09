package HR_Mudol.Service.TransportService;


import HR_Mudol.DTO.BranchDTO;
import HR_Mudol.DTO.UserDTO;
import HR_Mudol.Service.ManagerService.HRService;
import TransportModule.transport_module.ITransportController;

public interface ITransportShiftIntegrator {
    void integrateTransportShifts(UserDTO callerDTO) throws Exception;
}
