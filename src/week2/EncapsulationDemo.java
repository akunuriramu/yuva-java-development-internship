package week2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Week 2 - Topic 2: Encapsulation, Data Hiding, and Immutable Class Architecture
 * Author: YuvaIntern Java Development Intern
 * 
 * Principal Architectural Takeaways:
 * - Encapsulation binds state (fields) and behavior (methods) together while hiding internal implementation details.
 * - Private fields prevent direct, unauthorized modification from outside the class boundary.
 * - Accessor (Getter) and Mutator (Setter) methods enforce data validation constraints (invariants).
 * - Immutable Class Design Rules (Effective Java Item 17):
 *     1. Declare the class as 'final' so it cannot be extended/subclassed.
 *     2. Make all fields 'private' and 'final'.
 *     3. Do not provide any mutator (setter) methods.
 *     4. Ensure exclusive access to any mutable components via Defensive Copying in both constructors and accessors.
 */
public class EncapsulationDemo {

    // =========================================================================
    // 1. Encapsulated Mutable Class with Data Validation Guards
    // =========================================================================
    public static class SecureBankAccount {
        // Data Hiding: Private fields cannot be accessed directly from outside
        private final String accountNumber;
        private String accountHolderName;
        private double balance;
        private String kycStatus; // "PENDING", "VERIFIED", "REJECTED"

        public SecureBankAccount(String accountNumber, String accountHolderName, double initialDeposit) {
            if (accountNumber == null || accountNumber.trim().isEmpty()) {
                throw new IllegalArgumentException("Account number cannot be null or empty.");
            }
            if (accountHolderName == null || accountHolderName.trim().length() < 3) {
                throw new IllegalArgumentException("Account holder name must be at least 3 characters long.");
            }
            if (initialDeposit < 1000.0) { // Minimum initial deposit business rule
                throw new IllegalArgumentException("Minimum initial deposit of Rs. 1,000 required. Attempted: Rs. " + initialDeposit);
            }

            this.accountNumber = accountNumber;
            this.accountHolderName = accountHolderName.trim();
            this.balance = initialDeposit;
            this.kycStatus = "PENDING";
        }

        // Getter: Read-only access to account number (No setter -> read-only invariant)
        public String getAccountNumber() {
            return this.accountNumber;
        }

        public String getAccountHolderName() {
            return this.accountHolderName;
        }

        // Setter with data integrity validation filter
        public void setAccountHolderName(String newName) {
            if (newName != null && newName.trim().length() >= 3) {
                System.out.println("  [Update] Account holder updated from '" + this.accountHolderName + "' to '" + newName.trim() + "'.");
                this.accountHolderName = newName.trim();
            } else {
                System.out.println("  [Validation Rejected] New name must have at least 3 characters. Update ignored.");
            }
        }

        public double getBalance() {
            return this.balance;
        }

        public String getKycStatus() {
            return this.kycStatus;
        }

        public void verifyKyc(String officerToken) {
            if ("COMPLIANCE_SEC_2026".equals(officerToken)) {
                this.kycStatus = "VERIFIED";
                System.out.println("  [KYC Compliance] Account " + this.accountNumber + " verified successfully.");
            } else {
                System.out.println("  [KYC Error] Invalid compliance token. KYC remains " + this.kycStatus);
            }
        }

        // Controlled business mutation: Deposit
        public void deposit(double amount) {
            if (amount <= 0) {
                System.out.println("  [Deposit Failed] Deposit amount must be positive. Received: Rs. " + amount);
                return;
            }
            this.balance += amount;
            System.out.printf("  [Deposit Success] Rs. %,.2f deposited. Updated Balance: Rs. %,.2f%n", amount, this.balance);
        }

        // Controlled business mutation: Withdraw with overdraft guard
        public boolean withdraw(double amount) {
            if (amount <= 0) {
                System.out.println("  [Withdraw Failed] Withdrawal amount must be positive. Received: Rs. " + amount);
                return false;
            }
            if (!"VERIFIED".equals(this.kycStatus) && amount > 10000.0) {
                System.out.println("  [Withdraw Restricted] High-value transaction requires VERIFIED KYC status.");
                return false;
            }
            if (amount > this.balance) {
                System.out.printf("  [Withdraw Rejected] Insufficient funds! Requested: Rs. %,.2f, Available: Rs. %,.2f%n",
                        amount, this.balance);
                return false;
            }

            this.balance -= amount;
            System.out.printf("  [Withdraw Success] Rs. %,.2f withdrawn. Updated Balance: Rs. %,.2f%n", amount, this.balance);
            return true;
        }

        public void printAccountSummary() {
            System.out.printf("  | Account: %-12s | Holder: %-15s | KYC: %-8s | Balance: Rs. %,10.2f |%n",
                    this.accountNumber, this.accountHolderName, this.kycStatus, this.balance);
        }
    }

    // =========================================================================
    // 2. Production-Grade Immutable Class with Defensive Copying
    // =========================================================================
    // Rule 1: 'final' class prevents subclassing
    public static final class ImmutableAuditRecord {
        // Rule 2: Private final fields
        private final String transactionId;
        private final double transactionAmount;
        private final Date timestamp;           // Mutable object reference!
        private final List<String> auditTags;   // Mutable collection reference!

