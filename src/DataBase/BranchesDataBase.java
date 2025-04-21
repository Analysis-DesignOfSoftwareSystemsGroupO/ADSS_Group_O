package DataBase;

import Domain.Branch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BranchesDataBase {
    private Map<String, Branch> branches;

    //singleton database
    private static BranchesDataBase branchesDataBase = null;
    public static BranchesDataBase getInstance() {
        if (branchesDataBase == null) {
            branchesDataBase = new BranchesDataBase();
        }
        return branchesDataBase;
    }
    private BranchesDataBase(){
        branches = new HashMap<>();
    }


    public void addBranch(Branch branch) {
        branches.put(branch.getBranchID(), branch);
    }

    public boolean existsBranch(String branchID) {
        return branches.containsKey(branchID);
    }

    //returns a copy of all the existing branches
    public ArrayList<Branch> getCopyBranches(){
        return new ArrayList<>(branches.values());
    }

    public Branch getBranch(String branchID) {
        return branches.get(branchID);
    }
}
