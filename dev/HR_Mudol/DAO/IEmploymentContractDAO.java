package HR_Mudol.DAO;

import HR_Mudol.DTO.EmploymentContractDTO;

public interface IEmploymentContractDAO {
    void insert(EmploymentContractDTO dto);
    void update(EmploymentContractDTO dto);
    void delete(int empId);
    EmploymentContractDTO findByEmpId(int empId);
}
