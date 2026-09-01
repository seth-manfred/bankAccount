import java.util.List;

public interface AccountOperations {
    void deposit(double amount) throws InvalidAmountException;
    void deposit(double amount, String description) throws InvalidAmountException;
    void withdraw(double amount) throws InvalidAmountException, InsufficientFundsException;
    void withdraw(double amount, String description) throws InvalidAmountException, InsufficientFundsException;
    void transfer(AccountOperations targetAccount, double amount) throws InvalidAmountException, InsufficientFundsException;
    
    double getBalance();
    String getAccountNumber();
    String getAccountHolder();
    String getAccountType();
    
    void displayAccountInfo();
    List<Transaction> getTransactionHistory();
    void applyMonthlyMaintenance();
}
