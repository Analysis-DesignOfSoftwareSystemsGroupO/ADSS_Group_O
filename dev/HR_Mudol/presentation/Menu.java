package HR_Mudol.presentation;
import HR_Mudol.domain.AbstractEmployee;
import HR_Mudol.domain.Branch;
import HR_Mudol.domain.User;

public interface Menu {

    public abstract boolean start(User caller, AbstractEmployee self, Branch curBranch);
}
