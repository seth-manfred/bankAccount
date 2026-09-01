import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Bank {
    private final String bankName;
    private final Map<String, BankAccount> accounts;
    private int accountSequence;

    public Bank(String bankName) {
        if (bankName == null || bankName.trim().isEmpty()) {
            throw new IllegalArgumentException("Bank name cannot be null or empty.");
        }
        this.bankName = bankName.trim();
        this.accounts = new LinkedHashMap<>();
        this.accountSequence = 1000;
    }

    public synchronized String generateNextAccountNumber() {
        accountSequence++;
        return String.format("ACC-%04d", accountSequence);
    }

    public SavingsAccount openSavingsAccount(String accountHolder, double initialDeposit, double interestRate, double minimumBalance) {
        String accountNumber = generateNextAccountNumber();
        SavingsAccount account = new SavingsAccount(accountNumber, accountHolder, initialDeposit, interestRate, minimumBalance);
        accounts.put(accountNumber, account);
        return account;
    }

    public CheckingAccount openCheckingAccount(String accountHolder, double initialDeposit, double overdraftLimit, double transactionFee) {
        String accountNumber = generateNextAccountNumber();
        CheckingAccount account = new CheckingAccount(accountNumber, accountHolder, initialDeposit, overdraftLimit, transactionFee);
        accounts.put(accountNumber, account);
        return account;
    }

    public BankAccount findAccount(String accountNumber) throws AccountNotFoundException {
        if (accountNumber == null || !accounts.containsKey(accountNumber.trim().toUpperCase())) {
            throw new AccountNotFoundException("Account with number '" + accountNumber + "' was not found in the bank directory.");
        }
        return accounts.get(accountNumber.trim().toUpperCase());
    }

    public void transferFunds(String sourceAccNumber, String targetAccNumber, double amount)
            throws AccountNotFoundException, InvalidAmountException, InsufficientFundsException {
        BankAccount source = findAccount(sourceAccNumber);
        BankAccount target = findAccount(targetAccNumber);

        source.transfer(target, amount);
    }

    public void applyMonthlyMaintenanceToAll() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts registered in the bank to process.");
            return;
        }
        System.out.println("\n--- Processing Monthly Maintenance & Interest for All Accounts ---");
        for (BankAccount account : accounts.values()) {
            account.applyMonthlyMaintenance();
        }
        System.out.println("--- Monthly Maintenance Processing Completed ---\n");
    }

    public Collection<BankAccount> getAllAccounts() {
        return Collections.unmodifiableCollection(accounts.values());
    }

    public double getTotalBankAssets() {
        double total = 0.0;
        for (BankAccount account : accounts.values()) {
            total += account.getBalance();
        }
        return total;
    }

    public String getBankName() {
        return bankName;
    }

    public int getAccountCount() {
        return accounts.size();
    }
}
