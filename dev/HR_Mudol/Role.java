package HR_Mudol;
import java.util.LinkedList;
import java.util.List;
public class Role {
    private int roleNumber;
    private String description;
    private List<Object> relevantEmployees;

    public Role(int roleNumber, String description) {
        this.roleNumber = roleNumber;
        this.description = description;
        this.relevantEmployees = new LinkedList<>();
    }
}
