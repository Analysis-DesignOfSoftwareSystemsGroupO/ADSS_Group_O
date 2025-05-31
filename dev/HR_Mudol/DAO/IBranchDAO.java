package HR_Mudol.DAO;

import HR_Mudol.DTO.BranchDTO;
import java.sql.SQLException;
import java.util.List;

public interface IBranchDAO {


    List<BranchDTO> getAll();

}
