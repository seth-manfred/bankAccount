import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Bank bank = new Bank("Apex Global Bank");

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("     WELCOME TO " + bank.getBankName().toUpperCase() + " SYSTEM     ");
        System.out.println("=================================================");

        seedInitialAccounts();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readIntInput("Select an option (1-10): ");

            switch (choice) {
                case 1 -> handleOpenSavingsAccount();
                case 2 -> handleOpenCheckingAccount();
                case 3 -> handleDeposit();
                case 4 -> handleWithdraw();
                case 5 -> handleTransfer();
                case 6 -> handleCheckBalance();
                case 7 -> handleViewTransactionHistory();
                case 8 -> handleRunMonthlyMaintenance();
                case 9 -> handleListAllAccounts();
                case 10 -> {
                    System.out.println("\nThank you for choosing " + bank.getBankName() + ". Have a great day!");
                    running = false;
                }
                default -> System.out.println("[Error] Invalid option selected. Please choose a number between 1 and 10.");
            }
        }

        scanner.close();
    }

    private static void seedInitialAccounts() {
        System.out.println("[System Info] Pre-loading demo accounts for instant testing...");
        SavingsAccount acc1 = bank.openSavingsAccount("Alice Johnson", 1500.0, 4.0, 100.0);
        CheckingAccount acc2 = bank.openCheckingAccount("Bob Smith", 800.0, 300.0, 2.50);
        
        try {
            acc1.deposit(500.0, "Salary Deposit");
            acc2.withdraw(200.0, "ATM Cash Withdrawal");
        } catch (Exception ignored) {}
        System.out.println("[System Info] Demo accounts ready: Alice Johnson (ACC-1001), Bob Smith (ACC-1002).\n");
    }

    private static void printMainMenu() {
        System.out.println("================ MAIN BANK MENU ================");
        System.out.println(" 1. Open New Savings Account");
        System.out.println(" 2. Open New Checking Account");
        System.out.println(" 3. Deposit Money");
        System.out.println(" 4. Withdraw Money");
        System.out.println(" 5. Transfer Funds");
        System.out.println(" 6. View Account Details & Balance");
        System.out.println(" 7. View Full Transaction History");
        System.out.println(" 8. Apply Monthly Interest & Maintenance Fees");
        System.out.println(" 9. List All Bank Accounts & Total Reserves");
        System.out.println(" 10. Exit System");
        System.out.println("================================================");
    }

    private static void handleOpenSavingsAccount() {
        System.out.println("\n--- [1] Open New Savings Account ---");
        System.out.print("Enter Account Holder Full Name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("[Error] Account holder name cannot be empty.");
            return;
        }

        double minBalance = readDoubleInput("Enter Minimum Balance Requirement ($) [default 50.0]: ", 50.0);
        double initialDeposit = readDoubleInput("Enter Initial Deposit Amount ($): ");
        double interestRate = readDoubleInput("Enter Annual Interest Rate (%) [e.g., 3.5]: ", 3.5);

        try {
            SavingsAccount account = bank.openSavingsAccount(name, initialDeposit, interestRate, minBalance);
            System.out.println("\n[Success] Savings Account successfully opened!");
            account.displayAccountInfo();
        } catch (IllegalArgumentException e) {
            System.out.println("[Error] Failed to create account: " + e.getMessage());
        }
    }

    private static void handleOpenCheckingAccount() {
        System.out.println("\n--- [2] Open New Checking Account ---");
        System.out.print("Enter Account Holder Full Name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("[Error] Account holder name cannot be empty.");
            return;
        }

        double initialDeposit = readDoubleInput("Enter Initial Deposit Amount ($): ");
        double overdraftLimit = readDoubleInput("Enter Overdraft Protection Limit ($) [e.g., 200.0]: ", 200.0);
        double transactionFee = readDoubleInput("Enter Overdraft Transaction Fee ($) [e.g., 2.50]: ", 2.50);

        try {
            CheckingAccount account = bank.openCheckingAccount(name, initialDeposit, overdraftLimit, transactionFee);
            System.out.println("\n[Success] Checking Account successfully opened!");
            account.displayAccountInfo();
        } catch (IllegalArgumentException e) {
            System.out.println("[Error] Failed to create account: " + e.getMessage());
        }
    }

    private static void handleDeposit() {
        System.out.println("\n--- [3] Deposit Funds ---");
        System.out.print("Enter Account Number (e.g., ACC-1001): ");
        String accNum = scanner.nextLine().trim();

        try {
            BankAccount account = bank.findAccount(accNum);
            double amount = readDoubleInput("Enter Amount to Deposit ($): ");
            System.out.print("Enter Deposit Description (Optional, press Enter for default): ");
            String note = scanner.nextLine().trim();

            if (note.isEmpty()) {
                account.deposit(amount);
            } else {
                account.deposit(amount, note);
            }
        } catch (AccountNotFoundException | InvalidAmountException e) {
            System.out.println("[Error] " + e.getMessage());
        }
    }

    private static void handleWithdraw() {
        System.out.println("\n--- [4] Withdraw Funds ---");
        System.out.print("Enter Account Number (e.g., ACC-1001): ");
        String accNum = scanner.nextLine().trim();

        try {
            BankAccount account = bank.findAccount(accNum);
            double amount = readDoubleInput("Enter Amount to Withdraw ($): ");
            account.withdraw(amount);
        } catch (AccountNotFoundException | InvalidAmountException | InsufficientFundsException e) {
            System.out.println("[Error] " + e.getMessage());
        }
    }

    private static void handleTransfer() {
        System.out.println("\n--- [5] Transfer Funds Between Accounts ---");
        System.out.print("Enter Source Account Number (From): ");
        String fromAcc = scanner.nextLine().trim();

        System.out.print("Enter Destination Account Number (To): ");
        String toAcc = scanner.nextLine().trim();

        if (fromAcc.equalsIgnoreCase(toAcc)) {
            System.out.println("[Error] Source and Destination accounts cannot be the same.");
            return;
        }

        double amount = readDoubleInput("Enter Transfer Amount ($): ");

        try {
            bank.transferFunds(fromAcc, toAcc, amount);
        } catch (AccountNotFoundException | InvalidAmountException | InsufficientFundsException | IllegalArgumentException e) {
            System.out.println("[Error] Transfer Failed: " + e.getMessage());
        }
    }

    private static void handleCheckBalance() {
        System.out.println("\n--- [6] View Account Details & Balance ---");
        System.out.print("Enter Account Number (e.g., ACC-1001): ");
        String accNum = scanner.nextLine().trim();

        try {
            BankAccount account = bank.findAccount(accNum);
            account.displayAccountInfo();
        } catch (AccountNotFoundException e) {
            System.out.println("[Error] " + e.getMessage());
        }
    }

    private static void handleViewTransactionHistory() {
        System.out.println("\n--- [7] View Full Transaction History ---");
        System.out.print("Enter Account Number (e.g., ACC-1001): ");
        String accNum = scanner.nextLine().trim();

        try {
            BankAccount account = bank.findAccount(accNum);
            List<Transaction> history = account.getTransactionHistory();

            System.out.println("\n======================= TRANSACTION STATEMENT =======================");
            System.out.printf("Account: %s | Holder: %s | Type: %s%n",
                    account.getAccountNumber(), account.getAccountHolder(), account.getAccountType());
            System.out.println("---------------------------------------------------------------------");

            if (history.isEmpty()) {
                System.out.println("No transactions recorded yet.");
            } else {
                for (Transaction txn : history) {
                    System.out.println(txn);
                }
            }
            System.out.println("---------------------------------------------------------------------");
            System.out.printf("Current Cleared Balance: $%.2f%n", account.getBalance());
            System.out.println("=====================================================================");
        } catch (AccountNotFoundException e) {
            System.out.println("[Error] " + e.getMessage());
        }
    }

    private static void handleRunMonthlyMaintenance() {
        System.out.println("\n--- [8] Apply Monthly Interest & Maintenance ---");
        bank.applyMonthlyMaintenanceToAll();
    }

    private static void handleListAllAccounts() {
        System.out.println("\n======================== ALL REGISTERED ACCOUNTS ========================");
        Collection<BankAccount> accounts = bank.getAllAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
        } else {
            System.out.printf("%-10s | %-20s | %-18s | %-12s%n", "Account No", "Holder Name", "Account Type", "Balance ($)");
            System.out.println("-------------------------------------------------------------------------");
            for (BankAccount acc : accounts) {
                System.out.printf("%-10s | %-20s | %-18s | $%10.2f%n",
                        acc.getAccountNumber(), acc.getAccountHolder(), acc.getAccountType(), acc.getBalance());
            }
            System.out.println("-------------------------------------------------------------------------");
            System.out.printf("Total Bank Accounts: %d | Total Reserve Assets: $%.2f%n",
                    bank.getAccountCount(), bank.getTotalBankAssets());
        }
        System.out.println("=========================================================================\n");
    }

    private static int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("[Input Error] Please enter a valid integer.");
            }
        }
    }

    private static double readDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("[Input Error] Please enter a valid numeric value (e.g. 100.50).");
            }
        }
    }

    private static double readDoubleInput(String prompt, double defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.printf("[Notice] Invalid input. Defaulting to: $%.2f%n", defaultValue);
            return defaultValue;
        }
    }
}
