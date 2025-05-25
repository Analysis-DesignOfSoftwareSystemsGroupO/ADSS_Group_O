package HR_Mudol.DAO;

import HR_Mudol.DAO.IShiftDAO;
import HR_Mudol.DTO.ShiftDTO;

import java.sql.SQLException;
import java.util.List;

public class ShiftRepositoryDAOImpl implements IShiftRepositoryDAO {

    private final IShiftDAO dao;
    private int currentMaxId;

    public ShiftRepositoryDAOImpl(IShiftDAO dao) throws SQLException {
        this.dao = dao;
        this.currentMaxId = dao.getAll().stream()
                .mapToInt(ShiftDTO::getShiftID)
                .max()
                .orElse(0);
    }

    @Override
    public void add(ShiftDTO shift) throws SQLException {
        ShiftDTO withID = new ShiftDTO(
                ++currentMaxId,
                shift.getDay(),
                shift.getType(),
                shift.getStatus(),
                shift.getShiftManagerId()
        );
        dao.insert(withID);
    }

    @Override
    public void update(ShiftDTO shift) throws SQLException {
        dao.update(shift);
    }

    @Override
    public void delete(int shiftID) throws SQLException {
        dao.delete(shiftID);
    }

    @Override
    public ShiftDTO get(int shiftID) throws SQLException {
        return dao.get(shiftID);
    }

    @Override
    public List<ShiftDTO> getAll() throws SQLException {
        return dao.getAll();
    }
}
