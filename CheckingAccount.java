public class CheckingAccount extends BankAccount {
    private double overdraftLimit;
    private double transactionFee;

    public CheckingAccount(String accountNumber, String accountHolder, double initialBalance, double overdraftLimit, double transactionFee) {
        super(accountNumber, accountHolder, initialBalance);
        if (overdraftLimit < 0) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative.");
        }
        if (transactionFee < 0) {
            throw new IllegalArgumentException("Transaction fee cannot be negative.");
        }
        this.overdraftLimit = overdraftLimit;
        this.transactionFee = transactionFee;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        if (overdraftLimit < 0) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative.");
        }
        this.overdraftLimit = overdraftLimit;
    }

    public double getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(double transactionFee) {
        if (transactionFee < 0) {
            throw new IllegalArgumentException("Transaction fee cannot be negative.");
        }
        this.transactionFee = transactionFee;
    }

    @Override
    public String getAccountType() {
        return "Checking Account";
    }

    @Override
    public void withdraw(double amount, String description) throws InvalidAmountException, InsufficientFundsException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be strictly positive. Received: $" + amount);
        }

        double availableFunds = this.balance + this.overdraftLimit;
        if (amount > availableFunds) {
            throw new InsufficientFundsException(String.format(
                    "Withdrawal failed: Overdraft limit exceeded. Available (Balance + Overdraft): $%.2f, Requested: $%.2f",
                    availableFunds, amount));
        }

        // Deduct amount
        this.balance -= amount;
        TransactionType txnType = description.toLowerCase().contains("transfer") ? TransactionType.TRANSFER_OUT : TransactionType.WITHDRAWAL;
        recordTransaction(txnType, amount, description + (this.balance < 0 ? " (Overdraft Used)" : ""));
        System.out.printf("Successfully withdrew $%.2f from Checking Account [%s]. New Balance: $%.2f%n", amount, getAccountNumber(), balance);

        // If balance dipped below zero, apply overdraft fee if applicable
        if (this.balance < 0 && this.transactionFee > 0) {
            this.balance -= this.transactionFee;
            recordTransaction(TransactionType.FEE, this.transactionFee, "Overdraft Protection Fee Charged");
            System.out.printf("Notice: Overdraft fee of $%.2f applied. New Balance: $%.2f%n", transactionFee, balance);
        }
    }

    @Override
    public void applyMonthlyMaintenance() {
        // Fixed monthly maintenance fee of $5.00 for checking accounts
        double monthlyServiceFee = 5.0;
        this.balance -= monthlyServiceFee;
        recordTransaction(TransactionType.FEE, monthlyServiceFee, "Monthly Checking Account Maintenance Fee");
        System.out.printf("Monthly maintenance fee of $%.2f applied to Checking Account [%s]. New Balance: $%.2f%n",
                monthlyServiceFee, getAccountNumber(), balance);
    }

    @Override
    public void displayAccountInfo() {
        super.displayAccountInfo();
        System.out.printf("Overdraft Limit: $%.2f%n", overdraftLimit);
        System.out.printf("Transaction Fee: $%.2f%n", transactionFee);
        System.out.println("-----------------------------------------");
    }
}
