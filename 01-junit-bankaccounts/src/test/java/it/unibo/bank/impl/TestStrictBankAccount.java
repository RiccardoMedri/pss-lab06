package it.unibo.bank.impl;

import it.unibo.bank.api.AccountHolder;
import it.unibo.bank.api.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test class for the {@link StrictBankAccount} class.
 */
class TestStrictBankAccount {

    private static final double INITIAL_BALANCE = 0.0;
    private static final double DEPOSIT_AMOUNT = 100.0;
    private static final double MANAGEMENT_FEES_AFTER_ONE_DEPOSIT = 94.9;

    // Create a new AccountHolder and a StrictBankAccount for it each time tests are executed.
    private AccountHolder mRossi;
    private BankAccount bankAccount;

    /**
     * Prepare the tests.
     */
    @BeforeEach
    public void setUp() {
        this.mRossi = new AccountHolder("Mario", "Rossi", 1);
        this.bankAccount = new StrictBankAccount(this.mRossi, INITIAL_BALANCE);
    }

    /**
     * Test the initial state of the StrictBankAccount.
     */
    @Test
    public void testInitialization() {
        assertEquals(INITIAL_BALANCE, this.bankAccount.getBalance());
        assertEquals(0, this.bankAccount.getTransactionsCount());
        assertEquals(this.mRossi, this.bankAccount.getAccountHolder());
    }

    /**
     * Perform a deposit of 100€, compute the management fees, and check that the balance is correctly reduced.
     */
    @Test
    public void testManagementFees() {
        this.bankAccount.deposit(this.mRossi.getUserID(), DEPOSIT_AMOUNT);
        this.bankAccount.chargeManagementFees(this.mRossi.getUserID());

        assertEquals(MANAGEMENT_FEES_AFTER_ONE_DEPOSIT, this.bankAccount.getBalance());
        assertEquals(0, this.bankAccount.getTransactionsCount());
    }

    /**
     * Test that withdrawing a negative amount causes a failure.
     */
    @Test
    public void testNegativeWithdraw() {
        try {
            this.bankAccount.withdraw(this.mRossi.getUserID(), -1);
            fail("Withdrawing a negative amount should have thrown an exception");
        } catch (final IllegalArgumentException e) {
            assertEquals(INITIAL_BALANCE, this.bankAccount.getBalance());
            assertEquals(0, this.bankAccount.getTransactionsCount());
        }
    }

    /**
     * Test that withdrawing more money than it is in the account is not allowed.
     */
    @Test
    public void testWithdrawingTooMuch() {
        this.bankAccount.deposit(this.mRossi.getUserID(), DEPOSIT_AMOUNT);

        try {
            this.bankAccount.withdraw(this.mRossi.getUserID(), DEPOSIT_AMOUNT + 1);
            fail("Withdrawing more than the available balance should have thrown an exception");
        } catch (final IllegalArgumentException e) {
            assertEquals(DEPOSIT_AMOUNT, this.bankAccount.getBalance());
            assertEquals(1, this.bankAccount.getTransactionsCount());
        }
    }
}
