/**
 * Week 1 - Topic 6: Error Handling and Exceptions in Java
 * Author: YuvaIntern Java Development Intern
 * 
 * Placement Focus & Concept Summary:
 * - An Exception is an abnormal condition or event that disrupts the normal flow of program instructions.
 * - Java Exception Hierarchy:
 *     Throwable (Root)
 *       ├── Error (Fatal, unrecoverable system issues: OutOfMemoryError, StackOverflowError)
 *       └── Exception (Recoverable conditions that programs should catch)
 *             ├── Checked Exceptions: Verified at compile-time (IOException, SQLException, ClassNotFoundException)
 *             └── Unchecked / Runtime Exceptions: Occur at runtime (ArithmeticException, NullPointerException, ArrayIndexOutOfBoundsException)
 * - Key Exception Keywords:
 *     - try: Encloses code that might generate an exception.
 *     - catch: Handles the specific exception if one is thrown.
 *     - finally: Block that executes unconditionally (ideal for resource deallocation).
 *     - throw: Explicitly throws an instance of an exception.
 *     - throws: Declares in a method signature that the method may propagate an exception.
 * - Custom Exceptions: User-defined exception classes extending Exception (Checked) or RuntimeException (Unchecked).
 */

// Custom Exception for Banking Domain
class InsufficientBalanceException extends Exception {
    private final double attemptedAmount;
    private final double currentBalance;

    public InsufficientBalanceException(String message, double attemptedAmount, double currentBalance) {
        super(message);
        this.attemptedAmount = attemptedAmount;
        this.currentBalance = currentBalance;
    }

    public double getAttemptedAmount() {
        return attemptedAmount;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }
}

public class ExceptionHandlingDemo {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  TOPIC 6: ERROR HANDLING AND EXCEPTIONS IN JAVA");
        System.out.println("============================================================\n");

        // ---------------------------------------------------------------------
        // 1. Basic try-catch: ArithmeticException (Divide by Zero)
        // ---------------------------------------------------------------------
        System.out.println("--- 1. Basic try-catch (ArithmeticException) ---");
        int totalMarks = 450;
        int completedSubjects = 0; // Simulated edge-case

        try {
            System.out.println("Attempting division: " + totalMarks + " / " + completedSubjects);
            int average = totalMarks / completedSubjects;
            System.out.println("Calculated Average: " + average);
        } catch (ArithmeticException ex) {
            System.out.println("Caught ArithmeticException: " + ex.getMessage() +
                    " (Cannot divide by zero! Guard check or fallback triggered)");
        }

        // ---------------------------------------------------------------------
        // 2. Multiple catch Blocks and Order (Specific to General)
        // ---------------------------------------------------------------------
        System.out.println("\n--- 2. Multiple Catch Blocks & ArrayIndexOutOfBoundsException ---");
        int[] scores = {90, 85, 78};

        try {
            System.out.println("Accessing scores[1]: " + scores[1]);
            System.out.println("Accessing scores[5] (Invalid index):");
            int outOfBoundsValue = scores[5]; // Throws ArrayIndexOutOfBoundsException
            System.out.println("Value: " + outOfBoundsValue);
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Handled Specific Error: Array index out of range! Detail: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Handled Generic Exception: " + ex.getMessage());
        }

        // ---------------------------------------------------------------------
        // 3. try-catch-finally Block (Resource Cleanup Guarantee)
        // ---------------------------------------------------------------------
        System.out.println("\n--- 3. try-catch-finally Demonstration ---");
        String databaseConnectionState = "DISCONNECTED";

        try {
            databaseConnectionState = "CONNECTED";
            System.out.println("Database Status: " + databaseConnectionState);
            System.out.println("Executing sensitive transaction query...");
            // Simulate potential number parsing failure
            int parsedRecord = Integer.parseInt("invalid_number_format");
            System.out.println("Parsed: " + parsedRecord);
        } catch (NumberFormatException ex) {
            System.out.println("Caught NumberFormatException: Failed to parse numeric value.");
        } finally {
            databaseConnectionState = "DISCONNECTED";
            System.out.println("Finally Block Executed: Cleanup completed. Database Status: " + databaseConnectionState);
        }

        // ---------------------------------------------------------------------
        // 4. Custom Exception & throw / throws Keywords (ATM Banking System)
        // ---------------------------------------------------------------------
        System.out.println("\n--- 4. Practical Placement Use Case: Bank ATM Cash Withdrawal ---");
        double accountBalance = 12500.00;
        double firstWithdrawal = 3000.00;
        double secondWithdrawal = 15000.00;

        System.out.printf("Starting Account Balance: Rs. %,.2f%n", accountBalance);

        // First Transaction: Valid
        try {
            accountBalance = processWithdrawal(accountBalance, firstWithdrawal);
            System.out.printf("Transaction 1 SUCCESS: Withdrew Rs. %,.2f | New Balance: Rs. %,.2f%n",
                    firstWithdrawal, accountBalance);
        } catch (InsufficientBalanceException ex) {
            System.out.println("Transaction 1 FAILED: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid Input: " + ex.getMessage());
        }

        // Second Transaction: Exceeds balance -> Custom Exception thrown and caught
        try {
            accountBalance = processWithdrawal(accountBalance, secondWithdrawal);
            System.out.printf("Transaction 2 SUCCESS: Withdrew Rs. %,.2f | New Balance: Rs. %,.2f%n",
                    secondWithdrawal, accountBalance);
        } catch (InsufficientBalanceException ex) {
            System.out.printf("Transaction 2 FAILED: %s (Requested: Rs. %,.2f, Available: Rs. %,.2f)%n",
                    ex.getMessage(), ex.getAttemptedAmount(), ex.getCurrentBalance());
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid Input: " + ex.getMessage());
        }

        // Third Transaction: Negative amount -> IllegalArgumentException
        try {
            accountBalance = processWithdrawal(accountBalance, -500.00);
        } catch (InsufficientBalanceException ex) {
            System.out.println("Transaction 3 FAILED: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println("Transaction 3 REJECTED: " + ex.getMessage());
        }

        System.out.printf("Final Account Balance: Rs. %,.2f%n", accountBalance);
        System.out.println("============================================================\n");
    }

    /**
     * Business method demonstrating 'throws' declaration and 'throw' usage.
     * Validates business rules before updating account balance.
     */
    public static double processWithdrawal(double currentBalance, double amountToWithdraw)
            throws InsufficientBalanceException, IllegalArgumentException {
        
        if (amountToWithdraw <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be strictly greater than 0.");
        }

        if (amountToWithdraw > currentBalance) {
            throw new InsufficientBalanceException(
                    "Insufficient funds for this withdrawal request.",
                    amountToWithdraw,
                    currentBalance
            );
        }

        return currentBalance - amountToWithdraw;
    }
}
