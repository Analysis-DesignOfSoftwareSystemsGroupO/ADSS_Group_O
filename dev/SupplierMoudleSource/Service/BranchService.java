package SupplierMoudleSource.Service;

import SupplierMoudleSource.DataBase.BranchesDataBase;
import SupplierMoudleSource.Domain.Branch;

import java.util.ArrayList;

public class BranchService {
    private BranchesDataBase branchesDataBase = BranchesDataBase.getInstance();
    //method that checks if a branch id is valid
    public boolean isValidBranch(String branchId) {
        return branchesDataBase.existsBranch(branchId);
    }
    //method that prints all branches
    public void printAllBranchIds(){
        ArrayList<Branch> branches = branchesDataBase.getCopyBranches();
        for (Branch branch : branches){
            System.out.println(branch.toString());
        }
    }
}
