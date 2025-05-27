package HR_Mudol.DTO;

import HR_Mudol.domain.Objects.EmploymentContract;
import HR_Mudol.domain.Objects.Employee;
import HR_Mudol.domain.Objects.User;
import HR_Mudol.domain.repository.EmployeeRepository;

public class EmploymentContractMapper {

    // Creates a DTO from domain model
    public static EmploymentContractDTO toDTO(EmploymentContract contract, User caller) {
        return new EmploymentContractDTO(
                contract.getMinDayShift(caller, contract.getOwner()),
                contract.getMinEveninigShift(caller, contract.getOwner()),
                contract.getSickDays(caller, contract.getOwner()),
                contract.getDaysOff(caller, contract.getOwner()),
                contract.getOwner().getEmpId()
        );
    }

    // Reconstructs the domain object from DTO
    public static EmploymentContract fromDTO(EmploymentContractDTO dto, EmployeeRepository employeeRepo) {
        Employee employee = employeeRepo.getById(dto.getOwnerId());
        return new EmploymentContract(
                dto.getMinDayShift(),
                dto.getMinEveningShift(),
                dto.getSickDays(),
                dto.getDaysOff(),
                employee
        );
    }
}
