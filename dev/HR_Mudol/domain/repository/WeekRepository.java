package HR_Mudol.domain.repository;

import HR_Mudol.domain.Week;

import java.util.LinkedList;
import java.util.List;

public class WeekRepository {
    private final List<Week> weeks = new LinkedList<>();

    public void add(Week week) {
        weeks.add(week);
    }

    public List<Week> getAll() {
        return new LinkedList<>(weeks);
    }

    public void clear() {
        weeks.clear();
    }
}
