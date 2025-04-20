package DataBase;

import Domain.Branch;
import Domain.Supplier;

import java.util.Map;

public class BranchesDataBase {
    public Map<String, Branch> branches;

    public void addBranch(Branch branch) {
        branches.put(branch.getBranchID(), branch);
    }

    public Branch getBranch(String branchID) {
        return branches.get(branchID);
    }
}
