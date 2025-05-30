package HR_Mudol.DAO;

import HR_Mudol.DTO.*;
import java.sql.*;
import HR_Mudol.DataBase.*;

import java.util.ArrayList;
import java.util.List;

public class BranchDAOImpl implements IBranchDAO {

    private final Connection conn;
    private final IEmployeeDAO employeeDAO;
    private final IRoleDAO roleDAO;

    public BranchDAOImpl(IEmployeeDAO empDAO, IRoleDAO roleDAO) throws SQLException {
        this.conn = PostgresConnection.getConnection();;
        this.employeeDAO = empDAO;
        this.roleDAO = roleDAO;
    }

    @Override
    public void insert(BranchDTO branch) throws SQLException {
        String sql = "INSERT INTO branches (branchID) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, branch.getBranchID());
            stmt.executeUpdate();
        }

        for (EmployeeDTO emp : branch.getEmployees()) {
            employeeDAO.insert(emp);
        }
        for (RoleDTO role : branch.getRoles()) {
            roleDAO.insert(role);
        }

    }

    @Override
    public BranchDTO get(int branchID) throws SQLException {
        String sql = "SELECT * FROM branches WHERE branchID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, branchID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");

                List<EmployeeDTO> employees = employeeDAO.getAllByBranch(branchID);
                List<RoleDTO> roles = roleDAO.getAllByBranch(branchID);
                List<WeekDTO> weeks = List.of(); // עדיין ריק כי אין טבלה לזה

                return new BranchDTO(branchID, name, employees, roles, weeks);
            } else {
                return null; // או לזרוק שגיאה
            }
        }
    }



    @Override
    public void delete(int branchID) throws SQLException {
        String sql = "DELETE FROM branches WHERE branchID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, branchID);
            stmt.executeUpdate();
        }
    }
    @Override
    public List<BranchDTO> getAll() throws SQLException {
        String sql = "SELECT * FROM branches";
        List<BranchDTO> branches = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("branchID");
                String name = rs.getString("name");

                List<EmployeeDTO> employees = employeeDAO.getAllByBranch(id);
                List<RoleDTO> roles = roleDAO.getAllByBranch(id);

                branches.add(new BranchDTO(id, name, employees, roles, List.of()));
            }
        }
        return branches;
    }


}
