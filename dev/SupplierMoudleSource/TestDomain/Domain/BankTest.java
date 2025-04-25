package Domain;

import SupplierMoudleSource.Domain.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {
    private Bank bank;
    @BeforeEach
    void setUp() {
        bank = new Bank("55", "215", "10", "211587951");
    }

    @Test
    void getOwnerID() {
        assertEquals(bank.getOwnerID(), "211587951");
    }
}