public class SavingsAccount extends BankAccount {
    private double interestRate; // Annual interest rate in percentage, e.g. 3.5 for 3.5%
    private final double minimumBalance; // Minimum balance requirement

    public SavingsAccount(String accountNumber, String accountHolder, double initialBalance, double interestRate, double minimumBalance) {
        super(accountNumber, accountHolder, initialBalance);
        if (interestRate < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative.");
        }
        if (minimumBalance < 0) {
            throw new IllegalArgumentException("Minimum balance cannot be negative.");
        }
        if (initialBalance < minimumBalance) {
            throw new IllegalArgumentException(String.format("Initial balance ($%.2f) cannot be less than minimum balance ($%.2f).", initialBalance, minimumBalance));
        }
        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        if (interestRate < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative.");
        }
        this.interestRate = interestRate;
    }

    public double getMinimumBalance() {
        return minimumBalance;
    }

    @Override
    public String getAccountType() {
        return "Savings Account";
    }

    @Override
    public void withdraw(double amount, String description) throws InvalidAmountException, InsufficientFundsException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be strictly positive. Received: $" + amount);
        }
        if (this.balance - amount < minimumBalance) {
            throw new InsufficientFundsException(String.format(
                    "Withdrawal failed: Maintaining minimum balance of $%.2f is required. Current balance: $%.2f, Requested: $%.2f",
                    minimumBalance, balance, amount));
        }

        this.balance -= amount;
        TransactionType txnType = description.toLowerCase().contains("transfer") ? TransactionType.TRANSFER_OUT : TransactionType.WITHDRAWAL;
        recordTransaction(txnType, amount, description);
        System.out.printf("Successfully withdrew $%.2f from Savings Account [%s]. New Balance: $%.2f%n", amount, getAccountNumber(), balance);
    }

    public double calculateMonthlyInterest() {
        return (this.balance * (this.interestRate / 100.0)) / 12.0;
    }

    @Override
    public void applyMonthlyMaintenance() {
        double monthlyInterest = calculateMonthlyInterest();
        if (monthlyInterest > 0) {
            this.balance += monthlyInterest;
            recordTransaction(TransactionType.INTEREST, monthlyInterest, String.format("Monthly Interest Credit (@%.2f%% p.a.)", interestRate));
            System.out.printf("Interest applied to Savings Account [%s]: +$%.2f. New Balance: $%.2f%n", getAccountNumber(), monthlyInterest, balance);
        } else {
            System.out.printf("No interest accrued for Savings Account [%s] (Balance: $%.2f).%n", getAccountNumber(), balance);
        }
    }

    @Override
    public void displayAccountInfo() {
        super.displayAccountInfo();
        System.out.printf("Interest Rate  : %.2f%% p.a.%n", interestRate);
        System.out.printf("Min Balance Req: $%.2f%n", minimumBalance);
        System.out.println("-----------------------------------------");
    }
}
