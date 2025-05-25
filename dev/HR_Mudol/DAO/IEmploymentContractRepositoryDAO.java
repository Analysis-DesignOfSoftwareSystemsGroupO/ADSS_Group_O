package HR_Mudol.DAO;

import HR_Mudol.DTO.EmploymentContractDTO;

public interface IEmploymentContractRepositoryDAO {
    void insert(EmploymentContractDTO dto);
    void update(EmploymentContractDTO dto);
    void delete(int empId);
    EmploymentContractDTO findByEmpId(int empId);
}
