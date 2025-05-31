package HR_Mudol.domain.repository;

import HR_Mudol.DAO.IBranchDAO;
import HR_Mudol.DTO.BranchDTO;
import HR_Mudol.domain.Controllers.DTOToDomainMapper;
import HR_Mudol.domain.Objects.Branch;

import java.sql.SQLException;
import java.util.*;

public class BranchRepository {

    private final IBranchDAO dao;
    private static final Map<Integer, Branch> branchCache = new HashMap<>();

    public BranchRepository(IBranchDAO dao) throws SQLException {
        this.dao = dao;
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

    public Collection<Branch> getAllBranches() {
        return branchCache.values();
    }
}
