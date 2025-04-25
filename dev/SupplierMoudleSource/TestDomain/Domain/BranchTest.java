package Domain;

import SupplierMoudleSource.Domain.Branch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BranchTest {
    private Branch branch;

    @BeforeEach
    void setUp() {
         branch = new Branch("1", "Lehavim", "Hagefen 12");
    }

    @Test
    void getBranchID() {
        assertEquals(branch.getBranchID(), "1");
    }

    @Test
    void getBranchAddress() {
        assertEquals(branch.getBranchAddress(), "Hagefen 12");
    }

    @Test
    void getBranchCity() {
        assertEquals(branch.getBranchCity(), "Lehavim");
    }
}