package HR_Mudol.DAO;

import HR_Mudol.DTO.FilledRoleDTO;
import java.util.List;

public interface IFilledRoleDAO {
    void insert(FilledRoleDTO dto);
    void delete(int shiftId, int empId, int roleId);
    List<FilledRoleDTO> findByShift(int shiftId);
    List<FilledRoleDTO> findByEmployee(int empId);
    List<FilledRoleDTO> findAll();
    List<FilledRoleDTO> getEmployees(int shiftId);

}