        // Constructor: Rule 4 - Defensive copying on ingest
        public ImmutableAuditRecord(String transactionId, double transactionAmount, Date timestamp, List<String> auditTags) {
            if (transactionId == null || timestamp == null || auditTags == null) {
                throw new IllegalArgumentException("Audit parameters cannot be null.");
            }

            this.transactionId = transactionId;
            this.transactionAmount = transactionAmount;

            // DEFENSIVE COPY of mutable Date: Prevents caller from altering internal state
            this.timestamp = new Date(timestamp.getTime());

            // DEFENSIVE COPY of mutable List: Creates an independent collection
            this.auditTags = new ArrayList<>(auditTags);
        }

        // Rule 3: No setters provided

        public String getTransactionId() {
            return this.transactionId;
        }

        public double getTransactionAmount() {
            return this.transactionAmount;
        }

        // DEFENSIVE COPY on getter: Return new Date clone
        public Date getTimestamp() {
            return new Date(this.timestamp.getTime());
        }

        // DEFENSIVE RETURN on getter: Return unmodifiable view or copy
        public List<String> getAuditTags() {
            return Collections.unmodifiableList(this.auditTags);
        }

        @Override
        public String toString() {
            return String.format("AuditRecord[TX=%s, Amount=Rs.%,.2f, Date=%s, Tags=%s]",
                    this.transactionId, this.transactionAmount, this.timestamp, this.auditTags);
        }
    }

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("  WEEK 2 - TOPIC 2: ENCAPSULATION, DATA INTEGRITY & IMMUTABILITY");
        System.out.println("==========================================================================\n");

        // ---------------------------------------------------------------------
        // 1. Encapsulation with Business Invariant Validation
        // ---------------------------------------------------------------------
        System.out.println("--- 1. Encapsulated Bank Account with Data Validation Filters ---");
        SecureBankAccount account = new SecureBankAccount("SB-10928475", "Rohan Verma", 25000.00);
        account.printAccountSummary();

        System.out.println("\nTesting Validation Guards:");
        // Attempt negative deposit
        account.deposit(-500.0);

        // Attempt unauthorized high-value withdrawal without verified KYC
        account.withdraw(15000.0);

        // Verify KYC and re-attempt withdrawal
        System.out.println("\nSubmitting KYC verification token:");
        account.verifyKyc("COMPLIANCE_SEC_2026");
        account.withdraw(15000.0);

        // Attempt overdraft
        account.withdraw(20000.0);

        // Attempt invalid name update
        account.setAccountHolderName("Al"); // Too short
        account.setAccountHolderName("Rohan V. Sharma"); // Valid

        System.out.println("\nFinal Encapsulated Account State:");
        account.printAccountSummary();

        // ---------------------------------------------------------------------
        // 2. Immutable Class Design & Defensive Copying Demonstration
        // ---------------------------------------------------------------------
        System.out.println("\n--- 2. Immutable Class Design & Defensive Copying Verification ---");
        
        Date txDate = new Date(); // Current timestamp
        List<String> tags = new ArrayList<>();
        tags.add("UPI-TRANSFER");
        tags.add("MERCHANT-SETTLEMENT");

        // Create immutable instance
        ImmutableAuditRecord auditRecord = new ImmutableAuditRecord("TXN-998877", 4500.00, txDate, tags);
        System.out.println("Initial Immutable Record:");
        System.out.println("  " + auditRecord);

        // External Attack Attempt 1: Mutating the original Date passed to constructor
        System.out.println("\n[Attack 1] Mutating external 'txDate' reference (shifting to year 1970)...");
        txDate.setTime(0L); // Jan 1, 1970
        System.out.println("External Date is now: " + txDate);
        System.out.println("Immutable Record Date: " + auditRecord.getTimestamp() + " (Unchanged due to constructor defensive copy!)");

        // External Attack Attempt 2: Mutating the original List passed to constructor
        System.out.println("\n[Attack 2] Adding malicious tag to external 'tags' list...");
        tags.add("MALICIOUS-INJECTED-TAG");
        System.out.println("External tags list: " + tags);
        System.out.println("Immutable Record Tags: " + auditRecord.getAuditTags() + " (Unchanged due to constructor defensive copy!)");

        // External Attack Attempt 3: Mutating the collection returned by getter
        System.out.println("\n[Attack 3] Attempting to mutate List returned from getter...");
        try {
            auditRecord.getAuditTags().add("HACKER-TAG");
        } catch (UnsupportedOperationException ex) {
            System.out.println("  [Caught Expected Exception]: Cannot mutate unmodifiable list returned by getter!");
        }

        System.out.println("\nFinal State of Immutable Audit Record (Completely Intact):");
        System.out.println("  " + auditRecord);

        System.out.println("\n==========================================================================");
        System.out.println("  TOPIC 2 DEMONSTRATION COMPLETE: DATA INTEGRITY PRESERVED");
        System.out.println("==========================================================================\n");
    }
}
