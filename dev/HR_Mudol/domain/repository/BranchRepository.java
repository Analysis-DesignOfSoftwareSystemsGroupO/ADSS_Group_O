package HR_Mudol.domain.repository;

import HR_Mudol.DAO.IBranchDAO;
import HR_Mudol.DTO.BranchDTO;

import java.sql.SQLException;
import java.util.List;

public class BranchRepository {

    private final IBranchDAO dao;

    public BranchRepository(IBranchDAO dao) {
        this.dao = dao;
    }

    public void add(BranchDTO dto) throws SQLException {
        dao.insert(dto);
    }

    public boolean exists(int id) throws SQLException {
        return dao.get(id) != null;
    }

    public BranchDTO getById(int id) throws SQLException {
        return dao.get(id);
    }

    public List<BranchDTO> getAll() throws SQLException {
        return dao.getAll();
    }

    public void delete(int id) throws SQLException {
        dao.delete(id);
    }
}
