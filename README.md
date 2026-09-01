# Apex Global Bank — Java OOP Banking System

A fully-featured, production-quality **Bank Account Management System** written in Java, demonstrating all four core Object-Oriented Programming (OOP) pillars: **Encapsulation, Abstraction, Inheritance, and Polymorphism**, along with Composition, Enums, and Custom Exception Handling.

---

## Project Architecture

```
bankAccount/
├── AccountOperations.java         # Interface (Abstraction)
├── BankAccount.java               # Abstract base class (Encapsulation + Abstraction)
├── SavingsAccount.java            # Subclass (Inheritance + Polymorphism)
├── CheckingAccount.java           # Subclass (Inheritance + Polymorphism)
├── Transaction.java               # Immutable audit record (Encapsulation + Composition)
├── TransactionType.java           # Enum for type-safe categorization
├── Bank.java                      # Institution manager (Composition)
├── InsufficientFundsException.java # Custom checked exception
├── InvalidAmountException.java    # Custom checked exception
├── AccountNotFoundException.java  # Custom checked exception
├── Main.java                      # Interactive CLI application entry point
├── EXPLANATION.md                 # Line-by-line code explanation guide
└── README.md                      # Project overview (this file)
```

---

## OOP Concepts Demonstrated

| OOP Principle | Where Applied | How |
|:---|:---|:---|
| **Encapsulation** | `BankAccount`, `Transaction` | Private/protected fields; access via controlled getters/setters |
| **Abstraction** | `AccountOperations`, `BankAccount` | Interface contract + abstract methods hide implementation detail |
| **Inheritance** | `SavingsAccount`, `CheckingAccount` | Both extend `BankAccount`, inheriting shared account logic |
| **Polymorphism (Dynamic)** | `withdraw()`, `applyMonthlyMaintenance()`, `displayAccountInfo()` | Overridden methods behave differently at runtime per account type |
| **Polymorphism (Static)** | `deposit()`, `withdraw()`, `readDoubleInput()` | Method overloading with multiple parameter signatures |
| **Composition** | `Bank` → `BankAccount` → `Transaction` | Objects contain other objects to model real-world relationships |
| **Custom Exceptions** | `InsufficientFundsException`, `InvalidAmountException`, etc. | OOP-style error handling propagated through the call chain |
| **Enum** | `TransactionType` | Type-safe categorization of financial events |

---

## Features

- **Open Savings Accounts** — configurable interest rate and minimum balance requirement
- **Open Checking Accounts** — configurable overdraft limit and transaction fee
- **Deposit Funds** — with optional custom memos per transaction
- **Withdraw Funds** — respects minimum balance (Savings) and overdraft protection (Checking)
- **Transfer Funds** — bidirectional inter-account transfers, automatically categorized as `TRANSFER_IN` / `TRANSFER_OUT`
- **Full Transaction History / Statement** — UUID-identified, timestamped, formatted ledger per account
- **Monthly Maintenance** — interest crediting for Savings accounts, fee deduction for Checking accounts
- **Account Registry** — list all accounts with a live total reserve balance
- **Robust Input Validation** — all inputs are protected from crashes with clean error messages

---

## Compile & Run

### Prerequisites
- Java JDK 17+ (project targets `--release 25` for full syntax feature support)

### Compile
```bash
javac --release 25 *.java
```

### Run
```bash
java Main
```

---

## Sample Menu

```
=================================================
     WELCOME TO APEX GLOBAL BANK SYSTEM     
=================================================
[System Info] Pre-loading demo accounts for instant testing...
[System Info] Demo accounts ready: Alice Johnson (ACC-1001), Bob Smith (ACC-1002).

================ MAIN BANK MENU ================
 1. Open New Savings Account
 2. Open New Checking Account
 3. Deposit Money
 4. Withdraw Money
 5. Transfer Funds
 6. View Account Details & Balance
 7. View Full Transaction History
 8. Apply Monthly Interest & Maintenance Fees
 9. List All Bank Accounts & Total Reserves
 10. Exit System
================================================
```

---

## Transaction Statement Example

```
======================= TRANSACTION STATEMENT =======================
Account: ACC-1001 | Holder: Alice Johnson | Type: Savings Account
---------------------------------------------------------------------
[2026-09-01 09:23:58] TXN-FFCB6DCE | Type: DEPOSIT       | Amount: $   1500.00 | Balance: $   1500.00 | Note: Initial Account Opening Deposit
[2026-09-01 09:23:58] TXN-9C7E4B77 | Type: DEPOSIT       | Amount: $    500.00 | Balance: $   2000.00 | Note: Salary Deposit
[2026-09-01 09:23:58] TXN-A1290585 | Type: DEPOSIT       | Amount: $    250.00 | Balance: $   2250.00 | Note: Bonus Deposit
[2026-09-01 09:23:58] TXN-3284AE9C | Type: WITHDRAWAL    | Amount: $    300.00 | Balance: $   1950.00 | Note: Savings Account Withdrawal
[2026-09-01 09:23:58] TXN-807CBCA0 | Type: TRANSFER_OUT  | Amount: $    300.00 | Balance: $   1950.00 | Note: Transfer sent to Account ACC-1002 (Bob Smith)
---------------------------------------------------------------------
Current Cleared Balance: $1950.00
=====================================================================
```

---

## Documentation

See [EXPLANATION.md](./EXPLANATION.md) for an exhaustive **line-by-line breakdown** of every file, including:
- Exact OOP principle demonstrated per line/block
- Java language mechanics and JVM concepts
- Design decisions and architecture rationale

---

## Author

Built with ❤️ as part of Java OOP coursework.
