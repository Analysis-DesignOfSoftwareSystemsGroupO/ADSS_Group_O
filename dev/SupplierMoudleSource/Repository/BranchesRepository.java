package SupplierMoudleSource.Repository;

import DTO.BranchDTO;
import SupplierMoudleSource.DAO.BranchDAO;
import SupplierMoudleSource.Domain.Branch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BranchesRepository {
    private Map<String, Branch> branches;
    private BranchDAO branchDAO;

    //singleton database
    private static BranchesRepository branchesDataBase = null;
    public static BranchesRepository getInstance() {
        if (branchesDataBase == null) {
            branchesDataBase = new BranchesRepository();
        }
        return branchesDataBase;
    }
    private BranchesRepository(){
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

    public BranchDTO getBranch(String branchID) throws Exception {
        if (branches.containsKey(branchID)) {
            return branches.get(branchID).getBranchDTO();
        }
        try {
            branchDAO.getBranch(branchID);
        }catch (Exception e){
            throw new Exception("Branch does not exist");
        }
        return null;
    }
}