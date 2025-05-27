package HR_Mudol.DAO;

import HR_Mudol.DTO.*;
import HR_Mudol.domain.Objects.Branch;


import java.sql.SQLException;
import java.util.*;

public class BranchRepositoryDAOImpl implements IBranchRepositoryDAO {

    private final IBranchDAO branchDAO;
    private final Map<Integer, Branch> branches = new HashMap<>();

    public BranchRepositoryDAOImpl(IBranchDAO branchDAO) throws SQLException {
        this.branchDAO = branchDAO;

        // load existing branches
        for (BranchDTO dto : branchDAO.getAll()) {
            branches.put(dto.getBranchID(), BranchMapper.fromDTO(dto));
        }
    }

    @Override
    public void add(Branch branch) throws SQLException {
        branches.put(branch.getBranchID(), branch);
        branchDAO.insert(BranchMapper.toDTO(branch));
    }

    @Override
    public Branch get(int branchID) {
        return branches.get(branchID);
    }

    @Override
    public List<Branch> getAll() {
        return new ArrayList<>(branches.values());
    }

    @Override
    public void remove(int branchID) {
        branches.remove(branchID);
    }
}
