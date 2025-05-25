package HR_Mudol.DAO;

import HR_Mudol.DTO.BranchDTO;
import java.sql.SQLException;
import java.util.List;

public interface IBranchDAO {
    void insert(BranchDTO branch) throws SQLException;
    BranchDTO get(int branchID) throws SQLException;
    List<BranchDTO> getAll() throws SQLException;
    void delete(int branchID) throws SQLException;
}
