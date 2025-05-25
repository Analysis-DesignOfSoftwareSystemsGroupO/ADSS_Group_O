package HR_Mudol.DAO;

import HR_Mudol.domain.Branch;
import java.sql.SQLException;
import java.util.List;

public interface IBranchRepositoryDAO {
    void add(Branch branch) throws SQLException;
    Branch get(int branchID);
    List<Branch> getAll();
    void remove(int branchID);
}
