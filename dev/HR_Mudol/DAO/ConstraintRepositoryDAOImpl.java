package HR_Mudol.DAO;

import HR_Mudol.DAO.IConstraintDAO;
import HR_Mudol.DTO.ConstraintDTO;

import java.sql.SQLException;
import java.util.List;

public class ConstraintRepositoryDAOImpl implements IConstraintRepositoryDAO {

    private final IConstraintDAO dao;

    public ConstraintRepositoryDAOImpl(IConstraintDAO dao) {
        this.dao = dao;
    }

    @Override
    public void add(ConstraintDTO constraint) throws SQLException {
        dao.insert(constraint);
    }

    @Override
    public void delete(int empID, String day, String type) throws SQLException {
        dao.delete(empID, day, type);
    }

    @Override
    public List<ConstraintDTO> getByEmployee(int empID) throws SQLException {
        return dao.getByEmployee(empID);
    }

    @Override
    public List<ConstraintDTO> getAll() throws SQLException {
        return dao.getAll();
    }
}
