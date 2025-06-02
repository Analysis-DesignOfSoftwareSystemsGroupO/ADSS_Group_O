package HR_Mudol.domain.repository;

import HR_Mudol.DAO.BranchDAOImpl;
import HR_Mudol.DAO.IBranchDAO;
import HR_Mudol.DTO.BranchDTO;
import HR_Mudol.domain.Controllers.DTOToDomainMapper;
import HR_Mudol.domain.Objects.Branch;

import java.sql.SQLException;
import java.util.*;

public class BranchRepository {

    private final IBranchDAO dao;
    private static final Map<Integer, Branch> branchCache = new HashMap<>();

    public BranchRepository() throws SQLException {
        this.dao = new BranchDAOImpl();
        loadBranches(); // טוען פעם אחת את כל הסניפים מה-DB
    }

    private void loadBranches() throws SQLException {
        List<BranchDTO> branchDTOs = dao.getAll();
        for (BranchDTO dto : branchDTOs) {
            Branch branch = DTOToDomainMapper.fromDTO(dto);
            branchCache.put(dto.getBranchID(), branch);
        }
    }

    public Branch getBranchById(int branchId) {
        return branchCache.get(branchId);
    }

    public List<BranchDTO> getAllBranches() throws SQLException {
        List<BranchDTO> result = new ArrayList<>();
        for (Branch branch : branchCache.values()) {
            result.add(DTOToDomainMapper.toDTO(branch));
        }
        return result;
    }

    public void add(BranchDTO dto) throws SQLException {
        dao.insert(dto); // הוספה למסד הנתונים
        Branch branch = DTOToDomainMapper.fromDTO(dto);
        branchCache.put(dto.getBranchID(), branch); // הוספה לזיכרון
    }

}
