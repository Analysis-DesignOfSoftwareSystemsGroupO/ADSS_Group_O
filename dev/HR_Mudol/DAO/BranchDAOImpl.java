package HR_Mudol.DAO;

import HR_Mudol.DTO.*;
import java.sql.*;
import HR_Mudol.DataBase.*;
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
        List<EmployeeDTO> employees = employeeDAO.getAll(); // or by branchID if applicable
        List<RoleDTO> roles = roleDAO.getAll();

        // WeekDTOs are not stored in DB, so we'll return an empty list or null
        List<WeekDTO> weeks = List.of(); // empty

        return new BranchDTO(branchID, employees, roles, weeks);
    }

    @Override
    public List<BranchDTO> getAll() {
        throw new UnsupportedOperationException("Multiple branches not supported yet.");
    }

    @Override
    public void delete(int branchID) throws SQLException {
        String sql = "DELETE FROM branches WHERE branchID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, branchID);
            stmt.executeUpdate();
        }
    }
}
