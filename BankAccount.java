import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BankAccount implements AccountOperations {
    private final String accountNumber;
    private final String accountHolder;
    protected double balance;
    private final LocalDateTime creationDate;
    private final List<Transaction> transactions;

    public BankAccount(String accountNumber, String accountHolder, double initialBalance) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be null or empty.");
        }
        if (accountHolder == null || accountHolder.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder name cannot be null or empty.");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }

        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder.trim();
        this.balance = initialBalance;
        this.creationDate = LocalDateTime.now();
        this.transactions = new ArrayList<>();

        if (initialBalance > 0) {
            recordTransaction(TransactionType.DEPOSIT, initialBalance, "Initial Account Opening Deposit");
        }
    }

    @Override
    public void deposit(double amount) throws InvalidAmountException {
        deposit(amount, "Standard Cash/Cheque Deposit");
    }

    @Override
    public void deposit(double amount, String description) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be strictly greater than zero. Received: $" + amount);
        }
        this.balance += amount;
        TransactionType txnType = description.toLowerCase().contains("transfer") ? TransactionType.TRANSFER_IN : TransactionType.DEPOSIT;
        recordTransaction(txnType, amount, description);
        System.out.printf("Successfully deposited $%.2f into account [%s]. New Balance: $%.2f%n", amount, accountNumber, balance);
    }

    @Override
    public void withdraw(double amount) throws InvalidAmountException, InsufficientFundsException {
        withdraw(amount, "Standard Cash Withdrawal");
    }

    @Override
    public abstract void withdraw(double amount, String description) throws InvalidAmountException, InsufficientFundsException;

    @Override
    public void transfer(AccountOperations targetAccount, double amount)
            throws InvalidAmountException, InsufficientFundsException {
        if (targetAccount == null) {
            throw new IllegalArgumentException("Target account cannot be null.");
        }
        if (this.equals(targetAccount) || this.accountNumber.equals(targetAccount.getAccountNumber())) {
            throw new IllegalArgumentException("Cannot transfer funds to the same account.");
        }
        if (amount <= 0) {
            throw new InvalidAmountException("Transfer amount must be positive. Received: $" + amount);
        }

        // Perform withdrawal from this account with transfer description
        this.withdraw(amount, "Transfer sent to Account " + targetAccount.getAccountNumber() + " (" + targetAccount.getAccountHolder() + ")");
        // Deposit into target account with descriptive message
        targetAccount.deposit(amount, "Transfer received from Account " + this.accountNumber + " (" + this.accountHolder + ")");

        System.out.printf("Successfully transferred $%.2f from [%s] to [%s].%n", amount, this.accountNumber, targetAccount.getAccountNumber());
    }

    protected void recordTransaction(TransactionType type, double amount, String description) {
        Transaction transaction = new Transaction(type, amount, this.balance, description);
        this.transactions.add(transaction);
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public String getAccountHolder() {
        return accountHolder;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    @Override
    public List<Transaction> getTransactionHistory() {
        return Collections.unmodifiableList(transactions);
    }

    @Override
    public void displayAccountInfo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("=========================================");
        System.out.printf("Account Type   : %s%n", getAccountType());
        System.out.printf("Account Number : %s%n", accountNumber);
        System.out.printf("Account Holder : %s%n", accountHolder);
        System.out.printf("Current Balance: $%.2f%n", balance);
        System.out.printf("Opened On      : %s%n", creationDate.format(formatter));
        System.out.println("=========================================");
    }
}
